package fr.army.stelymarket.utils.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.army.stelymarket.StelyMarketPlugin;

public enum MessageManager {
    
    INVALID_COMMAND("invalid_command"),
    CONFIRM_BUY("confirm_buy"),
    MARKET_DELETED_IN("market_deleted_in", "%time%"),
    PLAYER_PAID("player_paid", "%price%"),
    COMMAND_HELP("commands.help.output"),
    COMMAND_CREATE("commands.create.output"),
    COMMAND_CREATE_NO_SELECTION("commands.create.no_selection"),
    COMMAND_REMOVE("commands.remove.output"),
    COMMAND_REMOVE_USAGE("commands.remove.usage"),
    COMMAND_REMOVE_NO_MARKET("commands.remove.no_market"),
    ;


    private final YamlConfiguration messages = StelyMarketPlugin.getPlugin().getMessages();

    private final String path;
    private String target = null;
    private String replacement = null;


    MessageManager(String path) {
        this.path = path;
    }

    MessageManager(String path, String target) {
        this.path = path;
        this.target = target;
    }

    public String getMessage() {
        if (target == null && replacement == null) {
            return getPluginPrefix() + messages.getString(path);
        }else{
            return getPluginPrefix() + messages.getString(path).replace(target, replacement);
        }
    }

    public String getHelpMessage(){
        return String.join("\n", messages.getStringList(path));
    }

    public MessageManager setReplacement(String replacement) {
        this.replacement = replacement;
        return this;
    }

    private String getPluginPrefix(){
        return messages.getString("prefix");
    }
}
