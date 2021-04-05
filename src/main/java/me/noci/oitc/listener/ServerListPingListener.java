package me.noci.oitc.listener;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerListPingListener extends OITCListener {

    public ServerListPingListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleServerListPing(ServerListPingEvent event) {
        String motd = game.getMapName();
        event.setMaxPlayers(game.getMaxPlayers());
        if(isState(StateManager.MAP_CONFIG_STATE)) {
            event.setMaxPlayers(-1);
            motd = "Configure Map";
        }
        event.setMotd(motd);
    }

}
