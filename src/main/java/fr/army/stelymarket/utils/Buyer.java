package fr.army.stelymarket.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;


public class Buyer {
    
    private final String name;
    private final String startDate;
    private final String endDate;

    private UUID uuid;

    public Buyer(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public OfflinePlayer asPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
