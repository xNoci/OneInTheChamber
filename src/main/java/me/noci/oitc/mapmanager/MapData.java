package me.noci.oitc.mapmanager;

import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;

public enum MapData {

    WORLD_NAME(String.class),
    MAP_NAME(String.class),
    MAP_BUILDER(String.class),
    SPECTATOR_SPAWN(Location.class),
    PLAYER_SPAWNS(new ArrayList<String>().getClass());

    @Getter private final Class<?> type;

    MapData(Class<?> type) {
        this.type = type;
    }

}
