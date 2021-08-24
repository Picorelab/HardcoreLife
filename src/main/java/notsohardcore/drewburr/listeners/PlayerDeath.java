package notsohardcore.drewburr.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import notsohardcore.drewburr.Touchy;

import java.util.Objects;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        PlayerLife playerLife = new PlayerLife(player);
        Location deathLocation = player.getLocation();

        PlayerLife.removeLives(player, 1);

        if (PlayerLife.getLives(player) >= 2) {
            // TODO - You have "numbers" left;
        } else if (PlayerLife.getLives(player) == 1) {
            // TODO - You have 1 left ! Be aware !
            player.sendMessage("You have 1 life left ! Be aware !");
        } else if (PlayerLife.getLives(player) == 0) {

            Bukkit.getScheduler().scheduleSyncDelayedTask(Touchy.get(), () -> player.spigot().respawn(), 2);
            // TODO - Oh no ! You have 0 left !

            player.sendMessage("Oh no ! You don't have life anymore ! ");
            Bukkit.broadcastMessage(player.getName() + " Died, he have 0 lives left !");
            // TODO - Fireworks
            // TODO - Spectator

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Touchy.get(), new Runnable() {
                public void run() {
                    player.sendMessage("You died, spectator mode, press 1");
                }
            }, (3 * 20));

            player.setGameMode(GameMode.SPECTATOR);

            player.setFlySpeed(0);
            player.setWalkSpeed(0);
            player.teleport(deathLocation);

            Bukkit.getConsoleSender().sendMessage(Objects.requireNonNull(event.deathMessage()));
        }

    }

}
