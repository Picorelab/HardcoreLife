package hardcorelife.chryscorelab.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        Server server = Touchy.get().getServer();

        PlayerLife.removeLife(player);

        int remainingLives = PlayerLife.getLives(player);

        if (Touchy.get().globalLivesEnabled()) {
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
                PluginManager pm = Touchy.get().getServer().getPluginManager();
                Permission resetserver = pm.getPermission("hardcorelife.resetserver");
                resetserver.setDefault(PermissionDefault.TRUE);

                TextComponent reset_comp = Component
                        .text("Game over! When ready, use the '/resetserver' command to restart.");
                server.broadcast(reset_comp);

            } else {
                respawnPlayer(player);
            }

        } else {
            player.sendMessage("You have " + remainingLives + " live(s) remaining.");

            if (remainingLives == 0) {
                // handle player permadeath
                TextComponent component = Component.text(player.getName() + " has run out of lives.");
                server.broadcast(component);
                player.setGameMode(GameMode.SPECTATOR);
                // Prevent movement on death
                player.setFlySpeed(0);
                player.setWalkSpeed(0);
                player.teleport(deathLocation);

            } else {
                respawnPlayer(player);
            }
        }
    }

    private static void respawnPlayer(Player player) {
        // Handle respawning a new player
        // This may be necessary if hardcore == True
        player.setGameMode(Touchy.get().getServer().getDefaultGameMode());
    }

}
