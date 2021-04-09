package me.noci.oitc.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ProtectionListener implements Listener {

    @EventHandler
    public void handleFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleAchievementAwarded(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleProjectileHit(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    @EventHandler
    public void handleBowSh0ot(EntityShootBowEvent event) {
        event.getProjectile().setVelocity(event.getProjectile().getVelocity()); //ARROW VISUAL FIX
    }

    @EventHandler
    public void handlePlayerRightClickBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        switch (block.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
            case ENDER_CHEST:
            case ENCHANTMENT_TABLE:
            case WORKBENCH:
            case ANVIL:
            case BREWING_STAND:
            case FURNACE:
            case HOPPER:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void handleVehicleEnterEvent(VehicleEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerInteractWithMinecart(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Minecart)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerInteractWithItemFrame(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (event.getReason().equals(EntityTargetEvent.TargetReason.CUSTOM) || event.getReason().equals(EntityTargetEvent.TargetReason.UNKNOWN))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void handleMobSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void handleHangingBreak(HangingBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleHangingBreakByEntity(HangingBreakByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleHangingPlace(HangingPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handleItemMerge(ItemMergeEvent event) {
        event.setCancelled(true);
    }

}
