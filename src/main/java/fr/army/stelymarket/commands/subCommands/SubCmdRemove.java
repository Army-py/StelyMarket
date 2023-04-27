package fr.army.stelymarket.commands.subCommands;

import java.util.List;

import org.bukkit.command.CommandSender;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.SubCommand;

public class SubCmdRemove extends SubCommand {

    public SubCmdRemove(StelyMarketPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onTabComplete'");
    }

    @Override
    public boolean isOpCommand() {
        return true;
    }
    
}
