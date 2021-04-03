package me.noci.oitc.state;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.noclib.packtes.utils.WrappedEnumScoreboardTeamAction;
import me.noci.noclib.packtes.utils.WrappedScoreboardTeam;
import me.noci.noclib.packtes.wrapper.server.WrappedServerScoreboardTeam;
import me.noci.oitc.OITC;
import org.bukkit.Sound;

public class LobbyState extends State {

    private int remainingTime;
    private boolean starting;
    private boolean forceStarted;

    @Override
    protected void start() {
        this.remainingTime = game.getTimeToStart();
        this.starting = false;
        this.forceStarted = false;
    }

    @Override
    protected void stop() {
        this.remainingTime = game.getTimeToStart();
        this.starting = false;
        this.forceStarted = false;
        NocAPI.getOnlineUsers().forEach(User::resetLevelValue);
    }

    @Override
    protected void update() {
        if (starting) {
            if (remainingTime <= 0) {
                changeState(StateManager.GAME_STATE);
                return;
            }

            NocAPI.getOnlineUsers().forEach(user -> {
                if (remainingTime % 10 == 0 || remainingTime == 15 || remainingTime <= 5) {
                    user.setLevelValue(remainingTime, game.getTimeToStart(), Sound.NOTE_BASS, 1, 1);
                    sendRemainingTime(user, remainingTime);
                } else {
                    user.setLevelValue(remainingTime, game.getTimeToStart());
                }
            });

            remainingTime--;
        } else {
            NocAPI.getOnlineUsers().forEach(user -> user.setLevelValue(game.getTimeToStart(), game.getTimeToStart()));
        }
    }

    @Override
    protected void updateTabList(User user) {
        for (User player : NocAPI.getOnlineUsers()) {
            String teamName = player.getUUID().toString().replaceAll("-", "");
            if(teamName.length() > 16) teamName = teamName.substring(0, 16);

            WrappedScoreboardTeam team = new WrappedScoreboardTeam(teamName);
            team.setPrefix("§9User §8| §7");
            team.getEntries().add(player.getName());
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.REMOVE_TEAM));
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.CREATE_TEAM));
        }
    }

    @Override
    protected void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        scoreboard.updateTitle("     §9OITC     ");
        scoreboard.updateLine(0, "");
        scoreboard.updateLine(1, " §7Map");
        scoreboard.updateLine(2, String.format("  §8» §c%s", mapName()));
        scoreboard.updateLine(3, "");
    }

    public void checkTimer() {
        boolean enoughPlayer = game.getPlayerSet().size() >= game.getPlayersNeeded();

        if (enoughPlayer && !starting) {
            starting = true;
            this.remainingTime = game.getTimeToStart();
        }

        if (!enoughPlayer && starting) {
            starting = false;
            forceStarted = false;
            this.remainingTime = game.getTimeToStart();
        }
    }

    public boolean forceStart() {
        if (forceStarted) return false;
        if (remainingTime <= game.getForceTime()) return false;
        forceStarted = true;
        starting = true;
        remainingTime = game.getForceTime();
        return true;
    }

    private void sendRemainingTime(User user, int time) {
        if (time != 1) {
            user.sendMessage("%sDas Spiel startet in §c%s §7Sekunden.", OITC.PREFIX, time);
        } else {
            user.sendMessage("%sDas Spiel startet in §c%s §7Sekunde.", OITC.PREFIX, time);
        }
    }

    private String mapName() {
        return game.getCurrentMap() == null ? "Unknown" : game.getCurrentMap().getMapName();
    }

}
