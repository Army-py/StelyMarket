package fr.army.stelymarket.events;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.MarketSign;

public class InteractEvent implements Listener {

    private final YamlConfiguration config = StelyMarketPlugin.getPlugin().getConfig();
    
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!config.getStringList("allowed_sign").contains(event.getClickedBlock().getType().name())) return;
        
        Sign clickedSign = (Sign) event.getClickedBlock().getState();
        if (!clickedSign.getLines().equals((String[]) config.getStringList("linked_sign").toArray())) return;
        
        Location loc = clickedSign.getLocation();
        MarketSign marketSign = MarketSign.get(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (marketSign == null) return;

        
    }
}
