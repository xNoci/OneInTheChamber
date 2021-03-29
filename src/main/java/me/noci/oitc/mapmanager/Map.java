package me.noci.oitc.mapmanager;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

public class Map {

    @Getter @Setter private String mapName = "Unknown";
    @Getter @Setter private String builderName = "Unknown";

    public Map(String mapName) {
        if (StringUtils.isNotBlank(mapName)) {
            this.mapName = mapName;
        }
    }

}
