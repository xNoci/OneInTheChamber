package me.noci.oitc.listener.mapconfigphase;

import me.noci.oitc.state.StateManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MapArmorStandDamageListener implements Listener {

    private final StateManager stateManager;

    public MapArmorStandDamageListener(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @EventHandler
    public void handleArmorStandDamage(EntityDamageByEntityEvent event) {
        if (!stateManager.isState(StateManager.MAP_CONFIG_STATE)) return;
        if (!(event.getEntity() instanceof ArmorStand)) return;
        event.setCancelled(true);
    }

}
