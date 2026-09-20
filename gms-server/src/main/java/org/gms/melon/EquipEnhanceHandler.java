package org.gms.melon;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gms.client.Character;
import org.gms.client.inventory.Equip;
import org.gms.client.inventory.InventoryType;
import org.gms.client.inventory.manipulator.InventoryManipulator;
import org.gms.constants.inventory.ItemConstants;
import org.gms.server.ItemInformationProvider;
import org.gms.util.I18nUtil;
import org.gms.util.Randomizer;

@Slf4j
public class EquipEnhanceHandler {

    private static final ItemInformationProvider ii = ItemInformationProvider.getInstance();

    public static void enhance(Character player, int maxStar, boolean useProtectScroll) {
        // 获取装备背包第一格的装备
        var item = player.getInventory(InventoryType.EQUIP).getItem((short) 1);
        if (item == null) {
            player.message("请把需要升星的装备放到装备栏第一格");
            return;
        }
        var equip = (Equip) item;

        var equipName = ii.getName(equip.getItemId());
        player.message("开始升星装备 " + equipName + "...");

        int star = getStar(equip);

        if (star >= maxStar) {
            player.message(String.format("装备 %s 已经达到最大星级 %d 星，无法继续升星", equipName, maxStar));
            return;
        }

        if (player.getMeso() < (star + 1) * 10000) {
            player.message(String.format("升星失败，金币不足，升星需要 %d 金币", (star + 1) * 10000));
            return;
        }

        if (player.countItem(4001126) < 1) {
            player.message("升星失败，缺少枫叶材料");
            return;
        }

        if (useProtectScroll && player.countItem(2340000) < 1) {
            player.message("升星失败，缺少祝福卷轴");
            return;
        }

        InventoryManipulator.addById(player.getClient(), 2049000, (short) -1);
        player.gainMeso(-(star + 1) * 10000, false);
        InventoryManipulator.removeById(
                player.getClient(),
                ItemConstants.getInventoryType(4001126),
                4001126,
                1,
                true,
                false
        );
        if (useProtectScroll) {
            InventoryManipulator.removeById(
                    player.getClient(),
                    ItemConstants.getInventoryType(2340000),
                    2340000,
                    1,
                    true,
                    false
            );
        }

        int rand = Randomizer.nextInt(100);
        player.message(String.format("升星幸运值：%d", rand));
        boolean success = rand >= 70;
        // 二分之一的概率成功
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

            double ratio = 0.3;

            // 已存在的属性 提升 ratio%，增加幅度最小为 1
            if (curStr > 0) {
                equip.setStr((short) (curStr + Math.max(3, curStr * ratio)));
            }
            if (curDex > 0) {
                equip.setDex((short) (curDex + Math.max(3, curDex * ratio)));
            }
            if (curInt > 0) {
                equip.setInt((short) (curInt + Math.max(3, curInt * ratio)));
            }
            if (curLuk > 0) {
                equip.setLuk((short) (curLuk + Math.max(3, curLuk * ratio)));
            }
            if (curWatk > 0) {
                equip.setWatk((short) (curWatk + Math.max(3, curWatk * ratio)));
            }
            if (curWdef > 0) {
                equip.setWdef((short) (curWdef + Math.max(3, curWdef * ratio)));
            }
            if (curMatk > 0) {
                equip.setMatk((short) (curMatk + Math.max(3, curMatk * ratio)));
            }
            if (curMdef > 0) {
                equip.setMdef((short) (curMdef + Math.max(3, curMdef * ratio)));
            }
            if (curAcc > 0) {
                equip.setAcc((short) (curAcc + Math.max(3, curAcc * ratio)));
            }
            if (curAvoid > 0) {
                equip.setAvoid((short) (curAvoid + Math.max(3, curAvoid * ratio)));
            }
            if (curSpeed > 0) {
                equip.setSpeed((short) (curSpeed + Math.max(3, curSpeed * ratio)));
            }
            if (curJump > 0) {
                equip.setJump((short) (curJump + Math.max(3, curJump * ratio)));
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
            if (useProtectScroll) {
                player.message("装备 " + equipName + " 升星失败，已被祝福卷轴保护，装备未被销毁");
            } else {
                rand = Randomizer.nextInt(100);
                if (rand >= 70) {
                    player.message("装备 " + equipName + " 升星失败，但幸运地保留了装备");
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
            }
        }
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
