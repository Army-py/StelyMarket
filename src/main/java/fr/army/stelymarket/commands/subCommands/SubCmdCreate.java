package fr.army.stelymarket.commands.subCommands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;

import fr.army.stelymarket.StelyMarketPlugin;
import fr.army.stelymarket.commands.SubCommand;
import fr.army.stelymarket.utils.MarketArea;

public class SubCmdCreate extends SubCommand {

    public SubCmdCreate(StelyMarketPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player actor = BukkitAdapter.adapt((org.bukkit.entity.Player) sender); // WorldEdit's native Player class extends Actor
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        Region region;
        World selectionWorld = localSession.getSelectionWorld();
        try {
            if (selectionWorld == null) throw new IncompleteRegionException();
            region = localSession.getSelection(selectionWorld);
        } catch (IncompleteRegionException ex) {
            actor.printError(TextComponent.of("Faites une sélection avant de créer un market."));
            return true;
        }

        new MarketArea().create(region);

        actor.print(TextComponent.of("Market créée !"));

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
