package org.gms.melon;

import org.gms.client.Character;
import org.gms.client.SkillFactory;
import org.gms.client.inventory.InventoryType;
import org.gms.client.status.MonsterStatus;
import org.gms.client.status.MonsterStatusEffect;
import org.gms.constants.skills.Crusader;

import org.gms.server.ItemInformationProvider;
import org.gms.server.StatEffect;
import org.gms.server.life.Monster;
import org.gms.server.maps.MapleMap;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class MobVacHandler {

    public static volatile Thread mobBuffThread;

    public static volatile Point vacPosition = null;
    public static volatile MapleMap vacMap = null;
    public static volatile Character vacPlayer = null;

    private static final boolean AUTO_SALE = false;

    private static final ItemInformationProvider ii = ItemInformationProvider.getInstance();

    private static MonsterStatusEffect mse;

    public synchronized static void mobVacStart(Character player) {
        MapleMap currentMap = player.getMap();

        // clear last spawn point first
        if (vacMap != null && vacMap != currentMap) {
            vacMap.setVacPoint(null);
            vacMap.killAllMonsters();
        }

        vacMap = currentMap;
        vacPosition = player.getPosition();
        vacMap.setVacPoint(vacPosition);
        vacPlayer = player;

        // 移动所有怪物到新的刷怪点
        for (Monster monster : vacMap.getAllMonsters()) {
            if (!monster.isBoss() && monster.isAlive()) {
                monster.resetMobPosition(vacPosition);
            }
        }

        player.dropMessage("刷怪点已设置为当前位置。");

        startMobBuffThread(player);
    }

    public synchronized static void mobVacStop(Character player) {
        vacPlayer = null;
        vacPosition = null;
        if (vacMap != null) {
            vacMap.setVacPoint(null);
            vacMap = null;
        }

        player.dropMessage("刷怪点已清除。");

        stopMobBuffThread(player);
    }

    public synchronized static void startMobBuffThread(Character player) {
        if (mobBuffThread != null && mobBuffThread.isAlive()) {
            player.message("buff线程已经在运行中。");
            return;
        }

        if (mse == null) {
            // 给新生的怪附加一个眩晕技能
            var skill = SkillFactory.getSkill(Crusader.SHOUT);
            StatEffect effect = skill.getEffect(skill.getMaxLevel());
            mse = new MonsterStatusEffect(
                    Map.of(MonsterStatus.STUN, effect.getX()),
                    skill,
                    null,
                    false
            );
        }

        mobBuffThread = new Thread(() -> {
            player.message("buff线程已启动。");
            int loopCount = 0;
            while (!Thread.currentThread().isInterrupted()) {
                loopCount++;

                if (vacMap != null) {
                    for (Monster monster : vacMap.getAllMonsters()) {
                        if (!monster.isBoss() && monster.isAlive()) {
                            applyBuff(player, monster);
                        }
                    }
                }

                if (AUTO_SALE) {
                    if (loopCount >= 100) {
                        short numFreeSlot = player.getInventory(InventoryType.EQUIP).getNumFreeSlot();
                        if (numFreeSlot < 8) {
                            int mesoGain = player.sellAllItemsFromPosition(ii, InventoryType.EQUIP, (short) 25);
                            player.message("通过【自动卖装备】获得 " + mesoGain / 10000 + "万 金币。");
                        }
                        loopCount = 0;
                    }
                }

                try {
                    Thread.sleep(1000); // 每秒检查一次
                } catch (InterruptedException e) {
                    break;
                }
            }
            player.message("buff线程已停止。");
        });
        mobBuffThread.start();
    }

    public synchronized static void stopMobBuffThread(Character player) {
        if (mobBuffThread != null) {
            // 如果线程正在sleep，中断它以立即响应flag变化
            mobBuffThread.interrupt();
            try {
                mobBuffThread.join(); // 等待线程结束
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                return;
            }
            mobBuffThread = null;
        } else {
            player.message("buff线程未运行。");
        }
    }

    private static void applyBuff(Character player, Monster monster) {
        List<MonsterStatus> alreadyBuffed = monster.getAlreadyBuffed();
        if (!alreadyBuffed.contains(MonsterStatus.STUN)) {
            monster.applyStatus(player, mse, false, 100);
        }
        monster.resetMobPosition(vacPosition);
    }
}
