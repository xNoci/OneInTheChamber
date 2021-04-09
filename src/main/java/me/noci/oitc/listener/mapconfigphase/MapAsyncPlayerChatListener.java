package me.noci.oitc.listener.mapconfigphase;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.mapmanager.MapConfigPhase;
import me.noci.oitc.state.MapConfigState;
import me.noci.oitc.state.StateManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MapAsyncPlayerChatListener extends OITCListener {

    public MapAsyncPlayerChatListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!isState(StateManager.MAP_CONFIG_STATE)) return;
        PermissionGroup permissionGroup = CloudServer.getInstance().getCachedPlayer(event.getPlayer().getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
        String message = event.getMessage();
        message = message.replace("%", "%%");
        if (event.getPlayer().hasPermission("coloredChat")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        String display = ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay());
        String format = String.format("%s%s§8: §7%s", display, event.getPlayer().getName(), message);
        event.setFormat(format);

        handleSetup(event);
    }

    private void handleSetup(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        MapConfigState state = (MapConfigState) stateManager.getCurrentState();
        if (!state.isConfigurator(player)) return;

        if (state.isPhase(MapConfigPhase.MAP_NAME)) {
            event.setCancelled(true);
            String message = event.getMessage();
            String[] args = message.trim().split(" ");

            if (args.length != 1) {
                player.sendMessage(String.format("%s§cBitte gebe nur ein Wort als Name ein.", OITC.PREFIX));
                return;
            }

            if (args[0].length() > 16) {
                player.sendMessage(String.format("%s§cDer Map-Name darf nur 16 Zeichen lang sein.", OITC.PREFIX));
                return;
            }
            state.getMap().setMapName(args[0]);
            state.switchPhase();
            return;
        }

        if (state.isPhase(MapConfigPhase.BUILDER_NAME)) {
            event.setCancelled(true);
            String message = event.getMessage();

            state.getMap().setBuilderName(message);
            state.switchPhase();
            return;
        }
    }


}
