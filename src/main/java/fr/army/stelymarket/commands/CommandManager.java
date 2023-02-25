package fr.army.stelymarket.commands;

import org.bukkit.command.PluginCommand;

import fr.army.stelymarket.StelyMarketPlugin;

public class CommandManager {
    private CmdStelyMarket stelyMarketCommand;

    public CommandManager(StelyMarketPlugin plugin) {
        PluginCommand stelyMarketCmd = plugin.getCommand("stelymarket");

        stelyMarketCommand = new CmdStelyMarket(plugin);
        stelyMarketCmd.setExecutor(stelyMarketCommand);
        stelyMarketCmd.setTabCompleter(stelyMarketCommand);
    }
}
