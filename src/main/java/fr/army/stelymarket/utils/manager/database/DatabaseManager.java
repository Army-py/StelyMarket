package fr.army.stelymarket.utils.manager.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.Buyer;
import fr.army.stelymarket.utils.MarketArea;
import fr.army.stelymarket.utils.MarketSign;


public abstract class DatabaseManager {

    protected StelyMarketPlugin plugin;

    public DatabaseManager(StelyMarketPlugin plugin){
        this.plugin = plugin;
    }

    public static DatabaseManager connect(StelyMarketPlugin plugin) throws ClassNotFoundException, SQLException {
        YamlConfiguration config = plugin.getConfig();
        DatabaseManager sqlManager;
        switch (config.getString("database_type")) {
            case "mysql":
                sqlManager = new MySQLManager(plugin);
                break;
            default:
                sqlManager = new SQLiteManager(plugin);
                break;
        }
        sqlManager.init();
        return sqlManager;
    }

    public abstract void init() throws ClassNotFoundException, SQLException;
    
    public abstract boolean isConnected();

    public abstract void disconnect();

    public abstract Connection getConnection();

    public abstract void createTables();

    public abstract void insertMarket(int marketId, int price, String worldName);

    public abstract void removeMarket(int marketId);

    public abstract void updateMarket(int marketId, int price);

    public abstract int getMarketPrice(int marketId);

    public abstract MarketArea getMarketArea(int marketId);

    public abstract MarketArea getMarketArea(String playerName);

    public abstract MarketArea getMarketArea(Integer signId);

    public abstract ArrayList<MarketArea> getMarketAreas();

    public abstract Integer getLastMarketId();

    public abstract void insertSign(int marketId, int x, int y, int z);

    public abstract void removeSign(int marketId);

    public abstract MarketSign getSign(int marketId);

    public abstract MarketSign getSign(int x, int y, int z);

    public abstract Integer[] getSignCoords(int marketId);

    public abstract ArrayList<Integer []> getSignCoords();

    public abstract void insertPlayer(String playerName, Calendar startDate, Calendar endDate, int marketId);

    public abstract void removePlayer(int marketId);

    public abstract Buyer getPlayer(String playerName);

    public abstract ArrayList<MarketArea> getExpiredMarkets();


    protected String getCurrentDate(){
        return new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }
}
