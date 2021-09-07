package hardcorelife.chryscorelab.listeners;

import hardcorelife.chryscorelab.Touchy;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        }

        // TODO - Show messages about the number of lives remaining server/player,
        // sending to the players (even more, show a history of past events)
        if (touchy.globalLivesEnabled()) {
            player.sendMessage(" Welcome back aboard " + player.getName() + "! The server has "
                    + PlayerLife.getLives(player) + " live(s) remaining");
        } else {
            player.sendMessage("Welcome back aboard " + player.getName() + "! You have " + PlayerLife.getLives(player)
                    + " live(s) remaining");
        }
    }
}
