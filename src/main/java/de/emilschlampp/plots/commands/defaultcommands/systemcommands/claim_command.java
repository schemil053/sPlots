package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.ObjectPair;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class claim_command extends PlotSubCommand implements HelpCommandInterface {
    public claim_command() {
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
        if(StorageMain.hasOwner(math_sys.getPlot(player.getLocation()))) {
            player.sendMessage(PREFIX+"Dieses Plot wurde bereits beansprucht.");
            return;
        }
        if(!StorageMain.canClaimMore(player)) {
            player.sendMessage(PREFIX+"Du kannst keine weiteren Plots besitzen.");
            return;
        }
        ObjectPair<Integer, Integer> pair = math_sys.getPlot(math_sys.getPlot(player.getLocation()));
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
