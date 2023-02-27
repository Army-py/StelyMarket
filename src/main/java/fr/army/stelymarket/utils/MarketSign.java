package fr.army.stelymarket.utils;

import fr.army.stelymarket.StelyMarketPlugin;

public class MarketSign {
    
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

    public void saveSign(){
        StelyMarketPlugin.getPlugin().getDatabaseManager().insertSign(market.getMarketId(), x, y, z);
    }

    public static MarketSign get(int marketId){
        return StelyMarketPlugin.getPlugin().getDatabaseManager().getSign(marketId);
    }

    public static MarketSign get(int x, int y, int z){
        return StelyMarketPlugin.getPlugin().getDatabaseManager().getSign(x, y, z);
    }
}
