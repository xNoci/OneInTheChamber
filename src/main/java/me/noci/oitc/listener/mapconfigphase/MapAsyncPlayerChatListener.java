package me.noci.oitc.listener.mapconfigphase;

import me.noci.oitc.OITC;
import me.noci.oitc.mapmanager.MapConfigPhase;
import me.noci.oitc.state.MapConfigState;
import me.noci.oitc.state.StateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MapAsyncPlayerChatListener implements Listener {

    private final StateManager stateManager;

    public MapAsyncPlayerChatListener(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!stateManager.isState(StateManager.MAP_CONFIG_STATE)) return;
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
                player.sendMessage(String.format("%s§cDie Map darf nur 16 Zeichen lang sein.", OITC.PREFIX));
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
