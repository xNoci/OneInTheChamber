package me.noci.oitc.state;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class EndingState extends State {

    @Override
    public void start() {
        NocAPI.getOnlineUsers().forEach(user -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getOnlinePlayers().forEach(other -> other.showPlayer(user.getBase()));
                    user.clearInventoryAndArmor();
                    user.getBase().setFoodLevel(20);
                    user.getBase().setHealth(user.getBase().getMaxHealth());
                    user.getBase().setGameMode(GameMode.SURVIVAL);
                    user.removePotionEffects();
                    user.getBase().spigot().setCollidesWithEntities(true);
                }
            }.runTask(plugin);


        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void update() {

    }

    @Override
    public void updatePlayerScoreboard(Scoreboard scoreboard, User user) {

    }

}
