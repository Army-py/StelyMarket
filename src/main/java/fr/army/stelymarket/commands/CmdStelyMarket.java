package fr.army.stelymarket.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.subCommands.SubCmdCreate;
import fr.army.stelymarket.commands.subCommands.SubCmdHelp;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;

public class CmdStelyMarket implements CommandExecutor, TabCompleter {

    private StelyMarketPlugin plugin;
    private DatabaseManager sqlManager;
    private Map<String, Object> subCommands;


    public CmdStelyMarket(StelyMarketPlugin plugin) {
        this.plugin = plugin;
        this.sqlManager = plugin.getDatabaseManager();
        this.subCommands = new HashMap<>();
        initSubCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            String playerName = player.getName();
            
            if (args.length == 0){
                return true;
            }else{
                if (subCommands.containsKey(args[0])){
                    SubCommand subCmd = (SubCommand) subCommands.get(args[0]);
                    if (subCmd.execute(player, args)){
                        return true;
                    }
                }else if (sender.isOp()){
                    if (subCommands.containsKey(args[0]) && ((SubCommand) subCommands.get(args[0])).isOpCommand()){
                        SubCommand subCmd = (SubCommand) subCommands.get(args[0]);
                        if (subCmd.execute(player, args)){
                            return true;
                        }
                    }
                }

                player.sendMessage("Commande invalide");
            }
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> result = new ArrayList<>();
            for (String subcommand : subCommands.keySet()) {
                if (subcommand.toLowerCase().toLowerCase().startsWith(args[0])){
                    result.add(subcommand);
                }
            }
            if (sender.isOp()){
                for (String subcommand : subCommands.keySet()) {
                    if (subcommand.toLowerCase().toLowerCase().startsWith(args[0]) && ((SubCommand) subCommands.get(subcommand)).isOpCommand()){
                        result.add(subcommand);
                    }
                }
            }
            return result;
        }


        if (args.length > 1 && subCommands.containsKey(args[0].toLowerCase())) {
            List<String> results = ((SubCommand) subCommands.get(args[0])).onTabComplete(sender, args);
            return results;
        } 
        return null;
    }


    private void initSubCommands(){
        subCommands.put("help", new SubCmdHelp(plugin));
        subCommands.put("create", new SubCmdCreate(plugin));
    }
}
