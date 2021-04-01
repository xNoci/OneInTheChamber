package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.OITC;
import me.noci.oitc.mapmanager.MapConfigPhase;
import me.noci.oitc.mapmanager.MapManager;
import me.noci.oitc.state.MapConfigState;
import me.noci.oitc.state.StateManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandData(name = "setupend")
public class MapSetupEndCommand extends Command {

    private final StateManager stateManager;
    private final MapManager mapManager;

    public MapSetupEndCommand(JavaPlugin plugin, StateManager stateManager, MapManager mapManager) {
        super(plugin);
        this.stateManager = stateManager;
        this.mapManager = mapManager;
    }

    @Override
    public void onPlayerExecute(Player player, String command, String[] args) {
        if (!player.hasPermission("mapsetup")) {
            player.sendMessage(OITC.NO_PERMISSION);
            return;
        }

        if (!stateManager.isState(StateManager.MAP_CONFIG_STATE)) {
            player.sendMessage(String.format("%s§cDieser Command funktioniert nur während der Map-Configurations Phase.", OITC.PREFIX));
            return;
        }

        MapConfigState state = (MapConfigState) stateManager.getCurrentState();
        if (!state.isPhase(MapConfigPhase.PLAYER_SPAWNS)) {
            player.sendMessage(String.format("%s§cDu musst das Setup erst beenden.", OITC.PREFIX));
            return;
        }

        if (state.getMap().getPlayerSpawns().size() < Game.MIN_PLAYER_SPAWNS) {
            player.sendMessage(String.format("%s§cDu hast nicht genug Spieler-Spawns platziert.", OITC.PREFIX));
            player.sendMessage(String.format("%sSpawns: §c%s§8/§c%s", OITC.PREFIX, state.getMap().getPlayerSpawns().size(), Game.MIN_PLAYER_SPAWNS));
            return;
        }

        if (!state.finishSetup(mapManager)) {
            player.sendMessage(String.format("%s§cEin unerwarteter Fehler ist aufgetreten.", OITC.PREFIX));
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage("§cThis command is only available for a player.");
    }

}