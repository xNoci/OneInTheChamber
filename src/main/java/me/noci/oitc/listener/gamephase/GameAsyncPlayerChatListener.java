package me.noci.oitc.listener.gamephase;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameAsyncPlayerChatListener extends OITCListener {

    public GameAsyncPlayerChatListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        if (game.getPlayerSet().contains(event.getPlayer().getUniqueId())) {
            handlePlayerChat(event);
        } else {
            handleSpectatorChat(event);
        }
    }

    private void handlePlayerChat(AsyncPlayerChatEvent event) {
        PermissionGroup permissionGroup = CloudServer.getInstance().getCachedPlayer(event.getPlayer().getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
        String message = event.getMessage();
        message = message.replace("%", "%%");
        if (event.getPlayer().hasPermission("coloredChat")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        String display = ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay());
        String format = String.format("%s%s§8: §7%s", display, event.getPlayer().getName(), message);
        event.setFormat(format);
    }

    private void handleSpectatorChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        String message = event.getMessage();
        message = message.replace("%", "%%");
        if (event.getPlayer().hasPermission("coloredChat")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        String format = String.format("§7%s§8: §7%s", event.getPlayer().getName(), message);
        game.getSpectatorSet().forEach(uuid -> {
            Player spectator = Bukkit.getPlayer(uuid);
            spectator.sendMessage(format);
        });
    }

}
