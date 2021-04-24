package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.state.LobbyState;
import me.noci.oitc.state.StateManager;
import net.atophia.atophiaapi.language.LanguageAPI;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandData(name = "start")
public class StartCommand extends Command {

    private final StateManager stateManager;
    private final Game game;

    public StartCommand(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin);
        this.stateManager = stateManager;
        this.game = game;
    }

    @Override
    public void onPlayerExecute(Player player, String command, String[] args) {
        if (!player.hasPermission("start")) {
            player.sendMessage(OITC.NO_PERMISSION);
            return;
        }

        if (!stateManager.isState(StateManager.LOBBY_STATE)) {
            LanguageAPI.send(player, "command.available_during.lobby_state");
            return;
        }

        if (game.getPlayerSet().size() < game.getPlayersNeeded()) {
            LanguageAPI.send(player, "command.start.not_enough_player", game.getPlayersNeeded());
            return;
        }

        LobbyState lobbyState = (LobbyState) stateManager.getCurrentState();
        if (lobbyState.forceStart()) {
            LanguageAPI.send(player, "command.start.round_started");
        } else {
            LanguageAPI.send(player, "command.start.round_already_started");
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage(LanguageAPI.getFormatted("command.console.only_available_for_player"));
    }
}
