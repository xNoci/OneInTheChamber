package me.noci.oitc.mapmanager.loader;

import me.noci.oitc.mapmanager.Map;

import java.util.List;

public interface MapLoader {

    boolean saveMap(Map map);

    List<Map> loadMaps();

}
