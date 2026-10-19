package hardcorelife.chryscorelab.listeners;


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
import hardcorelife.chryscorelab.helpers.Lang;
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
            Lang.broadcast("death.server_remaining", "lives", remainingLives);

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

                Lang.broadcast("death.server_game_over");
            }

        } else {
            player.sendMessage(Lang.get(player, "death.player_remaining", "lives", remainingLives));

            if (remainingLives == 0) {
                // handle player permadeath
                Lang.broadcast("death.player_out_of_lives", "player", player.getName());
                player.setGameMode(touchy.getGameModePermaDeath());
                // Revive the player. Allows teleport to work
                player.setHealth(20);
                player.teleport(deathLocation);

                // Prevent movement on death
                if (touchy.deathMovementEnabled() == false) {
                    player.setFlySpeed(0);
                    player.setWalkSpeed(0);
                }

            }
            if (touchy.killGainLivesEnabled()) {
                if (player.getKiller() != null) {
                    Player killer = player.getKiller();
                    PlayerLife.addLife(killer);
                    killer.sendMessage(Lang.get(killer, "death.gained_life"));
                }
            }
        }

    }
}
