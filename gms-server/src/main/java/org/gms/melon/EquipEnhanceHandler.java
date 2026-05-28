package org.gms.melon;

import org.gms.client.Character;
import org.gms.client.inventory.Equip;
import org.gms.client.inventory.InventoryType;
import org.gms.server.ItemInformationProvider;

public class EquipEnhanceHandler {

    private static final ItemInformationProvider ii = ItemInformationProvider.getInstance();

    public static void addUpgradeSlot(Character player) {
        // 获取装备背包第一格的装备
        var item = player.getInventory(InventoryType.EQUIP).getItem((short) 1);
        if (item == null) {
            player.message("请把需要开槽的装备放到装备栏第一格");
            return;
        }
        var equip = (Equip) item;
        var equipName = ii.getName(equip.getItemId());

        // 打印装备名字
        System.out.println("equipName: " + equipName);

        var equipLevel = equip.getLevel();

        // 打印装备等级
        System.out.println("equipLevel: " + equipLevel);

        var upgradeSlots = equip.getUpgradeSlots();

        // 打印升级槽位
        System.out.println("upgradeSlots: " + upgradeSlots);
    }
}
