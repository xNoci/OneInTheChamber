package me.noci.oitc.listener.gamephase;

import me.noci.noclib.events.PlayerDamageByPlayerEvent;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class GameSpectatorDamagedPlayerListener extends OITCListener {

    public GameSpectatorDamagedPlayerListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerDamagePlayer(PlayerDamageByPlayerEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        if (!game.getSpectatorSet().contains(event.getPlayer().getUniqueId()) &&
                !game.getSpectatorSet().contains(event.getAttacker().getUniqueId())) return;
        event.setDamage(0);
        event.setCancelled(true);
    }

}
