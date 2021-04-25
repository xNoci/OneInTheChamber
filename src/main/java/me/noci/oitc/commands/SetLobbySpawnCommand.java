package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.noclib.utils.LocationUtils;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.state.StateManager;
import net.atophia.atophiaapi.language.LanguageAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandData(name = "setlobbyspawn")
public class SetLobbySpawnCommand extends Command {

    private final StateManager stateManager;
    private final Game game;

    public SetLobbySpawnCommand(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin);
        this.stateManager = stateManager;
        this.game = game;
    }

    @Override
    public void onPlayerExecute(Player player, String command, String[] args) {
        if (!player.hasPermission("lobbyspawn")) {
            player.sendMessage(OITC.NO_PERMISSION);
            return;
        }

        if (!stateManager.isState(StateManager.LOBBY_STATE)) {
            LanguageAPI.send(player, "command.available_during.lobby_state");
            return;
        }


        Location lobbySpawn = player.getLocation().clone();
        game.setLobbySpawn(lobbySpawn);
        getPlugin().getConfig().set("lobbySpawn", LocationUtils.locationToStringSilently(lobbySpawn));
        getPlugin().saveConfig();
        LanguageAPI.send(player, "command.setlobby.success");
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage(LanguageAPI.getFormatted("command.console.only_available_for_player"));
    }
}
