package me.noci.oitc.mapmanager;

import lombok.Getter;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import org.bukkit.entity.Player;

public enum MapConfigPhase {

    CONFIG_START("Start"),
    MAP_NAME("Map Name") {
        @Override
        public void sendInfo(Player player) {
            player.sendMessage(String.format("%sBitte gebe den §cName §7der Map ein.", OITC.PREFIX));
        }
    },
    BUILDER_NAME("Builder Name") {
        @Override
        public void sendInfo(Player player) {
            player.sendMessage(String.format("%sBitte gebe den §cName §7des Builder ein.", OITC.PREFIX));
        }
    },
    SPECTATOR_SPAWN("Spectator Spawn") {
        @Override
        public void sendInfo(Player player) {
            player.sendMessage(String.format("%sBitte setzt den §cSpectator-Spawn§7, indem du Sneakst.", OITC.PREFIX));
        }
    },
    PLAYER_SPAWNS("Player Spawns") {
        @Override
        public void sendInfo(Player player) {
            player.sendMessage("");
            player.sendMessage(String.format("%sBitte setzt die §cSpieler-Spawn§7, indem du Sneakst.", OITC.PREFIX));
            player.sendMessage(String.format("%s§cDu musst mindestens %s Spawns platzieren.", OITC.PREFIX, Game.MIN_PLAYER_SPAWNS));
            player.sendMessage(String.format("%s§aWenn du Fertig bist benutze §c/setupend§7.", OITC.PREFIX));
            player.sendMessage("");
        }
    },
    SAVING("Speichern") {
        @Override
        public void sendInfo(Player player) {
            player.sendMessage(String.format("%s§a§lDie Map wird nun gespeichert...", OITC.PREFIX));
        }
    };

    @Getter private final String phaseName;

    MapConfigPhase(String phaseName) {
        this.phaseName = phaseName;
    }

    public void sendInfo(Player player) {
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
