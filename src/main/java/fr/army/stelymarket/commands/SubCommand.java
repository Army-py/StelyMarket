package fr.army.stelymarket.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import fr.army.stelymarket.StelyMarketPlugin;


public abstract class SubCommand {
    protected StelyMarketPlugin plugin;

    public SubCommand(StelyMarketPlugin plugin){
        this.plugin = plugin;
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public abstract boolean isOpCommand();
}
