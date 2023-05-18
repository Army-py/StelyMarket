package fr.army.stelymarket.utils.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.army.stelymarket.StelyMarketPlugin;

public class TasksManager {

    private final YamlConfiguration config = StelyMarketPlugin.getPlugin().getConfig();
    
    public void firstDayTask() {
        StelyMarketPlugin.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(StelyMarketPlugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (config.getBoolean("first_day_task.toggle")){
                    StelyMarketPlugin.getPlugin().getServer().broadcastMessage(MessageManager.TASK_FIRST_DAY.getMessage());
                }
            }
        }, 0, config.getInt("first_day_task.interval") * 20L);
    } 
}
