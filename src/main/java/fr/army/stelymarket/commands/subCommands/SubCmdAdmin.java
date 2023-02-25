package fr.army.stelymarket.commands.subCommands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.SubCommand;


public class SubCmdAdmin extends SubCommand {
    private YamlConfiguration messages;

    public SubCmdAdmin(StelyMarketPlugin plugin) {
        super(plugin);
        // this.messages = plugin.getMessages();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        List<String> commandsOp = messages.getStringList("commands.stelyteam_admin.output");

        player.sendMessage(String.join("\n", commandsOp));

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
