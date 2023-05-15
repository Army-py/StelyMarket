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
import fr.army.stelymarket.utils.manager.MessageManager;

public class InteractEvent implements Listener {

    private final YamlConfiguration config = StelyMarketPlugin.getPlugin().getConfig();
    
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!config.getStringList("allowed_signs").contains(event.getClickedBlock().getType().name())) return;
        
        Sign clickedSign = (Sign) event.getClickedBlock().getState();
        if (!config.getString("linked_sign_prefix").equals(clickedSign.getLines()[0])) return;
        
        Location loc = clickedSign.getLocation();
        MarketSign marketSign = MarketSign.get(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (marketSign == null) return;

        if (Buyer.get(marketSign.getMarket().getMarketId()) != null) return;
        
        Player player = event.getPlayer();
        MarketArea marketArea = marketSign.getMarket();
        
        Buyer buyer = new Buyer(player.getName(), player.getUniqueId());
        if (buyer.hasAMarket()) return;

        if (buyer.inConfirmation()){
            buyer.buyMarket(marketArea);
            marketSign.rentedSign(clickedSign, player.getName());
            marketArea.editRegionOwner(player.getName());
            buyer.removeConfirmation();
        }else{
            buyer.addConfirmation();
            // player.sendMessage("§aCliquer à nouveau sur le panneau pour confirmer l'achat.");
            player.sendMessage(MessageManager.CONFIRM_BUY.getMessage());
        }
    }
}
