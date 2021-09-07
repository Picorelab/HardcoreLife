package hardcorelife.chryscorelab.listeners;

import hardcorelife.chryscorelab.Touchy;

import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import java.io.IOException;

public class WorldLoad implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) throws IOException {

        World world = event.getWorld();
        Boolean regenEnabledConfig = Touchy.get().naturalRegEnabled();
        Server server = Touchy.get().getServer();

        if (world.getGameRuleValue(GameRule.NATURAL_REGENERATION) != regenEnabledConfig) {
            world.setGameRule(GameRule.NATURAL_REGENERATION, regenEnabledConfig);
            server.getConsoleSender().sendMessage("[Hardcorelife] Set NATURAL_REGENERATION to "
                    + regenEnabledConfig.toString() + " for world: " + world.getName());
        }
    }
}
