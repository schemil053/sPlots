package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.storage.Plot;
import de.emilschlampp.plots.storage.StorageMain;
import de.emilschlampp.plots.utils.MathSys;
import de.emilschlampp.plots.utils.ObjectPair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommand extends PlotSubCommand implements HelpCommandInterface {
    public ClaimCommand() {
        super("claim", "splots.claim", "c");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(!isOnPlotCheck(player)) {
            return;
        }
        if(StorageMain.hasOwner(MathSys.getPlot(player.getLocation()))) {
            player.sendMessage(PREFIX+"Dieses Plot wurde bereits beansprucht.");
            return;
        }
        if(!StorageMain.canClaimMore(player)) {
            player.sendMessage(PREFIX+"Du kannst keine weiteren Plots besitzen.");
            return;
        }
        ObjectPair<Integer, Integer> pair = MathSys.getPlot(MathSys.getPlot(player.getLocation()));
        Plot plot = new Plot(pair.a, pair.b, player.getUniqueId(), new ArrayList<>(), new ArrayList<>());
        plot.setClaimBorder();
        plot.save();
        player.sendMessage(PREFIX+"Du hast das Plot §b"+plot.id()+"§6 beansprucht.");
    }

    @Override
    public String getHelp() {
        return "Gibt dir das Plot, auf dem du stehst solange es niemanden gehört.";
    }
}
