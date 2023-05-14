package fr.army.stelymarket.commands.subCommands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.SubCommand;
import fr.army.stelymarket.utils.MarketArea;

public class SubCmdExpired extends SubCommand {

    public SubCmdExpired(StelyMarketPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArrayList<MarketArea> markets = plugin.getDatabaseManager().getExpiredMarkets();

        sender.sendMessage("Nombre de markets expirées : " + markets.size());

        for (MarketArea market : markets) {
            sender.sendMessage("Market : " + market.getMarketId());
            market.remove();
        }

        sender.sendMessage("Markets expirées supprimées");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isOpCommand() {
        return true;
    }
}
