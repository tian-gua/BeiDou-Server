package org.gms.melon;

import org.gms.client.Character;
import org.gms.client.Client;
import org.gms.client.command.Command;
import org.gms.client.inventory.InventoryType;
import org.gms.constants.inventory.ItemConstants;
import org.gms.server.ItemInformationProvider;
import org.gms.server.life.Monster;
import org.gms.server.life.MonsterDropEntry;
import org.gms.server.life.MonsterInformationProvider;
import org.gms.server.maps.MapleMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MelonCommand {

    public static ItemInformationProvider itemInformationProvider = ItemInformationProvider.getInstance();
    public static MonsterInformationProvider monsterInformationProvider = MonsterInformationProvider.getInstance();
    public static class MapDrop extends Command {
        {
            setDescription("查询当前地图掉落");
        }

        @Override
        public void execute(Client c, String[] params) {
            Character player = c.getPlayer();
            MapleMap map = player.getMap();
            Set<Integer> monsterIds = new HashSet<>();
            for (Monster allMonster : map.getAllMonsters()) {
                monsterIds.add(allMonster.getId());
            }

            for (Integer monsterId : monsterIds) {
                String monsterName = monsterInformationProvider.getMobNameFromId(monsterId);
                List<MonsterDropEntry> monsterDropEntries = monsterInformationProvider.retrieveDrop(monsterId);
                if (monsterDropEntries.isEmpty()) {
                    continue;
                }
                player.message(String.format("%s 掉落: \n", monsterName));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < monsterDropEntries.size(); i++) {
                    MonsterDropEntry monsterDropEntry = monsterDropEntries.get(i);
                    InventoryType inventoryType = ItemConstants.getInventoryType(monsterDropEntry.itemId);
                    if (inventoryType.isEquip() || inventoryType.getType() == InventoryType.USE.getType()) {
                        sb.append(String.format("[%s]", itemInformationProvider.getName(monsterDropEntry.itemId)));
                        if (i == monsterDropEntries.size() - 1 || (i + 1) % 3 == 0) {
                            player.message(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                }
            }
        }
    }

    public static class MobVac extends Command {
        {
            setDescription("定点吸怪");
        }

        @Override
        public void execute(Client c, String[] params) {
            MobVacHandler.mobVacStart(c.getPlayer());
        }
    }

    public static class MobVacStop extends Command {
        {
            setDescription("定点吸怪停止");
        }

        @Override
        public void execute(Client c, String[] params) {
            MobVacHandler.mobVacStop(c.getPlayer());
        }
    }
}
