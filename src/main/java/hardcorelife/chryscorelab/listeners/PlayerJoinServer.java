package hardcorelife.chryscorelab.listeners;

import hardcorelife.chryscorelab.Touchy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import hardcorelife.chryscorelab.helpers.PlayerLife;

import java.io.IOException;

public class PlayerJoinServer implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        // Ensures joining players have lives configured

        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            PlayerLife.initPlayer(player);
        }

        // TODO - Show messages about the number of lives remaining server/player, sending to the players (even more, show a history of past events)
        if(Touchy.globalLivesEnabled){
            player.sendMessage(" Welcome back aboard " + player.getName() + "! The server have " /* +  getServerLives() */ + " remaining");
        }else{
            player.sendMessage("Welcome back aboard " + player.getName() + "! You have " + PlayerLife.getLives(player) + " remaining");
        }


    }
}
