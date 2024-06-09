package fr.army.stelymarket.commands.subCommands;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.SubCommand;
import fr.army.stelymarket.utils.MarketArea;
import fr.army.stelymarket.utils.manager.MessageManager;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SubCmdRegen extends SubCommand {

    DatabaseManager databaseManager = plugin.getDatabaseManager();

    public SubCmdRegen(StelyMarketPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2){
            String regionId = args[1];
            MarketArea marketArea = MarketArea.get(getIntInString(regionId));
            if (marketArea != null){
                marketArea.expired();
                marketArea.clearMarket();
                // sender.sendMessage("Market supprimé");
                sender.sendMessage(MessageManager.COMMAND_REGEN.getMessage());
            }else{
                // sender.sendMessage("Market introuvable");
                sender.sendMessage(MessageManager.COMMAND_REGEN_NO_MARKET.getMessage());
            }
        }else{
            // sender.sendMessage("Usage: /stelymarket remove <regionId>");
            sender.sendMessage(MessageManager.COMMAND_REGEN_USAGE.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2){
            if (args[0].equals("regen")){
                List<String> result = new ArrayList<>();
                for (MarketArea marketArea : databaseManager.getMarketAreas()) {
                    String regionId = marketArea.getRegionId();
                    if (regionId.toLowerCase().startsWith(args[1].toLowerCase())){
                        result.add(regionId);
                    }
                }
                return result;
            }
        }
        return null;
    }

    @Override
    public boolean isOpCommand() {
        return true;
    }


    private int getIntInString(String string){
        String result = "";
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))){
                result += string.charAt(i);
            }
        }
        if (result.length() == 0){
            return -1;
        }else{
            return Integer.parseInt(result);
        }
    }
}
