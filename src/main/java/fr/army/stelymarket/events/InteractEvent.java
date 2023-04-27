package fr.army.stelymarket.events;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.Buyer;
import fr.army.stelymarket.utils.MarketArea;
import fr.army.stelymarket.utils.MarketSign;

public class InteractEvent implements Listener {

    private final YamlConfiguration config = StelyMarketPlugin.getPlugin().getConfig();
    
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        // System.out.println(1);
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        // System.out.println(2);
        if (!config.getStringList("allowed_signs").contains(event.getClickedBlock().getType().name())) return;
        
        // System.out.println(3);
        Sign clickedSign = (Sign) event.getClickedBlock().getState();
        // System.out.println(4);
        if (!config.getString("linked_sign_prefix").equals(clickedSign.getLines()[0])) return;
        
        // System.out.println(5);
        Location loc = clickedSign.getLocation();
        MarketSign marketSign = MarketSign.get(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (marketSign == null) return;
        // System.out.println(6);

        
        MarketArea marketArea = marketSign.getMarket();
        Player player = event.getPlayer();
        
        Buyer buyer = new Buyer(player.getName(), player.getUniqueId());
        if (buyer.hasAMarket()) return;

        // System.out.println(7);

        buyer.buyMarket(marketArea);
        marketSign.setSign(clickedSign);
        marketSign.rentedSign(player.getName());
    }
}
