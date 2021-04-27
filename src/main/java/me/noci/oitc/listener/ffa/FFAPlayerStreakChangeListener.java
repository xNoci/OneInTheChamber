package me.noci.oitc.listener.ffa;

import me.noci.oitc.events.PlayerStreakChangeEvent;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.gameutils.Streak;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class FFAPlayerStreakChangeListener extends OITCListener {

    public FFAPlayerStreakChangeListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerStreakChange(PlayerStreakChangeEvent event) {
        if (!isState(StateManager.FFA_STATE)) return;
        if (event.getNewStreak() > event.getOldStreak()) {
            Streak.announce(event.getPlayer(), event.getNewStreak());
        } else {
            Streak.get(event.getOldStreak()).announceBreak(event.getPlayer(), event.getKiller(), event.getOldStreak());
        }
    }

}
