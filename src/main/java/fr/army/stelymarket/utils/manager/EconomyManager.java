package fr.army.stelymarket.utils.manager;

import java.text.NumberFormat;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import fr.army.stelymarket.StelyMarketPlugin;
import net.milkbowl.vault.economy.Economy;


public class EconomyManager {
    private Economy economy = null;
    // private MessageManager messageManager;

    public EconomyManager(StelyMarketPlugin plugin){
        setupEconomy();
        // this.messageManager = new MessageManager(plugin);
    }

    public boolean checkMoneyPlayer(Player player, Double money) {
        return economy.getBalance(player) >= ((double) money);
    }

    public void removeMoneyPlayer(Player player, double money) {
        economy.withdrawPlayer(player, money);
        // player.sendRawMessage(messageManager.getReplaceMessage("payments.paid", DoubleToString(money)));
        player.sendMessage("§aVous avez payé §6" + DoubleToString(money) + "§a$");
    }

    public boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economy != null); 
	}

    private String DoubleToString(double value){
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }
}
