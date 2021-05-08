package me.noci.oitc.listener.mapconfigphase;

import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.mapmanager.MapConfigPhase;
import me.noci.oitc.mapmanager.MapData;
import me.noci.oitc.state.MapConfigState;
import me.noci.oitc.state.StateManager;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MapPlayerToggleSneakListener extends OITCListener {

    public MapPlayerToggleSneakListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!isState(StateManager.MAP_CONFIG_STATE)) return;
        Player player = event.getPlayer();
        MapConfigState state = (MapConfigState) stateManager.getCurrentState();
        if (!state.isConfigurator(player)) return;
        if (!event.isSneaking()) return;

        if (state.isPhase(MapConfigPhase.SPECTATOR_SPAWN)) {
            Location location = player.getLocation().clone();
            state.getMap().set(MapData.SPECTATOR_SPAWN, location);
            state.switchPhase();

            ArmorStand armorStand = spawnLocationStand(location, String.format("%sSpectator-Spawn", OITC.PREFIX_ACTIONBAR));
            state.addArmorStand(armorStand);
            return;
        }

        if (state.isPhase(MapConfigPhase.PLAYER_SPAWNS)) {
            Location location = player.getLocation().clone();
            state.getMap().addRawPlayerSpawn(location);
            state.getMap().set(MapData.WORLD_NAME, location.getWorld().getName());

            ArmorStand armorStand = spawnLocationStand(location, String.format("%sSpieler-Spawn §8» §c%s", OITC.PREFIX_ACTIONBAR, state.getMap().getList(MapData.PLAYER_SPAWNS, String.class).size()));
            state.addArmorStand(armorStand);
            return;
        }
    }

    private ArmorStand spawnLocationStand(Location location, String name) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(name);
        return armorStand;
    }

}
