package me.noci.oitc.state;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.noclib.packtes.utils.WrappedEnumScoreboardTeamAction;
import me.noci.noclib.packtes.utils.WrappedScoreboardTeam;
import me.noci.noclib.packtes.wrapper.server.WrappedServerScoreboardTeam;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.OITCRank;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.mapmanager.MapConfigPhase;
import me.noci.oitc.mapmanager.MapManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Set;

public class MapConfigState extends State {

    private final Set<ArmorStand> armorStandSet = Sets.newHashSet();
    @Getter private MapConfigPhase phase;
    @Getter private Map map;
    private Player configurator;

    @Override
    protected void start() {
        armorStandSet.clear();
        this.phase = MapConfigPhase.CONFIG_START;
        map = MapManager.createNewMap();
    }

    @Override
    protected void stop() {
        NocAPI.getOnlineUsers().forEach(User::resetLevelValue);
        armorStandSet.forEach(Entity::remove);
        configurator = null;
    }

    @Override
    protected void update() {
        int currentPhase = phase.ordinal();
        int lastPhase = MapConfigPhase.values().length - 1;
        NocAPI.getOnlineUsers().forEach(user -> {
            user.sendActionbar(String.format("%sPhase §a%s§8/§2%s", OITC.PREFIX_ACTIONBAR, currentPhase, lastPhase));
            user.setLevelValue(currentPhase, lastPhase);
        });
    }

    @Override
    protected void updateTabList(User user) {
        for (User player : NocAPI.getOnlineUsers()) {
            OITCRank rank = OITCRank.getData(player.getBase(), game, false);
            WrappedScoreboardTeam team = new WrappedScoreboardTeam(rank.getTeamName(player.getBase()));
            team.getEntries().add(player.getName());
            team.setPrefix(rank.getPrefix());

            if (isConfigurator(player.getBase())) {
                team.setSuffix(" §8| §9Setup");
            }

            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.REMOVE_TEAM));
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.CREATE_TEAM));
        }
    }

    @Override
    protected void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        scoreboard.updateTitle("     §9OITC     ");
        scoreboard.updateLine(0, "");
        scoreboard.updateLine(1, " §6Map einrichten ");
        scoreboard.updateLine(2, "");
        scoreboard.updateLine(3, " §7Configurator ");
        scoreboard.updateLine(4, String.format(" §8» §c%s ", configurator == null ? "Unknown" : configurator.getName()));
        scoreboard.updateLine(5, "");
        scoreboard.updateLine(6, " §7Map");
        scoreboard.updateLine(7, String.format(" §8» §c%s ", map.getMapName()));
        scoreboard.updateLine(8, "");
        scoreboard.updateLine(9, " §7Phase");
        scoreboard.updateLine(10, String.format(" §8» §c%s ", phase.getPhaseName()));
        scoreboard.updateLine(11, "");
    }

    public void setConfigurator(Player configurator) {
        this.configurator = configurator;
        updateTabList();
    }

    public boolean isConfigurator(Player player) {
        return configurator != null && player.getUniqueId().equals(configurator.getUniqueId());
    }

    public boolean isPhase(MapConfigPhase phase) {
        return this.phase != null && this.phase == phase;
    }

    public void switchPhase() {
        NocAPI.getUser(configurator).clearChat();
        this.phase = MapConfigPhase.getNextPhase(phase);
        this.phase.getPhaseInfo().sendInfo(configurator);
        updateScoreboard();
    }

    public void addArmorStand(ArmorStand armorStand) {
        this.armorStandSet.add(armorStand);
    }

    public boolean finishSetup(MapManager mapManager) {
        if (!map.isValid())
            return false;
        switchPhase();
        mapManager.saveMap(map, (savedSuccessfully, reason) -> {
            if (savedSuccessfully) {
                configurator.sendMessage(String.format("%s§aDie Map '%s' wurde erfolgreich gespeichert.", OITC.PREFIX, map.getMapName()));
            } else {
                configurator.sendMessage(String.format("%s§cEin Fehler beim Speichern der Map '%s' ist aufgetreten.", OITC.PREFIX, map.getMapName()));
                configurator.sendMessage(String.format("%s§cGrund§8: §7%s", OITC.PREFIX, reason));
            }

            changeState(StateManager.LOBBY_STATE);
        });
        return true;
    }
}
