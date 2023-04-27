package fr.army.stelymarket.utils;

import java.text.NumberFormat;
import java.util.Locale;

import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;

public class MarketArea {

    private DatabaseManager databaseManager = StelyMarketPlugin.getPlugin().getDatabaseManager();
    
    private final int marketId;
    private final Integer price;
    private final String regionId;

    private ProtectedRegion region;


    public MarketArea() {
        this.price = StelyMarketPlugin.getPlugin().getConfig().getInt("default_price");
        this.marketId = databaseManager.getLastMarketId()+1;
        this.regionId = "market_" + IntegerToString(this.marketId);
    }

    public MarketArea(int price) {
        this.price = price;
        this.marketId = databaseManager.getLastMarketId()+1;
        this.regionId = "market_" + IntegerToString(this.marketId);
    }

    public MarketArea(int marketId, int price) {
        this.marketId = marketId;
        this.price = price;
        this.regionId = "market_" + IntegerToString(marketId);
    }


    public int getMarketId() {
        return marketId;
    }

    public int getPrice() {
        return price;
    }

    public ProtectedRegion getRegion() {
        return region;
    }

    public void setRegion(ProtectedRegion region) {
        this.region = region;
    }

    public void create(Region region){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(region.getWorld());
        regions.addRegion(
            new ProtectedCuboidRegion(
                this.regionId,
                region.getMinimumPoint(),
                region.getMaximumPoint()
            )
        );

        // if (get(marketId) == null){
        databaseManager.insertMarket(this.marketId, this.price);
        // }else{
        //     databaseManager.insertMarket(databaseManager.getLastMarketId()+1, this.price);
        // }
    }

    public void remove(World world){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);
        regions.removeRegion(this.regionId);
        
        databaseManager.removeMarket(getIntFromText(this.regionId));
    }
    
    public static MarketArea get(int marketId){
        return StelyMarketPlugin.getPlugin().getDatabaseManager().getMarketArea(marketId);
    }
    
    private String IntegerToString(Integer value){
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }
    
    private Integer getIntFromText(String text){
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }
}
