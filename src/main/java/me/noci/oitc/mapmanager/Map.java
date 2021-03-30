package me.noci.oitc.mapmanager;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;

import java.util.List;

public class Map {

    @Getter @Setter private String mapName = "Unknown";
    @Getter @Setter private String builderName = "Unknown";
    @Getter @Setter private Location spectatorSpawn = null;
    @Getter private final List<Location> playerSpawns = Lists.newArrayList();

    public Map(String mapName) {
        if (StringUtils.isNotBlank(mapName)) {
            this.mapName = mapName;
        }
    }

}
