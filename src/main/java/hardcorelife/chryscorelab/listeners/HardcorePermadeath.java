package hardcorelife.chryscorelab.listeners;

import hardcorelife.chryscorelab.Touchy;
import hardcorelife.chryscorelab.helpers.PlayerLife;

import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class HardcorePermadeath implements Listener {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();

    private static Logger logger = server.getLogger();

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) throws IOException {
        // Cancel events where Hardcore mode would cause permadeath
        Player player = event.getPlayer();

        if (server.isHardcore() && PlayerLife.getLives(player) > 0 && event.getNewGameMode() == GameMode.SPECTATOR) {
            // Check if within the spawn radius or near bed, likely due to respawn event.
            // Allows spectator mode to be set outside of spawn location.
            boolean isNearSpawn = player.getLocation().distanceSquared(player.getWorld().getSpawnLocation()) < Math
                    .pow(server.getSpawnRadius(), 2);

            boolean isNearBed = false;
            if (player.getBedSpawnLocation() != null) {
                isNearBed = player.getLocation().distanceSquared(player.getBedSpawnLocation()) < 3;
            }

            if (isNearSpawn || isNearBed) {
                event.setCancelled(true);
                logger.info("Cancelled permadeath event for " + player.getName());
            }
        }
    }
}
