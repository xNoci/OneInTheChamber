package me.noci.oitc.listener.gamephase;

import me.noci.noclib.events.PlayerDamageByPlayerEvent;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class GamePlayerDamageByPlayerListener extends OITCListener {

    public GamePlayerDamageByPlayerListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerDamageByPlayer(PlayerDamageByPlayerEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        if (!(event.isProjectileUsed() || event.getProjectileType() == PlayerDamageByPlayerEvent.ProjectileType.ARROW))
            return;
        event.setDamage(10000);
    }

}
