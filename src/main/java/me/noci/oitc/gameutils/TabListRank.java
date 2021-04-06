package me.noci.oitc.gameutils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum TabListRank {

    ADMIN('a', "§4Admin §8| §7", ChatColor.DARK_RED),
    USER('b', "§9User §8| §7", ChatColor.GRAY),
    SPECTATOR('c', "§7", ChatColor.GRAY);


    private final char priority;
    @Getter private final String prefix;
    @Getter private final ChatColor rankColor;

    TabListRank(char priority, String prefix, ChatColor rankColor) {
        this.priority = priority;
        this.prefix = prefix;
        this.rankColor = rankColor;
    }

    public String getTeamName(Player player) {
        String name = priority + player.getUniqueId().toString().replaceAll("-", "");
        if (name.length() > 16) name = name.substring(0, 16);
        return name;
    }

    public static TabListRank getData(Player player, Game game, boolean allowSpectator) {
        boolean shouldSpectator = !game.getPlayerSet().contains(player.getUniqueId()) || game.getSpectatorSet().contains(player.getUniqueId());
        if (shouldSpectator && allowSpectator) return SPECTATOR;
        return player.isOp() ? ADMIN : USER;
    }

}
