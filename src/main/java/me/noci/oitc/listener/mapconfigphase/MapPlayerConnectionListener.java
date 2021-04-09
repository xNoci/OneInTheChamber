package me.noci.oitc.listener.mapconfigphase;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MapPlayerConnectionListener extends OITCListener {

    public MapPlayerConnectionListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (!isState(StateManager.MAP_CONFIG_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());

        user.clearInventoryAndArmor();
        user.removePotionEffects();
        user.getBase().setFoodLevel(20);
        user.getBase().setHealth(user.getBase().getMaxHealth());
        user.getBase().setGameMode(GameMode.ADVENTURE);

        event.setJoinMessage(String.format("%sDer Spieler §c%s §7hat das Spiel betreten.", OITC.PREFIX, user.getName()));
        updateTabList();

        game.teleportToLobby(user.getBase());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (!isState(StateManager.MAP_CONFIG_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());
        event.setQuitMessage(String.format("%sDer Spieler §c%s §7hat das Spiel verlassen.", OITC.PREFIX, user.getName()));
        updateTabList();
    }

}
