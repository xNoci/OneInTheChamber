package me.noci.oitc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {

    @EventHandler
    public void handleBowSh0ot(EntityShootBowEvent event) {
        event.getProjectile().setVelocity(event.getProjectile().getVelocity());
    }

}
