package hardcorelife.dafray.listeners;

import hardcorelife.dafray.Touchy;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class PlayerJoinServer implements Listener {


    private Touchy touchy;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {


        Player player = event.getPlayer();

        if(player.hasPlayedBefore()){
            // TODO - prout
            if(player.getGameMode() == GameMode.SPECTATOR){

                player.setFlySpeed(0);
                player.setWalkSpeed(0);

            }else{

                player.setFlySpeed(0.1f);
                player.setWalkSpeed(0.2f);
            }
        }else{
            PlayerLife.lives.put(player.getUniqueId(), 1);
        }


        //TODO - We can't reset a player actually


    }
}
