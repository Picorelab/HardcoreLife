package hardcorelife.chryscorelab.listeners;

import hardcorelife.chryscorelab.Touchy;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import hardcorelife.chryscorelab.helpers.Lang;
import hardcorelife.chryscorelab.helpers.LifeScaling;
import hardcorelife.chryscorelab.helpers.PlayerLife;

import java.io.IOException;

public class PlayerJoinServer implements Listener {

    private static Touchy touchy = Touchy.get();
    private static final long WELCOME_DELAY_TICKS = 20;

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

        // Delay the welcome message so the client has time to send its language setting
        touchy.getServer().getScheduler().runTaskLater(touchy, () -> sendWelcome(player), WELCOME_DELAY_TICKS);

        if (!player.hasPlayedBefore()) {
            LifeScaling.checkLifeScaling();
        }
    }

    private void sendWelcome(Player player) {
        if (!player.isOnline()) {
            return;
        }

        // TODO - Show a history of past death events
        if (touchy.globalLivesEnabled()) {
            // If server lives > 0
            if (PlayerLife.getLives(player) > 0) {
                // Show remaining lives
                player.sendMessage(Lang.get(player, "join.server_remaining", "lives", PlayerLife.getLives(player)));
            } else {
                // Show game over
                player.sendMessage(Lang.get(player, "join.server_game_over"));
            }

        } else {
            player.sendMessage(
                    Lang.get(player, "join.welcome", "player", player.getName(), "lives", PlayerLife.getLives(player)));
        }
    }
}
