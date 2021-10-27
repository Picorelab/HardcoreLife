package hardcorelife.chryscorelab.helpers;

import java.util.logging.Logger;

import org.bukkit.Server;

import hardcorelife.chryscorelab.Touchy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class LifeScaling {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();
    private static Logger logger = server.getLogger();

    public static void checkLifeScaling() throws UnsupportedOperationException {
        // Checks to see if lifescaling should be triggered

        if (!touchy.globalLivesEnabled()) {
            // Function was called in the wrong context
            return;
        }

        int totalPlayers = server.getOfflinePlayers().length;
        double lifeScaling = touchy.getLifeScalingValue();
        double lastVal = (totalPlayers - 1) * lifeScaling;
        double currentVal = totalPlayers * lifeScaling;

        // If the player-to-scaling ratio has crossed a whole-number threshold
        if (lastVal < (int) currentVal) {
            int delta = (int) currentVal - (int) lastVal;
            for (int i = 0; delta > i; i++) {
                PlayerLife.addServerLife();
            }
            TextComponent updateComp = Component.text(String.valueOf(delta) + " live(s) were added to the server!");
            server.broadcast(updateComp);
        }
    }
}
