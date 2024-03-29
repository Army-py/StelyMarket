package fr.army.stelymarket.utils.manager.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.Buyer;
import fr.army.stelymarket.utils.MarketArea;
import fr.army.stelymarket.utils.MarketSign;


public class SQLiteManager extends DatabaseManager {
    private Connection connection;
    private StelyMarketPlugin plugin;
    private YamlConfiguration config;
    private String filename;

    public SQLiteManager(StelyMarketPlugin plugin) {
        super(plugin);
        this.config = plugin.getConfig();
        this.plugin = plugin;
        
        this.filename = this.config.getString("sqlite.file");
    }

    @Override
    public boolean isConnected() {
        return this.connection == null ? false : true;
    }

    @Override
    public void init() throws ClassNotFoundException, SQLException{
        if(!isConnected()){
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+plugin.getDataFolder().getAbsolutePath()+"/"+this.filename);
        }
    }

    @Override
    public void disconnect() {
        if(isConnected()){
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }


    @Override
    public void createTables(){
        if (isConnected()){
            try {
                PreparedStatement queryCreatePlayer = connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'player' ('playerId' INTEGER, 'playerName' TEXT, 'startDate' INTEGER, 'endDate' INTEGER, 'marketId' INTEGER, FOREIGN KEY('marketId') REFERENCES 'market'('marketId'), PRIMARY KEY('playerId' AUTOINCREMENT));");
                PreparedStatement queryCreateMarket = connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'market' ('marketId' INTEGER, 'price' REAL, 'world' TEXT, PRIMARY KEY('marketId' AUTOINCREMENT));");
                PreparedStatement queryCreateSign = connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'sign' ('signId' INTEGER, 'x' INTEGER, 'y' INTEGER, 'z' INTEGER, 'marketId' INTEGER, FOREIGN KEY('marketId') REFERENCES 'market'('marketId'), PRIMARY KEY('signId' AUTOINCREMENT));");

                queryCreatePlayer.executeUpdate();
                queryCreateMarket.executeUpdate();
                queryCreateSign.executeUpdate();

                queryCreatePlayer.close();
                queryCreateMarket.close();
                queryCreateSign.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void insertMarket(int marketId, double price, String worldName){
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("INSERT INTO market VALUES (?, ?, ?);");
                query.setInt(1, marketId);
                query.setDouble(2, price);
                query.setString(3, worldName);
                query.executeUpdate();
                query.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void removeMarket(int marketId){
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("DELETE FROM market WHERE marketId = ?;");
                query.setInt(1, marketId);
                query.executeUpdate();
                query.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getLastMarketId() {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT marketId FROM market ORDER BY marketId DESC LIMIT 1;");
                ResultSet result = query.executeQuery();
                int marketId = 0;
                if(result.next()){
                    marketId = result.getInt("marketId");
                }
                query.close();
                return marketId;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public void updateMarket(int marketId, int price) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("UPDATE market SET price = ? WHERE marketId = ?;");
                query.setInt(1, price);
                query.setInt(2, marketId);
                query.executeUpdate();
                query.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public double getMarketPrice(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT price FROM market WHERE marketId = ?;");
                query.setInt(1, marketId);
                ResultSet result = query.executeQuery();
                double price = 0;
                if(result.next()){
                    price = result.getDouble("price");
                }
                query.close();
                return price;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public MarketArea getMarketArea(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM market WHERE marketId = ?;");
                query.setInt(1, marketId);
                ResultSet result = query.executeQuery();
                MarketArea marketArea = null;
                if(result.next()){
                    marketArea = new MarketArea(
                        Bukkit.getWorld(result.getString("world")),
                        marketId,
                        result.getInt("price")
                    );
                }
                query.close();
                return marketArea;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public MarketArea getMarketArea(String playerName) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM market m INNER JOIN player p ON p.marketId = m.marketId WHERE p.playerName = ?;");
                query.setString(1, playerName);
                ResultSet result = query.executeQuery();
                MarketArea marketArea = null;
                if(result.next()){
                    marketArea = new MarketArea(
                        Bukkit.getWorld(result.getString("world")),
                        result.getInt("price")
                    );
                }
                query.close();
                return marketArea;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void insertSign(int marketId, int x, int y, int z) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("INSERT INTO sign (x, y, z, marketId) VALUES (?, ?, ?, ?);");
                query.setInt(1, x);
                query.setInt(2, y);
                query.setInt(3, z);
                query.setInt(4, marketId);
                query.executeUpdate();
                query.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeSign(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("DELETE FROM sign WHERE marketId = ?;");
                query.setInt(1, marketId);
                query.executeUpdate();
                query.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Integer[] getSignCoords(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM sign WHERE marketId = ?;");
                query.setInt(1, marketId);
                ResultSet result = query.executeQuery();
                Integer[] coords = new Integer[3];
                if(result.next()){
                    coords[0] = result.getInt("x");
                    coords[1] = result.getInt("y");
                    coords[2] = result.getInt("z");
                }
                query.close();
                return coords;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public MarketArea getMarketArea(Integer signId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM market m INNER JOIN sign s ON s.marketId = m.marketId WHERE s.signId = ?;");
                query.setInt(1, signId);
                ResultSet result = query.executeQuery();
                MarketArea marketArea = null;
                if(result.next()){
                    marketArea = new MarketArea(
                        Bukkit.getWorld(result.getString("world")),
                        result.getInt("price")
                    );
                }
                query.close();
                return marketArea;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public MarketSign getSign(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM sign WHERE marketId = ?;");
                query.setInt(1, marketId);
                ResultSet result = query.executeQuery();
                MarketSign marketSign = null;
                if(result.next()){
                    marketSign = new MarketSign(
                        result.getInt("x"),
                        result.getInt("y"),
                        result.getInt("z"),
                        MarketArea.get(marketId)
                    );
                }
                query.close();
                return marketSign;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public MarketSign getSign(int x, int y, int z) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM sign WHERE x = ? AND y = ? AND z = ?;");
                query.setInt(1, x);
                query.setInt(2, y);
                query.setInt(3, z);
                ResultSet result = query.executeQuery();
                MarketSign marketSign = null;
                if(result.next()){
                    marketSign = new MarketSign(
                        result.getInt("x"),
                        result.getInt("y"),
                        result.getInt("z"),
                        MarketArea.get(result.getInt("marketId"))
                    );
                }
                query.close();
                return marketSign;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void insertPlayer(String playerName, Calendar startDate, Calendar endDate, int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("INSERT INTO player (playerName, startDate, endDate, marketId) VALUES (?, ?, ?, ?);");
                query.setString(1, playerName);
                query.setDate(2, new Date(startDate.getTime().getTime()));
                query.setDate(3, new Date(endDate.getTime().getTime()));
                query.setInt(4, marketId);
                query.executeUpdate();
                query.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removePlayer(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("DELETE FROM player WHERE marketId = ?;");
                query.setInt(1, marketId);
                query.executeUpdate();
                query.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Buyer getPlayer(String playerName) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM player WHERE playerName = ?;");
                query.setString(1, playerName);
                ResultSet result = query.executeQuery();
                Buyer buyer = null;
                if(result.next()){
                    buyer = new Buyer(
                        result.getString("playerName"),
                        result.getDate("startDate"),
                        result.getDate("endDate"),
                        MarketArea.get(result.getInt("marketId"))
                    );
                }
                query.close();
                return buyer;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ArrayList<Integer[]> getSignCoords() {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM sign;");
                ResultSet result = query.executeQuery();
                ArrayList<Integer[]> coords = new ArrayList<>();
                while(result.next()){
                    Integer[] coord = new Integer[3];
                    coord[0] = result.getInt("x");
                    coord[1] = result.getInt("y");
                    coord[2] = result.getInt("z");
                    coords.add(coord);
                }
                query.close();
                return coords;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ArrayList<MarketArea> getExpiredMarkets() {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT m.marketId, m.price, m.world FROM market m INNER JOIN player p ON m.marketId = p.marketId WHERE p.endDate < ?;");
                query.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
                ResultSet result = query.executeQuery();
                ArrayList<MarketArea> markets = new ArrayList<>();
                while(result.next()){
                    markets.add(
                        new MarketArea(
                            Bukkit.getWorld(result.getString("world")),
                            result.getInt("marketId"),
                            result.getInt("price")
                        )
                    );
                }
                query.close();
                return markets;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ArrayList<MarketArea> getMarketAreas() {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM market;");
                ResultSet result = query.executeQuery();
                ArrayList<MarketArea> markets = new ArrayList<>();
                while(result.next()){
                    markets.add(
                        new MarketArea(
                            Bukkit.getWorld(result.getString("world")),
                            result.getInt("marketId"),
                            result.getInt("price")
                        )
                    );
                }
                query.close();
                return markets;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ArrayList<Buyer> getAlertedPlayers() {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT p.playerName, p.startDate, p.endDate FROM market m INNER JOIN player p ON m.marketId = p.marketId WHERE p.endDate < ?;");
                query.setDate(1, new Date(this.getAlertsDates().getTime().getTime()));
                ResultSet result = query.executeQuery();
                ArrayList<Buyer> buyers = new ArrayList<>();
                while(result.next()){
                    buyers.add(
                        new Buyer(
                            result.getString("playerName"),
                            result.getDate("startDate"),
                            result.getDate("endDate"),
                            MarketArea.get(result.getInt("marketId"))
                        )
                    );
                }
                query.close();
                return buyers;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean buyerMustBeAlerted(String playerName) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM player WHERE playerName = ? AND endDate < ?;");
                query.setString(1, playerName);
                query.setDate(2, new Date(this.getAlertsDates().getTime().getTime()));
                ResultSet result = query.executeQuery();
                boolean mustBeAlerted = false;
                if(result.next()){
                    mustBeAlerted = true;
                }
                query.close();
                return mustBeAlerted;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Buyer getPlayer(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT * FROM player WHERE marketId = ?;");
                query.setInt(1, marketId);
                ResultSet result = query.executeQuery();
                Buyer buyer = null;
                if(result.next()){
                    buyer = new Buyer(
                        result.getString("playerName"),
                        result.getDate("startDate"),
                        result.getDate("endDate"),
                        MarketArea.get(result.getInt("marketId"))
                    );
                }
                query.close();
                return buyer;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}