package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.storage.Plot;
import de.emilschlampp.plots.storage.PlotFinder;
import de.emilschlampp.plots.storage.StorageMain;
import de.emilschlampp.plots.utils.MathSys;
import de.emilschlampp.plots.utils.ObjectPair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoCommand extends PlotSubCommand implements HelpCommandInterface {
    public AutoCommand() {
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
        ObjectPair<Integer, Integer> plotid = MathSys.getPlot(id);
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
