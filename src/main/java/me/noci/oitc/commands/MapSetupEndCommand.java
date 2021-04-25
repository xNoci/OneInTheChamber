package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.OITC;
import me.noci.oitc.mapmanager.MapManager;
import me.noci.oitc.state.MapConfigState;
import me.noci.oitc.state.StateManager;
import net.atophia.atophiaapi.language.LanguageAPI;
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
            LanguageAPI.send(player, "command.available_during.map_config_state");
            return;
        }

        MapConfigState state = (MapConfigState) stateManager.getCurrentState();
        if (!state.getMap().isValid()) {
            LanguageAPI.send(player, "command.setupend.map_invalid");
            return;
        }

        if (!state.finishSetup(mapManager)) {
            LanguageAPI.send(player, "command.setupend.unknown_error");
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage(LanguageAPI.getFormatted("command.console.only_available_for_player"));
    }

}