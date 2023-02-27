package fr.army.stelymarket.utils;

import java.util.List;

import org.bukkit.block.Sign;

import fr.army.stelymarket.StelyMarketPlugin;

public class MarketSign {
    
    private final int x;
    private final int y;
    private final int z;
    private final MarketArea market;

    private Sign sign;

    public MarketSign(int x, int y, int z, MarketArea market) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.market = market;
    }

    public void saveSign(){
        StelyMarketPlugin.getPlugin().getDatabaseManager().insertSign(market.getMarketId(), x, y, z);
    }

    public void rentedSign(String playerName){
        for (int i = 0; i < 4; i++) {
            sign.setLine(i, StelyMarketPlugin.getPlugin().getConfig().getStringList("buyed_market_sign").get(i));
        }

        List<String> newContent = StelyMarketPlugin.getPlugin().getConfig().getStringList("buyed_market_sign");
        for (int i = 0; i < newContent.size(); i++) {
            newContent.set(i, newContent.get(i)
                .replaceAll("%price%", String.valueOf(market.getPrice()))
                .replaceAll("%end%", StelyMarketPlugin.getPlugin().getDateEndOfMonth())
                .replaceAll("%player%", playerName));
        }
        
        for (int i = 0; i < newContent.size(); i++) {
            sign.setLine(i, newContent.get(i));
        }
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

    public Integer[] getCoords() {
        return new Integer[] {(int) x, (int) y, (int) z};
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public static MarketSign get(int marketId){
        return StelyMarketPlugin.getPlugin().getDatabaseManager().getSign(marketId);
    }

    public static MarketSign get(int x, int y, int z){
        return StelyMarketPlugin.getPlugin().getDatabaseManager().getSign(x, y, z);
    }
}
