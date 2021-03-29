package me.noci.oitc.mapmanager;

import lombok.Getter;
import me.noci.oitc.OITC;

public enum MapConfigPhase {

    MAP_NAME("Map Name", player -> player.sendMessage(String.format("%sBitte gebe den §cName §7der Map ein.", OITC.PREFIX))),
    BUILDER_NAME("Builder Name", player -> player.sendMessage(String.format("%sBitte gebe den §cName §7des Builder ein.", OITC.PREFIX))),
    LOBBY_SPAWN("Lobby Spawn", player -> player.sendMessage(String.format("%sBitte setzt den §cLobby-Spawn§7, indem du Sneakst.", OITC.PREFIX)));

    @Getter private final String phaseName;
    @Getter private final MapPhaseInfo phaseInfo;

    MapConfigPhase(String phaseName, MapPhaseInfo phaseInfo) {
        this.phaseName = phaseName;
        this.phaseInfo = phaseInfo;
    }

}
