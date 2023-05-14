package fr.army.stelymarket.events;

import java.util.Calendar;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.utils.Buyer;
import fr.army.stelymarket.utils.manager.MessageManager;
import fr.army.stelymarket.utils.manager.database.DatabaseManager;

public class JoinEvent implements Listener {

    private final YamlConfiguration config = StelyMarketPlugin.getPlugin().getConfig();
    private final DatabaseManager databaseManager = StelyMarketPlugin.getPlugin().getDatabaseManager();
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        Buyer buyer = databaseManager.getPlayer(player.getName());
        if (buyer == null) return;

        Calendar endDate = buyer.getEndDate();
        if (databaseManager.buyerMustBeAlerted(player.getName())){
            final int reminderTime = config.getInt("reminder_time");
            StelyMarketPlugin.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(StelyMarketPlugin.getPlugin(), () -> {
                // player.sendMessage("§cVotre market se supprimera dans " + getTimeLeft(endDate));
                player.sendMessage(MessageManager.MARKET_DELETED_IN.setReplacement(getTimeLeft(endDate)).getMessage());
            }, reminderTime * 20L);
        }
    }

    private String getTimeLeft(Calendar endDate){
        String timeLeftFormat = config.getString("time_left_format");
        Calendar timeLeft = Calendar.getInstance();
        timeLeft.setTimeInMillis(endDate.getTimeInMillis() - timeLeft.getTimeInMillis());
        return timeLeftFormat.replace("%d", String.valueOf(timeLeft.get(Calendar.DAY_OF_MONTH)-1))
            .replace("%h", String.valueOf(timeLeft.get(Calendar.HOUR_OF_DAY)))
            .replace("%m", String.valueOf(timeLeft.get(Calendar.MINUTE)))
            .replace("%s", String.valueOf(timeLeft.get(Calendar.SECOND)));
    }
}
