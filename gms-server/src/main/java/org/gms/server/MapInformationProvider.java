package org.gms.server;

import org.apache.commons.lang3.StringUtils;
import org.gms.provider.Data;
import org.gms.provider.DataProvider;
import org.gms.provider.DataProviderFactory;
import org.gms.provider.wz.WZFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapInformationProvider {

    private static final Logger log = LoggerFactory.getLogger(MapInformationProvider.class);
    private final static MapInformationProvider instance = new MapInformationProvider();

    protected Data mapStringData;
    protected DataProvider stringData;
    protected Map<String, String> mapCache = new HashMap<>();

    public static MapInformationProvider getInstance() {
        return instance;
    }

    private MapInformationProvider() {
        stringData = DataProviderFactory.getDataProvider(WZFiles.STRING);
        mapStringData = stringData.getData("Map.img");
        List<Data> lands = mapStringData.getChildren();
        for (Data land : lands) {
            String landName = land.getName();
            List<Data> maps = land.getChildren();
            for (Data map : maps) {
                String mapId = map.getName();
                List<Data> strings = map.getChildren();
                String mapName = "";
                for (Data stringNode : strings) {
                    String nodeName = stringNode.getAttributeValue("name");
                    if (StringUtils.isNotBlank(nodeName) && nodeName.equals("mapName")) {
                        mapName = stringNode.getAttributeValue("value");
                        break;
                    }
                }
                // System.out.println("加载地图信息: " + landName + " - " + mapId + " - " + mapName);
                if (StringUtils.isNotBlank(mapName) && StringUtils.isNotBlank(mapId)) {
                    mapCache.put(mapName, mapId);
                }
            }
        }
    }

    public List<Map.Entry<String, String>> search(String keyword) {
        return mapCache.entrySet().stream()
                .filter(entry -> entry.getKey().contains(keyword))
                .toList();
    }
}
