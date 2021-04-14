package hardcorelife.dafray;

import hardcorelife.dafray.commands.BuyLives;
import hardcorelife.dafray.commands.GuiShop;
import hardcorelife.dafray.commands.HowManyLives;
import hardcorelife.dafray.listeners.PlayerDeath;
import hardcorelife.dafray.listeners.PlayerJoinServer;
import hardcorelife.dafray.listeners.PlayerLife;

import hardcorelife.dafray.listeners.ShopListeners;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Touchy extends JavaPlugin {

    private static Touchy instance;
    private File LivesConfigFile;
    private FileConfiguration LivesConfig;

    private static Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;

        // Plugin startup logic
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        LivesConfig();

        if (setupEconomy()) {
            getLogger().info("Successfully hooked into vault.");
        } else {
            getLogger().info("Couldn't find vault continuing without it.");
        }

        World world = Bukkit.getWorld("world");


        getCommand("shop").setExecutor(new GuiShop());
        getCommand("buylives").setExecutor(new BuyLives());
        getCommand("howmanylives").setExecutor(new HowManyLives());

        assert world != null;
        if(world.getDifficulty() != Difficulty.HARD){
            Bukkit.getConsoleSender().sendMessage("Difficulty changed to Hard, you forget it");
            world.setDifficulty(Difficulty.HARD);

        }

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new PlayerJoinServer(), this);
        pm.registerEvents(new ShopListeners(), this);

    }


    private World getWorld(){
        return Bukkit.getWorld((String) Objects.requireNonNull(getConfig().get("Worlname")));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveHashmapData();
        instance = null;
    }

    public static Touchy get() {
        return instance;
    }

    private void LivesConfig() {
        LivesConfigFile = new File(getDataFolder(), "lives.yml");
        if (!LivesConfigFile.exists()) {
            LivesConfigFile.getParentFile().mkdirs();
            saveResource("lives.yml", false);
        }

        LivesConfig = new YamlConfiguration();
        try {
            LivesConfig.load(LivesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public File saveLivesConfig(){
        return LivesConfigFile;
    }

    public FileConfiguration getLivesConfig() {
        return LivesConfig;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public Economy getEconomy() {
        return economy;
    }

    private void saveHashmapData() {
        for (Map.Entry<UUID, Integer> entry : PlayerLife.lives.entrySet()) {
            LivesConfig.set(entry.getKey() + ".lives", entry.getValue());
        }
        try {
            LivesConfig.save(LivesConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
