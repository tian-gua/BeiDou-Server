package org.gms.melon;

import org.gms.client.Character;
import org.gms.client.Client;
import org.gms.client.command.Command;
import org.gms.server.ItemInformationProvider;
import org.gms.server.life.Monster;
import org.gms.server.life.MonsterDropEntry;
import org.gms.server.life.MonsterInformationProvider;
import org.gms.server.maps.MapleMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MelonCommand {

    public static class MapDrop extends Command {
        {
            setDescription("查询当前地图掉落");
        }

        @Override
        public void execute(Client c, String[] params) {
            MonsterInformationProvider monsterInformationProvider = MonsterInformationProvider.getInstance();
            ItemInformationProvider itemInformationProvider = ItemInformationProvider.getInstance();
            Character player = c.getPlayer();
            MapleMap map = player.getMap();
            Set<Integer> monsterIds = new HashSet<>();
            for (Monster allMonster : map.getAllMonsters()) {
                monsterIds.add(allMonster.getId());
            }

            for (Integer monsterId : monsterIds) {
                StringBuilder dropInfo = new StringBuilder();
                String monsterName = monsterInformationProvider.getMobNameFromId(monsterId);
                List<MonsterDropEntry> monsterDropEntries = monsterInformationProvider.retrieveDrop(monsterId);
                if (monsterDropEntries.isEmpty()) {
                    continue;
                }
                dropInfo.append(String.format("%s 掉落: \n", monsterName));
                for (MonsterDropEntry monsterDropEntry : monsterDropEntries) {
                    dropInfo.append(String.format(
                            "- %s (1/%d)\n",
                            itemInformationProvider.getName(monsterDropEntry.itemId),
                            monsterDropEntry.chance
                    ));
                }
                player.message(dropInfo.toString());
            }
        }
    }
}
