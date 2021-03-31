package me.noci.oitc.mapmanager;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.noci.oitc.Game;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;

import java.util.List;

public class Map {

    public static final String DEFAULT_MAP_NAME = "Unknown";

    @Getter @Setter private String mapName = DEFAULT_MAP_NAME;
    @Getter @Setter private String builderName = "Unknown";
    @Getter @Setter private String worldName = null;
    @Getter @Setter private Location spectatorSpawn = null;
    @Getter private final List<Location> playerSpawns = Lists.newArrayList();

    protected Map() {
        this(null);
    }

    protected Map(String mapName) {
        if (StringUtils.isNotBlank(mapName)) {
            this.mapName = mapName;
        }
    }

    public boolean isValid() {
        if (mapName.equals(DEFAULT_MAP_NAME)) return false;
        if (spectatorSpawn == null) return false;
        if (worldName == null) return false;
        return playerSpawns.size() >= Game.MIN_PLAYER_SPAWNS;
    }

}
