package fr.army.stelymarket.utils;

import java.text.NumberFormat;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;

public class MarketArea {

    private DatabaseManager databaseManager = StelyMarketPlugin.getPlugin().getDatabaseManager();
    
    private final World world;
    private final int marketId;
    private final Integer price;
    private final String regionId;


    public MarketArea(World world) {
        this.world = world;
        this.price = StelyMarketPlugin.getPlugin().getConfig().getInt("default_price");
        this.marketId = databaseManager.getLastMarketId()+1;
        this.regionId = "market_" + IntegerToString(this.marketId);
    }

    public MarketArea(World world, int price) {
        this.world = world;
        this.price = price;
        this.marketId = databaseManager.getLastMarketId()+1;
        this.regionId = "market_" + IntegerToString(this.marketId);
    }

    public MarketArea(World world, int marketId, int price) {
        this.world = world;
        this.marketId = marketId;
        this.price = price;
        this.regionId = "market_" + IntegerToString(marketId);
    }

    public World getWorld() {
        return world;
    }

    public org.bukkit.World getBukkitWorld() {
        return Bukkit.getWorld(world.getName());
    }

    public int getMarketId() {
        return marketId;
    }

    public int getPrice() {
        return price;
    }

    public String getRegionId() {
        return regionId;
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
        databaseManager.insertMarket(this.marketId, this.price, region.getWorld().getName());
        // }else{
        //     databaseManager.insertMarket(databaseManager.getLastMarketId()+1, this.price);
        // }
    }

    public void remove(){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(new BukkitWorld(Bukkit.getServer().getWorld(world.getName())));
        regions.removeRegion(this.regionId);
        
        MarketSign marketSign = databaseManager.getSign(marketId);
        
        marketSign.removeSign();
        databaseManager.removePlayer(marketId);
        databaseManager.removeMarket(this.marketId);
    }

    public void expired(){        
        MarketSign marketSign = databaseManager.getSign(marketId);
        Sign sign = (Sign) Bukkit.getWorld(world.getName()).getBlockAt(marketSign.getLocation()).getState();
        marketSign.linkedSign(sign);
        databaseManager.removePlayer(marketId);
    }

    public void clearMarket(){
        EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(world));
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(new BukkitWorld(Bukkit.getServer().getWorld(world.getName())));
        if (regions == null) return;

        ProtectedRegion protectedRegion = regions.getRegion(regionId);
        if (protectedRegion == null) return;

        CuboidRegion region = new CuboidRegion(protectedRegion.getMinimumPoint(), protectedRegion.getMaximumPoint());
        try {
            editSession.setBlocks(region, BukkitAdapter.adapt(Material.AIR.createBlockData()));
            editSession.commit();
            editSession.close();
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
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
