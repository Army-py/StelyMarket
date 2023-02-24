package fr.army.stelymarketarea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Objects;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.army.stelymarketarea.utils.manager.SQLiteManager;

public class StelyMarketAreaPlugin extends JavaPlugin {
    
    private static StelyMarketAreaPlugin plugin;
    private YamlConfiguration config;
    private YamlConfiguration messages;
    private SQLiteManager sqliteManager;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();

        this.config = initFile(this.getDataFolder(), "config.yml");
        this.messages = initFile(this.getDataFolder(), "messages.yml");

        this.sqliteManager = new SQLiteManager(this);

        try {
            this.sqliteManager.connect();
            this.getLogger().info("SQL connectée au plugin !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
        
        getLogger().info("StelyMarketAreaPlugin enabled");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("StelyMarketAreaPlugin disabled");
    }


    public static StelyMarketAreaPlugin getPlugin() {
        return plugin;
    }


    private YamlConfiguration initFile(File dataFolder, String fileName) {
        final File file = new File(dataFolder, fileName);
        if (!file.exists()) {
            try {
                Files.copy(Objects.requireNonNull(getResource(fileName)), file.toPath());
            } catch (IOException ignored) {
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
