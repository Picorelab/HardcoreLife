package notsohardcore.drewburr.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import notsohardcore.drewburr.helpers.PlayerLife;

import java.io.IOException;

public class PlayerJoinServer implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        // Ensures joining players have lives configured

        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            PlayerLife.initPlayer(player);
        }
    }
}
