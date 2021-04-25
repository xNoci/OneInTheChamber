package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.OITC;
import me.noci.oitc.state.LobbyState;
import me.noci.oitc.state.StateManager;
import net.atophia.atophiaapi.language.LanguageAPI;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandData(name = "forcestart", aliases = {"fs"})
public class ForceStartCommand extends Command {

    private final StateManager stateManager;

    public ForceStartCommand(JavaPlugin plugin, StateManager stateManager) {
        super(plugin);
        this.stateManager = stateManager;
    }

    @Override
    public void onPlayerExecute(Player player, String command, String[] args) {
        if (!player.hasPermission("forcestart")) {
            player.sendMessage(OITC.NO_PERMISSION);
            return;
        }

        if (!stateManager.isState(StateManager.LOBBY_STATE)) {
            LanguageAPI.send(player, "command.available_during.lobby_state");
            return;
        }

        LobbyState lobbyState = (LobbyState) stateManager.getCurrentState();
        if (lobbyState.forceStart()) {
            LanguageAPI.send(player, "command.forcestart.round_started");
        } else {
            LanguageAPI.send(player, "command.forcestart.round_already_started");
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage(LanguageAPI.getFormatted("command.console.only_available_for_player"));
    }
}
