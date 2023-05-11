package fr.army.stelymarket.utils;

import java.sql.Date;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.army.stelymarket.StelyMarketPlugin;


public class Buyer {

    private final StelyMarketPlugin plugin = StelyMarketPlugin.getPlugin();
    
    private final String name;
    
    private MarketArea market;
    private UUID uuid;
    private Calendar startDate;
    private Calendar endDate;

    public Buyer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;

        this.market = StelyMarketPlugin.getPlugin().getDatabaseManager().getMarketArea(name);
    }

    public Buyer(String name, Calendar startDate, Calendar endDate, MarketArea market) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.market = market;
    }

    public Buyer(String name, Date startDate, Date endDate, MarketArea marketArea) {
        this.name = name;
        this.startDate = Calendar.getInstance();
        this.startDate.setTimeInMillis(startDate.getTime());
        this.endDate = Calendar.getInstance();
        this.endDate.setTimeInMillis(endDate.getTime());
        this.market = marketArea;
    }

    public void buyMarket(MarketArea market) {
        if (hasAMarket()) return;
        this.market = market;
        this.startDate = Calendar.getInstance();
        this.endDate = plugin.getDateEndOfMonth();
        StelyMarketPlugin.getPlugin().getEconomyManager().removeMoneyPlayer(asPlayer(), market.getPrice());
        StelyMarketPlugin.getPlugin().getDatabaseManager().insertPlayer(name, startDate, endDate, market.getMarketId());
    }

    public String getName() {
        return name;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
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

    public boolean inConfirmation() {
        return StelyMarketPlugin.getPlugin().getCacheManager().playerHasActionName(name, TemporaryActionNames.BUY_CONFIRMATION);
    }

    public void addConfirmation() {
        StelyMarketPlugin.getPlugin().getCacheManager().addTempAction(name, TemporaryActionNames.BUY_CONFIRMATION);
    }

    public void removeConfirmation() {
        StelyMarketPlugin.getPlugin().getCacheManager().removePlayerActionName(name, TemporaryActionNames.BUY_CONFIRMATION);
    }
}
