package me.noci.oitc.listener.mapconfigphase;

import me.noci.oitc.OITC;
import me.noci.oitc.events.PlayerSendChatMessageEvent;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.mapmanager.MapConfigPhase;
import me.noci.oitc.mapmanager.settings.MapData;
import me.noci.oitc.state.MapConfigState;
import me.noci.oitc.state.StateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class MapPlayerSendChatMessageListener extends OITCListener {

    public MapPlayerSendChatMessageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerSendChatMessage(PlayerSendChatMessageEvent event) {
        if (!isState(StateManager.MAP_CONFIG_STATE)) return;
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
            state.getMap().set(MapData.MAP_NAME, args[0]);
            state.switchPhase();
            return;
        }

        if (state.isPhase(MapConfigPhase.BUILDER_NAME)) {
            event.setCancelled(true);
            String message = event.getMessage();

            state.getMap().set(MapData.MAP_BUILDER, message);
            state.switchPhase();
            return;
        }
    }

}
