package fr.army.stelymarket.commands.subCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.SubCommand;
import fr.army.stelymarket.utils.MarketArea;
import fr.army.stelymarket.utils.manager.MessageManager;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;

public class SubCmdRemove extends SubCommand {

    DatabaseManager databaseManager = plugin.getDatabaseManager();

    public SubCmdRemove(StelyMarketPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2){
            String regionId = args[1];
            MarketArea marketArea = MarketArea.get(getIntInString(regionId));
            if (marketArea != null){
                marketArea.remove();
                // sender.sendMessage("Market supprimé");
                sender.sendMessage(MessageManager.COMMAND_REMOVE.getMessage());
            }else{
                // sender.sendMessage("Market introuvable");
                sender.sendMessage(MessageManager.COMMAND_REMOVE_NO_MARKET.getMessage());
            }
        }else{
            // sender.sendMessage("Usage: /stelymarket remove <regionId>");
            sender.sendMessage(MessageManager.COMMAND_REMOVE_USAGE.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2){
            if (args[0].equals("remove")){
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
