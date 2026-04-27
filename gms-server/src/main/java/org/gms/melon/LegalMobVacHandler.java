package org.gms.melon;

import org.gms.client.Character;
import org.gms.net.server.PlayerStorage;
import org.gms.server.life.Monster;
import org.gms.server.maps.MapleMap;

import java.awt.*;

public class LegalMobVacHandler {

    public static volatile int mapId;
    public static Thread t;
    private static volatile boolean running; // 标志位，用于控制线程运行状态

    private static volatile Point vacPosition = null;
    private static volatile MapleMap vacMap = null;
    private static volatile long startTime = 0;

    public synchronized static void mobVac(Character player) {
        if (running) {
            player.dropMessage(String.format(
                    "官方吸怪功能已经在地图【%s】开启，等待吸怪结束后可再次开启。",
                    vacMap.getMapName()
            ));
            return;
        } else {
            start(player);
            player.dropMessage("吸怪已开启。");
            return;
        }
    }

    public synchronized static void stop() {
        running = false; // 设置标志位为 false，通知线程停止
        if (vacMap != null) {
            vacMap.setVacPoint(null); // 清除地图上的吸怪点
        }
        vacPosition = null; // 清除吸怪位置
    }

    public synchronized static void start(Character player) {
        startTime = System.currentTimeMillis(); // 记录线程启动时间
        vacPosition = player.getPosition();
        vacMap = player.getMap();
        vacMap.setVacPoint(vacPosition);
        running = true; // 启动线程时设置标志位为 true
        t = new Thread(() -> {
            while (running) {
                if (System.currentTimeMillis() - startTime > 3 * 60 * 1000) { // 超过3分钟自动关闭
                    stop(); // 调用 stop 方法停止线程
                    break;
                }

                for (Monster monster : vacMap.getAllMonsters()) {
                    if (!monster.isBoss() && monster.isAlive()) {
                        monster.resetMobPosition(vacPosition);
                    }
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
}
