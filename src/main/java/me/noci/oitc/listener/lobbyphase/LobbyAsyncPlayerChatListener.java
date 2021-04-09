package me.noci.oitc.listener.lobbyphase;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyAsyncPlayerChatListener extends OITCListener {

    public LobbyAsyncPlayerChatListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!isState(StateManager.LOBBY_STATE)) return;
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

}
