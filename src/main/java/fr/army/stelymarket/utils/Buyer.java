package fr.army.stelymarket.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.army.stelymarket.StelyMarketPlugin;


public class Buyer {
    
    private final String name;
    
    private MarketArea market;
    private UUID uuid;
    private String startDate;
    private String endDate;

    public Buyer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;

        this.market = StelyMarketPlugin.getPlugin().getDatabaseManager().getMarketArea(name);
    }

    public Buyer(String name, String startDate, String endDate, MarketArea market) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.market = market;
    }

    public void buyMarket(MarketArea market) {
        if (hasAMarket()) return;
        this.market = market;
        this.startDate = StelyMarketPlugin.getPlugin().getTodayDate();
        this.endDate = StelyMarketPlugin.getPlugin().getDateEndOfMonth();
        StelyMarketPlugin.getPlugin().getEconomyManager().removeMoneyPlayer(asPlayer(), market.getPrice());
        StelyMarketPlugin.getPlugin().getDatabaseManager().insertPlayer(name, startDate, endDate, market.getMarketId());
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public MarketArea getMarket() {
        return market;
    }

    public Player asPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean hasAMarket() {
        return market != null;
    }
}
