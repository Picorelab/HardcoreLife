package hardcorelife.chryscorelab.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.io.Console;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import hardcorelife.chryscorelab.Touchy;
import hardcorelife.chryscorelab.helpers.PlayerLife;

public class PlayerDeath implements Listener {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        PlayerLife.removeLife(player);

        int remainingLives = PlayerLife.getLives(player);

        if (touchy.globalLivesEnabled()) {
            TextComponent component = Component.text("The server has " + remainingLives + " live(s) remaining.");
            server.broadcast(component);

            if (remainingLives == 0) {
                // Handle server permadeath
                server.setDefaultGameMode(GameMode.SPECTATOR);

                // Change everyone's gamemode to spectator
                for (Player p : server.getOnlinePlayers()) {
                    p.setGameMode(GameMode.SPECTATOR);
                }

                // Make resetserver permission global
                PluginManager pm = server.getPluginManager();
                Permission resetserver = pm.getPermission("hardcorelife.resetserver");
                resetserver.setDefault(PermissionDefault.TRUE);

                TextComponent reset_comp = Component
                        .text("Game over! When ready, use the '/resetserver' command to restart.");
                server.broadcast(reset_comp);
            }

        } else {
            player.sendMessage("You have " + remainingLives + " live(s) remaining.");

            if (remainingLives == 0) {
                // handle player permadeath
                TextComponent component = Component.text(player.getName() + " has run out of lives.");
                server.broadcast(component);
                player.setGameMode(GameMode.SPECTATOR);
                // Revive the player. Allows teleport to work
                player.setHealth(20);
                player.teleport(deathLocation);

                //FIXME: *probably not* if player is in spectator mode with death movement = true, and return this to false, the player in spectator mode, can always move
                // Prevent movement on death
                if (touchy.deathMovementEnabled() == false) {
                    //server.getConsoleSender().sendMessage("[Hardcorelife] Preventing movement on death.");
                    //server.getConsoleSender().sendMessage("[Hardcorelife]" + touchy.deathMovementEnabled());
                    player.setFlySpeed(0);
                    player.setWalkSpeed(0);
                } 
                /*else { //TODO - just debug thing, should not be used in production
                    server.getConsoleSender().sendMessage("[Hardcorelife] Movement on death is disabled.");
                    server.getConsoleSender().sendMessage("[Hardcorelife]" + touchy.deathMovementEnabled());
                    player.setFlySpeed(0.1f);
                    player.setWalkSpeed(0.2f);
                } */
            }

        }

    }
}
