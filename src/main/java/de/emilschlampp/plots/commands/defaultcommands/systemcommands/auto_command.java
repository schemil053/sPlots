package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.PlotFinder;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.ObjectPair;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class auto_command  extends PlotSubCommand implements HelpCommandInterface {
    public auto_command() {
        super("auto", Arrays.asList("a"), "splots.auto");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(!StorageMain.canClaimMore(player)) {
            player.sendMessage(PREFIX+"Du kannst keine weiteren Plots besitzen.");
            return;
        }
        player.sendMessage(PREFIX+"Suche nach einem Plot...");
        String id = PlotFinder.findPlot(player.getLocation());
        ObjectPair<Integer, Integer> plotid = math_sys.getPlot(id);
        Plot plot = new Plot(plotid.a, plotid.b, player.getUniqueId(), new ArrayList<>(), new ArrayList<>());
        plot.save();
        plot.setClaimBorder();
        player.teleport(plot.getTPLocation());
        player.sendMessage(PREFIX+"Plot §b"+plot.id()+" §6erfolgreich in Besitz genommen!");
    }

    @Override
    public String getHelp() {
        return "Gibt dir ein zufälliges Plot";
    }
}
