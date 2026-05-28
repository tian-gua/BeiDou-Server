package org.gms.melon;

import org.gms.client.Character;
import org.gms.client.SkillFactory;
import org.gms.client.inventory.InventoryType;
import org.gms.client.status.MonsterStatus;
import org.gms.client.status.MonsterStatusEffect;
import org.gms.constants.skills.ILWizard;

import org.gms.server.ItemInformationProvider;
import org.gms.server.StatEffect;
import org.gms.server.life.Monster;
import org.gms.server.maps.MapObject;
import org.gms.server.maps.MapleMap;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class MobVacHandler {

    public static volatile int mapId;
    public static Thread t;
    private static volatile boolean running; // 标志位，用于控制线程运行状态

    private static volatile Point vacPosition = null;
    private static volatile MapleMap vacMap = null;

    private static final ItemInformationProvider ii = ItemInformationProvider.getInstance();


    public synchronized static void mobVac(boolean on, Character player) {
        if (on && running) {
            player.dropMessage("吸怪已开启，无需重复开启。");
            return;
        }

        if (!on && !running) {
            player.dropMessage("吸怪已关闭，无需重复关闭。");
            return;
        }

        if (on && !running) {
            start(player);
            player.dropMessage("吸怪已开启。");
            return;
        }

        if (!on && running) {
            stop(); // 调用 stop 方法停止线程
            player.dropMessage("吸怪已关闭。");
        }
    }

    public synchronized static void resetPosition(Character player) {
        if (!running) {
            player.dropMessage("吸怪未开启，无法重置位置。");
            return;
        }
        vacPosition = player.getPosition();
        vacMap.setVacPoint(vacPosition);

        // 清空怪物
        vacMap.resetMapObjects();

        player.dropMessage("吸怪位置已重置。");
    }

    public synchronized static void start(Character player) {
        vacPosition = player.getPosition();
        vacMap = player.getMap();
        vacMap.setVacPoint(vacPosition);
        running = true; // 启动线程时设置标志位为 true
        t = new Thread(() -> {
            int loopCount = 0;

            while (running) {
                // 在这里实现线程的主要逻辑
                var allPlayer = vacMap.getAllPlayer();
                if (allPlayer.size() > 1) {
                    for (MapObject mo : allPlayer) {
                        if (mo.getObjectId() != player.getObjectId()) {
                            var mapPlayer = (Character) mo;
                            if (mapPlayer.getParty() == null || player.getParty() == null || mapPlayer.getParty()
                                    .getId() != player.getParty().getId()) {
                                running = false;
                                vacMap.setVacPoint(null);
                                vacPosition = null;
                                player.dropMessage("吸怪已关闭。");

                                // 清空怪物
                                vacMap.resetMapObjects();

                                break;
                            }
                        }
                    }
                } else {
                    if (allPlayer.isEmpty() || allPlayer.getFirst().getObjectId() != player.getObjectId()) {
                        running = false;
                        vacMap.setVacPoint(null);
                        vacPosition = null;
                        player.dropMessage("吸怪已关闭。");

                        // 清空怪物
                        vacMap.resetMapObjects();

                        break; // 如果地图上没有玩家了，或者地图上有玩家但不是当前玩家，则停止线程
                    }
                }

                for (Monster monster : vacMap.getAllMonsters()) {
                    if (!monster.isBoss() && monster.isAlive()) {
                        applyBuff(player, monster);
                    }
                }

                loopCount++;
                if (loopCount >= 100) {
                    int mesoGain = player.sellAllItemsFromPosition(ii, InventoryType.EQUIP, (short) 25);
                    player.message("通过【自动卖装备】获得 " + mesoGain / 10000 + "万 金币。");

                    loopCount = 0;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态
                }
            }
        });
        t.start();
    }

    private static void applyBuff(Character player, Monster monster) {
        List<MonsterStatus> alreadyBuffed = monster.getAlreadyBuffed();
        if (!alreadyBuffed.contains(MonsterStatus.FREEZE)) {
            // 给新生的怪附加一个定身技能
            var skill = SkillFactory.getSkill(ILWizard.COLD_BEAM);
            StatEffect effect = skill.getEffect(skill.getMaxLevel());
            MonsterStatusEffect mse = new MonsterStatusEffect(
                    Map.of(MonsterStatus.FREEZE, effect.getX()),
                    skill, null, false
            );
            monster.applyStatus(player, mse, false, (long) effect.getDuration() * 100);
            monster.resetMobPosition(vacPosition);
        }
    }

    public synchronized static void stop() {
        running = false; // 设置标志位为 false，通知线程停止
        vacPosition = null;
        vacMap.setVacPoint(null); // 清除地图上的吸怪点

        // 清空怪物
        vacMap.resetMapObjects();

        if (t != null) {
            // 如果线程正在sleep，中断它以立即响应flag变化
            t.interrupt();
            try {
                t.join(); // 等待线程结束
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
            }
        }
    }
}

