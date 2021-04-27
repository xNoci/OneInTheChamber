package me.noci.oitc.state;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.noclib.packtes.utils.WrappedEnumScoreboardTeamAction;
import me.noci.noclib.packtes.utils.WrappedScoreboardTeam;
import me.noci.noclib.packtes.wrapper.server.WrappedServerScoreboardTeam;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.OITCRank;
import me.noci.oitc.gameutils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FFAState extends State {

    private int timeRemaining = 0;

    @Override
    protected void start() {
        timeRemaining = game.getFfaServerTime();
    }

    @Override
    protected void stop() {

        Bukkit.getServer().shutdown();
    }

    @Override
    protected void update() {
        broadcastMessages();

        if (timeRemaining <= 0) {
            stop();
            return;
        }
        timeRemaining--;
    }

    @Override
    protected void updateTabList(User user) {
        for (User player : NocAPI.getOnlineUsers()) {
            OITCRank rank = OITCRank.getRank(player.getBase(), game, true);
            WrappedScoreboardTeam team = new WrappedScoreboardTeam(rank.getTeamName(player.getBase()));
            team.getEntries().add(player.getName());
            team.setPrefix(rank.getPrefix());

            if (game.getSpectatorSet().contains(player.getUUID())) {
                team.setCanSeeFriendlyInvisibles(true);
            }

            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.REMOVE_TEAM));
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.CREATE_TEAM));
        }
    }

    @Override
    protected void updateScoreboardLines(List<String> lines, User user) {
        PlayerData playerData = game.getPlayerData(user.getBase());
        lines.add("");
        lines.add(String.format(" §7Streak§8: §c%s", playerData.getStreak()));
        lines.add("");
    }

    private void broadcastMessages() {
        int remainingMinutes = ((Long) TimeUnit.SECONDS.toMinutes(timeRemaining)).intValue();
        if (remainingMinutes == 3 || remainingMinutes == 2) {
            Bukkit.broadcastMessage(String.format("%sDer Server stoppt in §c%s §7Minuten.", OITC.PREFIX, remainingMinutes));
            return;
        }
        if (remainingMinutes == 1) {
            Bukkit.broadcastMessage(String.format("%sDer Server stoppt in §c%s §7Minute.", OITC.PREFIX, remainingMinutes));
            return;
        }
        if (timeRemaining == 30 || timeRemaining == 15) {
            Bukkit.broadcastMessage(String.format("%sDer Server stoppt in §c%s §7Sekunden.", OITC.PREFIX, remainingMinutes));
            return;
        }

        if (timeRemaining <= 5) {
            NocAPI.getOnlineUsers().forEach(user -> user.playSound(Sound.NOTE_PLING, 1, 1));
            if (timeRemaining == 1) {
                Bukkit.broadcastMessage(String.format("%sDer Server stoppt in §c%s §7Sekunde.", OITC.PREFIX, remainingMinutes));
                return;
            }
            Bukkit.broadcastMessage(String.format("%sDer Server stoppt in §c%s §7Sekunde.", OITC.PREFIX, remainingMinutes));
            return;
        }

    }
}
