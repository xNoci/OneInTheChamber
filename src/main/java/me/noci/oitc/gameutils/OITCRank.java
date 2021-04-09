package me.noci.oitc.gameutils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum OITCRank {

    ADMIN('a', "prefix.admin", "§4§lAdmin §8┃ §7", ChatColor.DARK_RED),
    CHAMP('b', "prefix.champ", "§6§lChamp §8┃ §7", ChatColor.GOLD),
    VIP('c', "prefix.vip", "§e§lVIP §8┃ §7", ChatColor.YELLOW),
    PLAYER('d', "§7Spieler §8┃ §7", ChatColor.GRAY),
    SPECTATOR('e', "§8§l", ChatColor.DARK_GRAY);

    private final char priority;
    private final String permission;
    @Getter private final String prefix;
    @Getter private final ChatColor rankColor;

    OITCRank(char priority, String prefix, ChatColor rankColor) {
        this(priority, null, prefix, rankColor);
    }

    OITCRank(char priority, String permission, String prefix, ChatColor rankColor) {
        this.priority = priority;
        this.permission = permission;
        this.prefix = prefix;
        this.rankColor = rankColor;
    }

    public String getTeamName(Player player) {
        String name = priority + player.getUniqueId().toString().replaceAll("-", "");
        if (name.length() > 16) name = name.substring(0, 16);
        return name;
    }

    private boolean hasPermission(Player player) {
        return permission == null || player.hasPermission(permission);
    }

    public static OITCRank getRank(Player player, Game game, boolean allowSpectator) {
        boolean isSpectator = !game.getPlayerSet().contains(player.getUniqueId()) || game.getSpectatorSet().contains(player.getUniqueId());
        if (isSpectator && allowSpectator) return SPECTATOR;
        for (OITCRank value : values()) {
            if (value.hasPermission(player)) return value;
        }
        return PLAYER;
    }

}
