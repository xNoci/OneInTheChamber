package me.noci.oitc.commands;

import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.OITC;
import me.noci.oitc.state.MapConfigState;
import me.noci.oitc.state.StateManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@CommandData(name = "setupmap")
public class MapSetupCommand extends Command {

    private final StateManager stateManager;

    public MapSetupCommand(JavaPlugin plugin, StateManager stateManager) {
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
            player.sendMessage(String.format("%s§cDieser Command funktioniert nur während der Lobby-Phase.", OITC.PREFIX));
            return;
        }

        if (stateManager.isState(StateManager.MAP_CONFIG_STATE)) {
            player.sendMessage(String.format("%s§cEs wird bereits eine Map konfiguriert.", OITC.PREFIX));
            return;
        }

        stateManager.changeState(StateManager.MAP_CONFIG_STATE);
        MapConfigState state = (MapConfigState) stateManager.getCurrentState();
        player.sendMessage(String.format("%s§aDu kannst nun eine Map einrichten.", OITC.PREFIX));
        player.sendMessage("");

        state.setConfigurator(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                state.switchPhase();
            }
        }.runTaskLaterAsynchronously(getPlugin(), 40);

    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage("§cThis command is only available for a player.");
    }
}
