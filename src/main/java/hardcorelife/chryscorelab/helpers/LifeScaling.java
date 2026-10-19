package hardcorelife.chryscorelab.helpers;

import java.util.logging.Logger;

import org.bukkit.Server;

import hardcorelife.chryscorelab.Touchy;

public class LifeScaling {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();
    private static Logger logger = server.getLogger();

    public static void checkLifeScaling() throws UnsupportedOperationException {
        // Checks to see if lifescaling should be triggered

        if (!touchy.globalLivesEnabled() || PlayerLife.getServerLives() == 0) {
            // Should not revive a permadeath server
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
            Lang.broadcast("life_scaling.lives_added", "lives", delta);
        }
    }
}
