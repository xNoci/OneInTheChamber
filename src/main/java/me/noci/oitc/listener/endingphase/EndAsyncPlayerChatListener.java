package me.noci.oitc.listener.endingphase;

import com.google.common.collect.Sets;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.player.permission.PermissionGroup;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndAsyncPlayerChatListener extends OITCListener {

    private static final Pattern PATTERN_REPLACE_GG = Pattern.compile("\\b(?i)GG\\b");
    private static final Pattern PATTERN_STARTS_WITH_GG = Pattern.compile("(?<=^)\\b(?i)GG\\b");
    private static final String GG_REPLACEMENT = "§c§kHH§f§l gg §c§kHH§7";

    private final Set<UUID> wroteGG = Sets.newHashSet();

    public EndAsyncPlayerChatListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!isState(StateManager.ENDING_STATE)) return;
        Player player = event.getPlayer();
        PermissionGroup permissionGroup = CloudServer.getInstance().getCachedPlayer(player.getUniqueId()).getPermissionEntity().getHighestPermissionGroup(CloudAPI.getInstance().getPermissionPool());
        String message = event.getMessage();
        message = message.replace("%", "%%");
        if (player.hasPermission("coloredChat")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        message = handleGGMessage(message, NocAPI.getUser(player));

        String display = ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay());
        String format = String.format("%s%s§8: §7%s", display, player.getName(), message);
        event.setFormat(format);
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
