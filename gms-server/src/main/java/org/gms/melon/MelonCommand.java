package org.gms.melon;

import org.apache.commons.lang3.StringUtils;
import org.gms.client.Character;
import org.gms.client.Client;
import org.gms.client.command.Command;
import org.gms.client.inventory.InventoryType;
import org.gms.constants.inventory.ItemConstants;
import org.gms.server.ItemInformationProvider;
import org.gms.server.MapInformationProvider;
import org.gms.server.life.Monster;
import org.gms.server.life.MonsterDropEntry;
import org.gms.server.life.MonsterInformationProvider;
import org.gms.server.maps.MapleMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    public static class To extends Command {
        MapInformationProvider mapInformationProvider = MapInformationProvider.getInstance();

        {
            setDescription("传送");
        }

        @Override
        public void execute(Client c, String[] params) {
            if (params.length <= 0) {
                c.getPlayer().dropMessage(6, "请输入地图名称");
                return;
            }

            String mapName = params[0];
            if (StringUtils.isBlank(mapName)) {
                c.getPlayer().dropMessage(6, "请输入地图名称");
                return;
            }

            List<Map.Entry<String, String>> results = mapInformationProvider.search(mapName);
            if (results.isEmpty()) {
                c.getPlayer().dropMessage(6, "未找到地图: " + mapName);
                return;
            }

            if (results.size() > 10) {
                c.getPlayer().dropMessage(6, "找到过多地图，请输入更精确的地图名称");
                return;
            }

            if (results.size() == 1) {
                c.getPlayer().changeMap(Integer.parseInt(results.getFirst().getValue()));
                return;
            }

            if (params.length == 2) {
                try {
                    int index = Integer.parseInt(params[1]);
                    Map.Entry<String, String> map = results.get(index);
                    c.getPlayer().changeMap(Integer.parseInt(map.getValue()));
                } catch (Exception e) {
                    c.getPlayer().dropMessage(6, "请输入正确的地图索引");
                }
            }
        }
    }

    public static class RenQi extends Command {
        {
            setDescription("增加人气");
        }

        @Override
        public void execute(Client c, String[] params) {
            if (params.length < 1) {
                c.getPlayer().message("请输入增加的人气值");
                return;
            }

            try {
                int amount = Integer.parseInt(params[0]);
                c.getPlayer().setFame(amount);
                c.getPlayer().message("已增加人气: " + amount);
            } catch (Exception e) {
                c.getPlayer().message("请输入正确的人气值");
            }
        }
    }
}
