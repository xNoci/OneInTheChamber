package me.noci.oitc.listener.lobbyphase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyEntityDamageListener extends OITCListener {

    public LobbyEntityDamageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event) {
        if (!isState(StateManager.LOBBY_STATE)) return;
        event.setCancelled(true);
    }

}
