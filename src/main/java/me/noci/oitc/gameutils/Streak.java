package me.noci.oitc.gameutils;

import me.noci.oitc.OITC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum Streak {

    STREAK_NONE(0, false) {
        @Override
        public void announce(Player player) {
        }
    },
    STREAK_5(5) {
        @Override
        public void announce(Player player) {
            Bukkit.broadcastMessage(String.format("%sDer Spieler §c%s §7hat eine §c5. Streak §7erreicht.", OITC.PREFIX, player.getName()));
        }
    },
    STREAK_10(10) {
        @Override
        public void announce(Player player) {
            Bukkit.broadcastMessage(String.format("%s§cDer Spieler §e%s §chat eine §e10. Streak §cerreicht.", OITC.PREFIX, player.getName()));
        }
    },
    STREAK_15(15) {
        @Override
        public void announce(Player player) {
            Bukkit.broadcastMessage(String.format("%sDer Spieler §c§l%s §r§7hat eine §c§l15. Streak §r§7erreicht.", OITC.PREFIX, player.getName()));
        }
    },
    STREAK_20(20) {
        @Override
        public void announce(Player player) {
            Bukkit.broadcastMessage(String.format("%s§c§lDer Spieler §e§l%s §c§lhat eine §e§l20. Streak §c§lerreicht.", OITC.PREFIX, player.getName()));
        }
    };

    private final int requiredStreak;
    private final boolean announceBreak;

    Streak(int requiredStreak) {
        this(requiredStreak, true);
    }

    Streak(int requiredStreak, boolean announceBreak) {
        this.requiredStreak = requiredStreak;
        this.announceBreak = announceBreak;
    }

    public abstract void announce(Player player);

    public void announceBreak(Player player, Player killer, int streak) {
        if (killer == null) return;
        if (!announceBreak) return;
        Bukkit.broadcastMessage(String.format("%sDie §c%s. Streak §7von §c%s §7wurde durch §c%s §7gebrochen.", OITC.PREFIX, streak, player.getName(), killer.getName()));
    }

    public static Streak get(int streakCount) {
        Streak streak = Streak.STREAK_NONE;
        for (Streak value : values()) {
            if (value.requiredStreak <= streakCount) {
                streak = value;
            }
        }
        return streak;
    }

    public static void announce(Player player, int streakCount) {
        for (Streak streak : values()) {
            if (streak.requiredStreak == streakCount) {
                streak.announce(player);
            }
        }
    }

}
