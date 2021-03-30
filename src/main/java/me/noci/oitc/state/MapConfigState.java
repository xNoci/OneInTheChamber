package me.noci.oitc.state;

import lombok.Getter;
import lombok.Setter;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.mapmanager.MapConfigPhase;
import org.bukkit.entity.Player;

public class MapConfigState extends State {

    @Setter private Player configurator;
    @Getter private Map map;
    @Getter private MapConfigPhase phase;

    @Override
    void start() {
        this.phase = MapConfigPhase.CONFIG_START;
        map = new Map(null);
    }

    @Override
    void stop() {

    }

    @Override
    void update() {

    }

    @Override
    void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        scoreboard.updateTitle("     §9OITC     ");
        scoreboard.updateLine(0, "");
        scoreboard.updateLine(1, " §7Map einrichten ");
        scoreboard.updateLine(2, "");
        scoreboard.updateLine(3, " §7Map");
        scoreboard.updateLine(4, String.format(" §8» §c%s", map.getMapName()));
        scoreboard.updateLine(5, "");
        scoreboard.updateLine(6, " §7Phase");
        scoreboard.updateLine(7, String.format(" §8» §c%s", phase.getPhaseName()));
        scoreboard.updateLine(8, "");
    }

    public boolean isConfigurator(Player player) {
        return player.getUniqueId().equals(configurator.getUniqueId());
    }

    public boolean isPhase(MapConfigPhase phase) {
        return this.phase != null && this.phase == phase;
    }

    public void switchPhase() {
        this.phase = MapConfigPhase.getNextPhase(phase);
        this.phase.getPhaseInfo().sendInfo(configurator);
    }
}
