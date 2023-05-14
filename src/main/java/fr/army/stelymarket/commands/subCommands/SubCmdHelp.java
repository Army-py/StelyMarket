package fr.army.stelymarket.commands.subCommands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.SubCommand;
import fr.army.stelymarket.utils.manager.MessageManager;


public class SubCmdHelp extends SubCommand {

    public SubCmdHelp(StelyMarketPlugin plugin){
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // player.sendMessage(String.join("\n", commands));
        player.sendMessage(MessageManager.COMMAND_HELP.getHelpMessage());
        
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
