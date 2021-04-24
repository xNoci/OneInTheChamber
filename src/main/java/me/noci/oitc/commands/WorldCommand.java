package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.OITC;
import me.noci.oitc.state.StateManager;
import net.atophia.atophiaapi.language.LanguageAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandData(name = "world")
public class WorldCommand extends Command {

    private final StateManager stateManager;

    public WorldCommand(JavaPlugin plugin, StateManager stateManager) {
        super(plugin);
        this.stateManager = stateManager;
    }

    @Override
    public void onPlayerExecute(Player player, String command, String[] args) {
        if (!player.hasPermission("mapsetup")) {
            player.sendMessage(OITC.NO_PERMISSION);
            return;
        }

        if (!stateManager.isState(StateManager.LOBBY_STATE)) {
            LanguageAPI.send(player, "command.available_during.lobby_state");
            return;
        }

        if (args.length != 1) {
            LanguageAPI.send(player, "command.world.usage");
            return;
        }

        String worldName = args[0];
        LanguageAPI.send(player, "command.world.import.start", worldName);
        World world = Bukkit.createWorld(WorldCreator.name(worldName));
        player.teleport(world.getSpawnLocation());
        LanguageAPI.send(player, "command.world.import.finished", world.getName());
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage(LanguageAPI.getFormatted("command.console.only_available_for_player"));
    }
}
