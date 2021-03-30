package me.noci.oitc.mapmanager;

import lombok.Getter;
import me.noci.oitc.OITC;

public enum MapConfigPhase {

    CONFIG_START("Start"),
    MAP_NAME("Map Name", player -> player.sendMessage(String.format("%sBitte gebe den §cName §7der Map ein.", OITC.PREFIX))),
    BUILDER_NAME("Builder Name", player -> player.sendMessage(String.format("%sBitte gebe den §cName §7des Builder ein.", OITC.PREFIX))),
    SPECTATOR_SPAWN("Lobby Spawn", player -> player.sendMessage(String.format("%sBitte setzt den §cSpectator-Spawn§7, indem du Sneakst.", OITC.PREFIX)));

    @Getter private final String phaseName;
    @Getter private final MapPhaseInfo phaseInfo;

    MapConfigPhase(String phaseName) {
        this(phaseName, player -> {});
    }

    MapConfigPhase(String phaseName, MapPhaseInfo phaseInfo) {
        this.phaseName = phaseName;
        this.phaseInfo = phaseInfo;
    }

    public static MapConfigPhase getNextPhase(MapConfigPhase phase) {
        int nextPhase = phase.ordinal() + 1;
        MapConfigPhase[] phases = values();
        if (nextPhase >= phases.length) {
            return CONFIG_START;
        }
        return phases[nextPhase];
    }
}
