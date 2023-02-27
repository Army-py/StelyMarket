package fr.army.stelymarket.events;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;


import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.MarketArea;
import fr.army.stelymarket.utils.MarketSign;

public class SignEvent implements Listener {

    private StelyMarketPlugin plugin;
    private final YamlConfiguration config = StelyMarketPlugin.getPlugin().getConfig();

    public SignEvent(StelyMarketPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (!event.getLine(0).equalsIgnoreCase(config.getString("sign_prefix"))
                && !isInt(event.getLine(1))) return;

        System.out.println("SignEvent");
        

        Sign sign = (Sign) event.getBlock().getState();

        int marketId = getIntFromText(event.getLine(1));

        MarketArea marketArea = MarketArea.get(marketId);
        if (marketArea == null) return;

        MarketSign marketSign = MarketSign.get(marketId);
        if (marketSign != null) return;

        List<String> newContent = config.getStringList("linked_sign");
        for (int i = 0; i < newContent.size(); i++) {
            newContent.set(i, newContent.get(i)
                .replaceAll("%price%", String.valueOf(marketArea.getPrice()))
                .replaceAll("%end%", plugin.getDateEndOfMonth()));
        }
        
        for (int i = 0; i < newContent.size(); i++) {
            event.setLine(i, newContent.get(i));
        }

        new MarketSign(
            sign.getX(),
            sign.getY(),
            sign.getZ(),
            marketArea
        ).saveSign();
    }


    private int getIntFromText(String text){
        try {
            return Integer.parseInt(text.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return plugin.getConfig().getInt("default_price");
        }
    }

    private boolean isInt(String text){
        if (text.length() < 1) return false;
        try {
            Integer.parseInt(text.replaceAll("[^0-9]", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String IntegerToString(Integer value){
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }
}
