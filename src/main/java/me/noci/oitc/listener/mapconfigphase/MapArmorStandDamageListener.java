package me.noci.oitc.listener.mapconfigphase;

import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MapArmorStandDamageListener extends OITCListener {

    public MapArmorStandDamageListener(StateManager stateManager) {
        super(stateManager);
    }

    @EventHandler
    public void handleArmorStandDamage(EntityDamageByEntityEvent event) {
        if (!isState(StateManager.MAP_CONFIG_STATE)) return;
        if (!(event.getEntity() instanceof ArmorStand)) return;
        event.setCancelled(true);
    }

}
