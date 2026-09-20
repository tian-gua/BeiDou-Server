package org.gms.melon;

import org.gms.client.Character;
import org.gms.server.ItemInformationProvider;
import org.gms.server.life.AbstractLoadedLife;
import org.gms.server.life.MonsterInformationProvider;
import org.gms.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MelonHelper {

    public static ItemInformationProvider itemInformationProvider = ItemInformationProvider.getInstance();
    public static MonsterInformationProvider monsterInformationProvider = MonsterInformationProvider.getInstance();

    public static boolean isBook(int itemId) {
        return itemId >= 2290000 && itemId <= 2290139;
    }

    public static List<Pair<String, Integer>> currentMapMonster(Character player) {
        Set<Integer> monsterIds = player.getMap().getAllMonsters().stream().map(AbstractLoadedLife::getId).collect(Collectors.toSet());
        if (monsterIds.isEmpty()) {
            return List.of();
        }
        return monsterIds.stream()
                .map(monsterId -> new Pair<>(monsterInformationProvider.getMobNameFromId(monsterId), monsterId))
                .collect(Collectors.toList());
    }

    public static List<Pair<String, Integer>> dropList(int monsterId) {
        return monsterInformationProvider.retrieveDrop(monsterId)
                .stream()
                .map(drop -> new Pair<>(itemInformationProvider.getName(drop.itemId), drop.itemId))
                .collect(Collectors.toList());
    }
}
