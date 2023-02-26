package fr.army.stelymarket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Objects;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.army.stelymarket.commands.CommandManager;
import fr.army.stelymarket.events.SignEvent;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;

public class StelyMarketPlugin extends JavaPlugin {
    
    private static StelyMarketPlugin plugin;
    private YamlConfiguration config;
    private YamlConfiguration messages;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;


    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();

        this.config = initFile(this.getDataFolder(), "config.yml");
        this.messages = initFile(this.getDataFolder(), "messages.yml");

        try {
            this.databaseManager = DatabaseManager.connect(this);
            this.databaseManager.createTables();
            this.getLogger().info("SQL connectée au plugin !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        this.commandManager = new CommandManager(this);

        getServer().getPluginManager().registerEvents(new SignEvent(this), this);

        getLogger().info("StelyMarketPlugin enabled");
    }


    @Override
    public void onDisable() {
        getLogger().info("StelyMarketPlugin disabled");
    }


    public static StelyMarketPlugin getPlugin() {
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


    public YamlConfiguration getConfig() {
        return config;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
