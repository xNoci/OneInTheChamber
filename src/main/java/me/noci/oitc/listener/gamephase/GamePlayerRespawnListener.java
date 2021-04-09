package me.noci.oitc.listener.gamephase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.state.StateManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.SplittableRandom;

public class GamePlayerRespawnListener extends OITCListener {

    private static final SplittableRandom RANDOM = new SplittableRandom();

    public GamePlayerRespawnListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerRespawn(PlayerRespawnEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        Player player = event.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                Game.giveItems(player);
                Map map = game.getCurrentMap();
                List<Location> spawns = map.getPlayerSpawns();
                Location location = spawns.get(RANDOM.nextInt(spawns.size()));
                player.teleport(location);
            }
        }.runTaskLater(plugin, 1);
    }

}
