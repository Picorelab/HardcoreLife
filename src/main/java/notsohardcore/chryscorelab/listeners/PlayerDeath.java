package notsohardcore.chryscorelab.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import notsohardcore.chryscorelab.Touchy;
import notsohardcore.chryscorelab.helpers.PlayerLife;

import org.bukkit.Server;

import java.util.Objects;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        PlayerLife playerLife = new PlayerLife(player);
        Location deathLocation = player.getLocation();
        Server.Spigot server = new Server.Spigot();

        PlayerLife.removeLife(player);

        int remainingLives = PlayerLife.getLives(player);

        // TODO - Broadcast remaining lives for individual or server
        final TextComponent component = Component.text("You have " + remainingLives + " live(s) remaining.");
        server.broadcast(component);

        if (remainingLives == 0) {
            // Wait for player to respawn
            Bukkit.getScheduler().scheduleSyncDelayedTask(Touchy.get(), () -> player.spigot().respawn(), 2);

            player.sendMessage("Oh no ! You don't have life anymore ! ");

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
