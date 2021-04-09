package me.noci.oitc.listener.endingphase;

import com.google.common.collect.Sets;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.events.PlayerSendChatMessageEvent;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndPlayerSendChatMessageListener extends OITCListener {

    private static final Pattern PATTERN_REPLACE_GG = Pattern.compile("\\b(?i)GG\\b");
    private static final Pattern PATTERN_STARTS_WITH_GG = Pattern.compile("(?<=^)\\b(?i)GG\\b");
    private static final String GG_REPLACEMENT = "§c§kHH§f§l gg §c§kHH§7";

    private final Set<UUID> wroteGG = Sets.newHashSet();

    public EndPlayerSendChatMessageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerSendChatMessage(PlayerSendChatMessageEvent event) {
        if (!isState(StateManager.ENDING_STATE)) return;
        Player player = event.getPlayer();
        event.setMessage(handleGGMessage(event.getMessage(), NocAPI.getUser(player)));
    }

    private String handleGGMessage(String message, User user) {
        Matcher ggReplacement = PATTERN_REPLACE_GG.matcher(message);
        Matcher startsGG = PATTERN_STARTS_WITH_GG.matcher(message);
        String formattedMessage = message;

        if (user.hasPermission("customGG")) {
            formattedMessage = ggReplacement.replaceAll(GG_REPLACEMENT);
        }

        if (startsGG.matches() && !wroteGG.contains(user.getUUID())) {
            wroteGG.add(user.getUUID());
            user.playSound(Sound.NOTE_PLING, 3, 2);
        }

        return formattedMessage;
    }

}
