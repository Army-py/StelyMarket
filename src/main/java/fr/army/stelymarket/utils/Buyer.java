package fr.army.stelymarket.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import fr.army.stelymarket.StelyMarketPlugin;


public class Buyer {
    
    private final String name;
    private final String startDate;
    private final String endDate;
    private final MarketArea market;

    private UUID uuid;

    public Buyer(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;

        this.market = StelyMarketPlugin.getPlugin().getDatabaseManager().getMarketArea(name);
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

    public OfflinePlayer asPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
