package org.gms.melon;

import org.gms.client.Character;
import org.gms.server.life.Monster;
import org.gms.server.maps.MapleMap;

import java.awt.*;

public class MobVacHandler {

    public static volatile boolean status;
    public static volatile int mapId;
    public static Thread t;
    private static volatile boolean running; // 标志位，用于控制线程运行状态

    private static volatile Point vacPosition = null;
    private static volatile MapleMap vacMap = null;

    public synchronized static void mobVac(boolean on, Character player) {
        if (on && status) {
            player.dropMessage("吸怪已开启，无需重复开启。");
            return;
        }

        if (!on && !status) {
            player.dropMessage("吸怪已关闭，无需重复关闭。");
            return;
        }

        if (on && !status) {
            status = true;
            start(player);
            player.dropMessage("吸怪已开启。");
            return;
        }

        if (!on && status) {
            status = false;
            stop(); // 调用 stop 方法停止线程
            player.dropMessage("吸怪已关闭。");
        }
    }

    public synchronized static void start(Character player) {
        vacPosition = player.getPosition();
        vacMap = player.getMap();
        running = true; // 启动线程时设置标志位为 true
        t = new Thread(() -> {
            while (running) {
                // 在这里实现线程的主要逻辑
                var allPlayer = vacMap.getAllPlayer();
                if (allPlayer.size() != 1 && allPlayer.getFirst().getObjectId() != player.getObjectId()) {
                    running = false;
                    break; // 如果地图上没有玩家了，或者地图上有玩家但不是当前玩家，则停止线程
                }

                for (Monster monster : vacMap.getAllMonsters()) {
                    vacMap.moveMonster(monster, vacPosition);
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

    public synchronized static void stop() {
        running = false; // 设置标志位为 false，通知线程停止
        if (t != null) {
            try {
                t.join(); // 等待线程结束
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
            }
        }
    }
}
