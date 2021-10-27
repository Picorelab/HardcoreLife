package hardcorelife.chryscorelab.listeners;

import hardcorelife.chryscorelab.Touchy;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import hardcorelife.chryscorelab.helpers.LifeScaling;
import hardcorelife.chryscorelab.helpers.PlayerLife;

import java.io.IOException;

public class PlayerJoinServer implements Listener {

    private static Touchy touchy = Touchy.get();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {

        Player player = event.getPlayer();

        // Ensure joining players have lives configured
        if (!player.hasPlayedBefore()) {
            PlayerLife.initPlayer(player);
        }

        if (PlayerLife.getLives(player) == 0) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            // Handle when a player's life count was increased while they were away
            if (player.getGameMode() == GameMode.SPECTATOR) {
                PlayerLife.revivePlayer(player);
            }
        }

        // TODO - Show a history of past death events
        if (touchy.globalLivesEnabled()) {
            player.sendMessage(" Welcome back aboard " + player.getName() + "! The server has "
                    + PlayerLife.getLives(player) + " live(s) remaining");
        } else {
            player.sendMessage("Welcome back aboard " + player.getName() + "! You have " + PlayerLife.getLives(player)
                    + " live(s) remaining");
        }

        if (!player.hasPlayedBefore()) {
            LifeScaling.checkLifeScaling();
        }
    }
}
