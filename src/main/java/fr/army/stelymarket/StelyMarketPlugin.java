package fr.army.stelymarket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.army.stelymarket.commands.CommandManager;
import fr.army.stelymarket.events.BreakEvent;
import fr.army.stelymarket.events.InteractEvent;
import fr.army.stelymarket.events.JoinEvent;
import fr.army.stelymarket.events.SignEvent;
import fr.army.stelymarket.utils.MarketArea;
import fr.army.stelymarket.utils.manager.CacheManager;
import fr.army.stelymarket.utils.manager.EconomyManager;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;

public class StelyMarketPlugin extends JavaPlugin {
    
    private static StelyMarketPlugin plugin;
    private YamlConfiguration config;
    private YamlConfiguration messages;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private EconomyManager economyManager;
    private CacheManager cacheManager;


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
        this.economyManager = new EconomyManager(this);
        this.cacheManager = new CacheManager();

        getServer().getPluginManager().registerEvents(new SignEvent(this), this);
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        getLogger().info("StelyMarketPlugin enabled");


        // Delete expired markets
        ArrayList<MarketArea> markets = plugin.getDatabaseManager().getExpiredMarkets();
        for (MarketArea market : markets) {
            market.expired();
            market.clearMarket();
        }
    }


    @Override
    public void onDisable() {
        this.databaseManager.disconnect();

        getLogger().info("StelyMarketPlugin disabled");
    }


    public static StelyMarketPlugin getPlugin() {
        return plugin;
    }


    public Calendar getDateEndOfMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar;
    }


    public Calendar getAlertsDates(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(
            Calendar.DAY_OF_MONTH, 
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - getConfig().getInt("days_before_reset")
        );
        return calendar;
    }


    public String dateToString(Calendar calendar, String dateFormat){
        return new SimpleDateFormat(dateFormat).format(calendar.getTime());
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

    public YamlConfiguration getMessages() {
        return messages;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
