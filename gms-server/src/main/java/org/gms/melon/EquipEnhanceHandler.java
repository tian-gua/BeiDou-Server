package org.gms.melon;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gms.client.Character;
import org.gms.client.inventory.Equip;
import org.gms.client.inventory.InventoryType;
import org.gms.client.inventory.manipulator.InventoryManipulator;
import org.gms.server.ItemInformationProvider;
import org.gms.util.I18nUtil;
import org.gms.util.Randomizer;

@Slf4j
public class EquipEnhanceHandler {

    private static final ItemInformationProvider ii = ItemInformationProvider.getInstance();

    public static boolean enhance(Character player, int maxStar, boolean useMaple) {
        // 获取装备背包第一格的装备
        var item = player.getInventory(InventoryType.EQUIP).getItem((short) 1);
        if (item == null) {
            player.message("请把需要升星的装备放到装备栏第一格");
            return false;
        }
        var equip = (Equip) item;

        var equipName = ii.getName(equip.getItemId());
        player.message("开始升星装备 " + equipName + "...");

        int star = getStar(equip);

        if (star >= maxStar) {
            player.message(String.format("装备 %s 已经达到最大星级 %d 星，无法继续升星", equipName, maxStar));
            return false;
        }

        // 二分之一的概率成功
        boolean success = Randomizer.nextBoolean();
        if (success) {
            short curStr, curDex, curInt, curLuk, curWatk, curWdef, curMatk, curMdef, curAcc, curAvoid, curSpeed, curJump, curHp, curMp;
            curStr = equip.getStr();
            curDex = equip.getDex();
            curInt = equip.getInt();
            curLuk = equip.getLuk();
            curWatk = equip.getWatk();
            curWdef = equip.getWdef();
            curMatk = equip.getMatk();
            curMdef = equip.getMdef();
            curAcc = equip.getAcc();
            curAvoid = equip.getAvoid();
            curSpeed = equip.getSpeed();
            curJump = equip.getJump();
            curHp = equip.getHp();
            curMp = equip.getMp();

            double ratio = 0.1;

            // random [0-100)，小于 70，ratio = 0.1，大于 70，ratio = 0.2，大于 90，ratio = 0.3
            int rand = Randomizer.nextInt(100);
            if (rand < 70) {
                ratio = 0.1;
                player.message("属性提升幅度为 10%");
            } else if (rand < 90) {
                ratio = 0.2;
                player.message("属性提升幅度为 20%");
            } else {
                ratio = 0.3;
                player.message("属性提升幅度为 30%");
            }

            log.info("升星成功，随机数: {}, 属性提升幅度: {}", rand, ratio);

            // 已存在的属性 提升 ratio%，增加幅度最小为 1
            if (curStr > 0) {
                equip.setStr((short) (curStr + Math.max(1, curStr * ratio)));
            }
            if (curDex > 0) {
                equip.setDex((short) (curDex + Math.max(1, curDex * ratio)));
            }
            if (curInt > 0) {
                equip.setInt((short) (curInt + Math.max(1, curInt * ratio)));
            }
            if (curLuk > 0) {
                equip.setLuk((short) (curLuk + Math.max(1, curLuk * ratio)));
            }
            if (curWatk > 0) {
                equip.setWatk((short) (curWatk + Math.max(1, curWatk * ratio)));
            }
            if (curWdef > 0) {
                equip.setWdef((short) (curWdef + Math.max(1, curWdef * ratio)));
            }
            if (curMatk > 0) {
                equip.setMatk((short) (curMatk + Math.max(1, curMatk * ratio)));
            }
            if (curMdef > 0) {
                equip.setMdef((short) (curMdef + Math.max(1, curMdef * ratio)));
            }
            if (curAcc > 0) {
                equip.setAcc((short) (curAcc + Math.max(1, curAcc * ratio)));
            }
            if (curAvoid > 0) {
                equip.setAvoid((short) (curAvoid + Math.max(1, curAvoid * ratio)));
            }
            if (curSpeed > 0) {
                equip.setSpeed((short) (curSpeed + Math.max(1, curSpeed * ratio)));
            }
            if (curJump > 0) {
                equip.setJump((short) (curJump + Math.max(1, curJump * ratio)));
            }
            // hp 和 mp 提升 10%，增加幅度最小为 50
            if (curHp > 0) {
                equip.setHp((short) (curHp + Math.max(50, curHp * ratio)));
            }
            if (curMp > 0) {
                equip.setMp((short) (curMp + Math.max(50, curMp * ratio)));
            }

            // 提升星级
            equip.setOwner("[" + (star + 1) + "]星");

            var newEquip = (Equip) equip.copy();

            // 移除玩家原有装备
            InventoryManipulator.removeFromSlot(
                    player.getClient(),
                    InventoryType.EQUIP,
                    (short) 1,
                    equip.getQuantity(),
                    false,
                    false
            );

            // 给于玩家新的装备
            if (!InventoryManipulator.checkSpace(player.getClient(), equip.getItemId(), 1, equip.getOwner())) {
                player.message(I18nUtil.getMessage(
                        "AbstractPlayerInteraction.gainEquip.message2",
                        InventoryType.EQUIP.getName()
                ));
            }
            InventoryManipulator.addFromDrop(player.getClient(), newEquip, false);
            player.message("升星成功，装备 " + equipName + " 星级提升至 " + (star + 1) + " 星");
        } else {
            // 如果是 5星以上的装备，升星失败会掉销毁装备
            if (star >= 5) {
                if (useMaple) {
                    player.message("装备 " + equipName + " 升星失败，已被枫叶保护");
                } else {
                    player.message("装备 " + equipName + " 升星失败，已被销毁");
                    InventoryManipulator.removeFromSlot(
                            player.getClient(),
                            InventoryType.EQUIP,
                            (short) 1,
                            equip.getQuantity(),
                            false,
                            false
                    );
                }
            } else {
                player.message("装备 " + equipName + " 升星失败，星级不变");
            }
        }
        return true;
    }

    public static int quickEnhance(Character player, int targetStar, boolean useMaple) {
        // loop enhance until reach target star
        int times = 0;
        while (true) {
            var item = player.getInventory(InventoryType.EQUIP).getItem((short) 1);
            if (item == null) {
                player.message("请把需要升星的装备放到装备栏第一格");
                break;
            }

            var equip = (Equip) item;
            int star = getStar(equip);
            if (star >= targetStar) {
                player.message("装备 " + ii.getName(equip.getItemId()) + " 已经达到目标星级 " + targetStar + " 星，停止升星");
                break;
            }

            boolean success = enhance(player, targetStar, useMaple);
            if (!success) {
                break;
            }

            times++;
        }

        player.message("共尝试升星 " + times + " 次");
        return times;
    }

    private static int getStar(Equip equip) {
        var owner = equip.getOwner();
        int star = -1;
        if (StringUtils.isBlank(owner)) {
            star = 0;
        } else {
            try {
                star = Integer.parseInt(owner.replace("[", "").replace("]星", ""));
            } catch (Exception e) {
                log.error("获取星级失败: {}", owner);
                star = 0;
            }
        }
        return star;
    }
}
