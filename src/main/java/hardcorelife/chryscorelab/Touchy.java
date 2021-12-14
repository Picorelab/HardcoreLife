package hardcorelife.chryscorelab;

import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import org.apache.commons.io.FileUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import hardcorelife.chryscorelab.commands.Lives;
import hardcorelife.chryscorelab.commands.ResetServer;
import hardcorelife.chryscorelab.commands.SetLives;
import hardcorelife.chryscorelab.helpers.PlayerLife;
import hardcorelife.chryscorelab.listeners.HardcorePermadeath;
import hardcorelife.chryscorelab.listeners.PlayerDeath;
import hardcorelife.chryscorelab.listeners.PlayerJoinServer;
import hardcorelife.chryscorelab.listeners.WorldLoad;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public final class Touchy extends JavaPlugin {

    private static Touchy instance;
    private static boolean reset_on_unload = false;
    private static Server server;
    private static FileConfiguration config;
    private static FileConfiguration livesConfig;

    @Override
    public void onEnable() {
        instance = this;
        server = this.getServer();

        // Plugin startup logic
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        // Initialize config files
        livesConfig = getLivesConfig();
        config = getConfig();

        Objects.requireNonNull(getCommand("lives")).setExecutor(new Lives());
        Objects.requireNonNull(getCommand("setlives")).setExecutor(new SetLives());
        Objects.requireNonNull(getCommand("resetserver")).setExecutor(new ResetServer());

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new PlayerJoinServer(), this);
        pm.registerEvents(new WorldLoad(), this);
        pm.registerEvents(new HardcorePermadeath(), this);

        // Setup bStats
        new Metrics(this, 12723);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerLife.saveLivesData();
        if (reset_on_unload) {
            deleteWorldData();
        }
        instance = null;
    }

    public static Touchy get() {
        return instance;
    }

    public void resetServer() {
        // Deletes all worlds on the server and resets life counts
        Logger logger = getServer().getLogger();

        // Kick all online players
        TextComponent message = Component.text("The server is resetting. Please rejoin.");
        for (Player p : getServer().getOnlinePlayers()) {
            p.kick(message);
        }

        // Delete existing lives.yml
        getLivesConfigFile().delete();
        // Clear playerlife cache
        PlayerLife.clearLivesData();

        logger.info("Restarting the server..");
        reset_on_unload = true;
        getServer().spigot().restart();
    }

    private void deleteWorldData() {
        Logger logger = getServer().getLogger();
        logger.info("Deleting all world data");

        for (World world : server.getWorlds()) {
            String worldName = world.getName();
            logger.info("Deleting: " + world.getName());

            try {
                FileUtils.deleteDirectory(world.getWorldFolder());
            } catch (Exception e) {
                logger.severe("Failed to delete world: " + worldName);
                logger.severe(e.getMessage());
            }
        }
    }

    private FileConfiguration getLivesConfig() {
        File LivesConfigFile = getLivesConfigFile();
        FileConfiguration LivesConfig = new YamlConfiguration();
        try {
            LivesConfig.load(LivesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return LivesConfig;
    }

    private void setLivesConfig(FileConfiguration LivesConfig) {
        File LivesConfigFile = getLivesConfigFile();
        try {
            LivesConfig.save(LivesConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getLivesConfigFile() {
        // Ensures lives.yml exists, and returns the file object
        File LivesConfigFile = new File(getDataFolder(), "lives.yml");
        if (!LivesConfigFile.exists()) {
            LivesConfigFile.getParentFile().mkdirs();
            saveResource("lives.yml", false);
        }
        return LivesConfigFile;
    }

    public int getDefaultLives() {
        // Returns the starting_lives value from config.yml
        return config.getInt("starting_lives");
    }

    public boolean globalLivesEnabled() {
        // Returns the global_lives value from config.yml
        return config.getBoolean("global_lives");
    }

    public boolean deathMovementEnabled() {
        // Returns the death_movement value from config.yml
        if (globalLivesEnabled()){
            return false;
        }
        else{
            return config.getBoolean("death_movement");
        }

    }

    public double getLifeScalingValue() {
        // Returns the server_life_scaling value from config.yml
        return config.getDouble("server_life_scaling");
    }

    public int getPlayerLivesConfig(UUID uuid) {
        // Gets the number of lives a player has from config.yml
        // If no data is set, the default is returned
        return livesConfig.getInt(uuid + ".lives", getDefaultLives());
    }

    public void savePlayerLifeConfig(UUID uuid, int lives) {
        // Update the life value for a single player, in lives.yml
        livesConfig.set(uuid + ".lives", lives);
        setLivesConfig(livesConfig);
    }

    public void saveHashmapData(HashMap<UUID, Integer> lives) {
        // Invoked during plugin shutdown, saves in-memory config to disk
        for (Map.Entry<UUID, Integer> entry : lives.entrySet()) {
            livesConfig.set(entry.getKey() + ".lives", entry.getValue());
        }
        setLivesConfig(livesConfig);
    }

    public boolean naturalRegEnabled() {
        // Returns the value of natural_regeneration from config.yml
        return getConfig().getBoolean("natural_regeneration", false);
    }

    public boolean killGainLivesEnabled() {
        // Returns the value of kill_gain_lives from config.yml
        if (globalLivesEnabled()){
            return false;
        }
        else{
            return config.getBoolean("kill_gain_lives");
        }
    }

    public GameMode getGameModePermaDeath(){
        // Returns the value of gamemode_on_death from config.yml
        return GameMode.valueOf(config.getString("gamemode_on_death"));

    }
}
