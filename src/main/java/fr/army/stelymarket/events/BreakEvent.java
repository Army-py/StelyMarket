package fr.army.stelymarket.events;

import java.util.ArrayList;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;


public class BreakEvent implements Listener {
    
    private YamlConfiguration config = StelyMarketPlugin.getPlugin().getConfig();
    private DatabaseManager databaseManager = StelyMarketPlugin.getPlugin().getDatabaseManager();


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!config.getStringList("allowed_signs").contains(event.getBlock().getType().name())) return;

        Sign breakSign = (Sign) event.getBlock().getState();
        ArrayList<Integer []> registeredSignsCoords = databaseManager.getSignCoords();
        for (String line : breakSign.getLines()) {
            if (line.contains(config.getString("linked_sign_prefix"))) {
                for (Integer [] coords : registeredSignsCoords) {
                    if (breakSign.getX() == coords[0] && breakSign.getY() == coords[1] && breakSign.getZ() == coords[2]) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
