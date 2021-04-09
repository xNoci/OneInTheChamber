package me.noci.oitc.listener.gamephase;

import me.noci.oitc.events.PlayerSendChatMessageEvent;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class GamePlayerSendChatMessageListener extends OITCListener {

    public GamePlayerSendChatMessageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerSendChatMessage(PlayerSendChatMessageEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        if (game.getPlayerSet().contains(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);

        String format = String.format("§7%s§8: §7%s", event.getPlayer().getName(), event.getMessage());
        game.getSpectatorSet().forEach(uuid -> {
            Player spectator = Bukkit.getPlayer(uuid);
            spectator.sendMessage(format);
        });
    }

}
