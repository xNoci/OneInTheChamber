package me.noci.oitc.listener;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import me.noci.oitc.events.PlayerSendChatMessageEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PermissionGroup permissionGroup = CloudServer.getInstance().getCachedPlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
        String message = event.getMessage();
        message = message.replace("%", "%%");
        if (player.hasPermission("coloredChat")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        PlayerSendChatMessageEvent chatEvent = new PlayerSendChatMessageEvent(player, message, event.getFormat(), event.isCancelled());

        String display = ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay());
        String format = String.format("%s%s§8: §7%s", display, player.getName(), chatEvent.getMessage());
        event.setFormat(format);
        event.setCancelled(chatEvent.isCancelled());
    }

}
