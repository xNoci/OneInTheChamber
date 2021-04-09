package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.OITC;
import me.noci.oitc.state.LobbyState;
import me.noci.oitc.state.StateManager;
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
            player.sendMessage(String.format("%s§cDieser Command funktioniert nur während der Lobby-Phase.", OITC.PREFIX));
            return;
        }

        LobbyState lobbyState = (LobbyState) stateManager.getCurrentState();
        if (lobbyState.forceStart()) {
            player.sendMessage(String.format("%sDu hast die Runde gestartet.", OITC.PREFIX));
        } else {
            player.sendMessage(String.format("%s§cDie Runde wurde bereits gestartet.", OITC.PREFIX));
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage("§cThis command is only available for a player.");
    }
}
