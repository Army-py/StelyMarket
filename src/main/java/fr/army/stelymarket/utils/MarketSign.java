package fr.army.stelymarket.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import fr.army.stelymarket.StelyMarketPlugin;

public class MarketSign {

    private final StelyMarketPlugin plugin = StelyMarketPlugin.getPlugin();
    
    private final int x;
    private final int y;
    private final int z;
    private final MarketArea market;

    public MarketSign(int x, int y, int z, MarketArea market) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.market = market;
    }

    public void saveSign(){
        plugin.getDatabaseManager().insertSign(market.getMarketId(), x, y, z);
    }

    public void linkedSign(SignChangeEvent event){
        List<String> newContent = plugin.getConfig().getStringList("linked_sign");
        for (int i = 0; i < newContent.size(); i++) {
            newContent.set(i, newContent.get(i)
                .replaceAll("%prefix%", plugin.getConfig().getString("linked_sign_prefix"))
                .replaceAll("%price%", String.valueOf(market.getPrice()))
                .replaceAll("%end%", plugin.dateToString(plugin.getDateEndOfMonth(), "dd/MM")));
        }
        
        for (int i = 0; i < newContent.size(); i++) {
            event.setLine(i, newContent.get(i));
        }
    }


    public void linkedSign(Sign sign){
        List<String> newContent = plugin.getConfig().getStringList("linked_sign");
        for (int i = 0; i < newContent.size(); i++) {
            newContent.set(i, newContent.get(i)
                .replaceAll("%prefix%", plugin.getConfig().getString("linked_sign_prefix"))
                .replaceAll("%price%", String.valueOf(market.getPrice()))
                .replaceAll("%end%", plugin.dateToString(plugin.getDateEndOfMonth(), "dd/MM")));
        }
        
        for (int i = 0; i < newContent.size(); i++) {
            sign.setLine(i, newContent.get(i));
        }
        sign.update();
    }


    public void rentedSign(Sign sign, String playerName){
        List<String> newContent = plugin.getConfig().getStringList("buyed_market_sign");
        for (int i = 0; i < newContent.size(); i++) {
            newContent.set(i, newContent.get(i)
                .replaceAll("%prefix%", plugin.getConfig().getString("linked_sign_prefix"))
                .replaceAll("%price%", String.valueOf(market.getPrice()))
                .replaceAll("%end%", plugin.dateToString(plugin.getDateEndOfMonth(), "dd/MM"))
                .replaceAll("%player%", playerName));
            if (newContent.get(i).length() > 15 + (getPrefixColors(newContent.get(i)).size()) * 2) newContent.set(i, newContent.get(i).substring(0, 15));
        }
        
        for (int i = 0; i < newContent.size(); i++) {
            sign.setLine(i, newContent.get(i));
        }
        sign.update();
    }


    public void removeSign(){
        Sign sign = (Sign) Bukkit.getWorld(market.getWorld().getName()).getBlockAt(x, y, z).getState();
        if (sign.isPlaced()) sign.getBlock().setBlockData(Material.AIR.createBlockData());

        plugin.getDatabaseManager().removeSign(market.getMarketId());
    }


    private ArrayList<String> getPrefixColors(String prefixTeam){
        Pattern pattern = Pattern.compile("[&§°].");
        Matcher matcher = pattern.matcher(prefixTeam);
        ArrayList<String> colors = new ArrayList<>();
        while (matcher.find()) {
            String hex = matcher.group().substring(1);
            colors.add(hex);
        }
        return colors;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public MarketArea getMarket() {
        return market;
    }

    public Location getLocation() {
        return new Location(market.getBukkitWorld(), x, y, z);
    }

    public static MarketSign get(int marketId){
        return StelyMarketPlugin.getPlugin().getDatabaseManager().getSign(marketId);
    }

    public static MarketSign get(int x, int y, int z){
        return StelyMarketPlugin.getPlugin().getDatabaseManager().getSign(x, y, z);
    }
}
