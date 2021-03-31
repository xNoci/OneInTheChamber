package me.noci.oitc.mapmanager;

import lombok.Getter;

public enum MapFilePath {

    WORLD_NAME("WorldName"),
    MAP_NAME("MapName"),
    MAP_BUILDER("MapBuilder"),
    SPECTATOR_SPAWN("SpectatorSpawn"),
    PLAYER_SPAWNS("PlayerSpawns");

    @Getter private final String filePath;

    MapFilePath(String filePate) {
        this.filePath = filePate;
    }

}
