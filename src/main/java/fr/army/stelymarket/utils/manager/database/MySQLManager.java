package fr.army.stelymarket.utils.manager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.army.stelymarket.StelyMarketPlugin;


public class MySQLManager extends DatabaseManager {
    
    private String host;
    private String database;
    private String user;
    private String password;

    private Connection connection;
    private YamlConfiguration config;

    public MySQLManager(StelyMarketPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.host = this.config.getString("sql.host");
        this.database = this.config.getString("sql.database");
        this.user = this.config.getString("sql.user");
        this.password = this.config.getString("sql.password");
    }

    @Override
    public boolean isConnected() {
        return this.connection == null ? false : true;
    }

    @Override
    public void init() throws ClassNotFoundException, SQLException{
        if(!isConnected()){
            this.connection = DriverManager.getConnection("jdbc:mysql://"+this.host+"/"+this.database, this.user, this.password);
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
                PreparedStatement queryCreatePlayer = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player (playerId INTEGER AUTO_INCREMENT, playerName VARCHAR(255), startDate VARCHAR(255), endDate VARCHAR(255), marketId INTEGER, PRIMARY KEY (playerId));");
                PreparedStatement queryCreateMarket = connection.prepareStatement("CREATE TABLE IF NOT EXISTS market (marketId INTEGER AUTO_INCREMENT, price INTEGER, PRIMARY KEY(marketId));");

                queryCreatePlayer.executeUpdate();
                queryCreateMarket.executeUpdate();

                queryCreatePlayer.close();
                queryCreateMarket.close();


                PreparedStatement queryAlterPlayers = connection.prepareStatement("ALTER TABLE player ADD FOREIGN KEY (marketId) REFERENCES market(marketId);");

                queryAlterPlayers.executeUpdate();

                queryAlterPlayers.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void insertMarket(int price){
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("INSERT INTO market (price) VALUES (?);");
                query.setInt(1, price);
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
    public Integer getLastMarketId() {
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
    public int getMarketPrice(int marketId) {
        if (isConnected()){
            try {
                PreparedStatement query = connection.prepareStatement("SELECT price FROM market WHERE marketId = ?;");
                query.setInt(1, marketId);
                ResultSet result = query.executeQuery();
                int price = 0;
                if(result.next()){
                    price = result.getInt("price");
                }
                query.close();
                return price;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }
}