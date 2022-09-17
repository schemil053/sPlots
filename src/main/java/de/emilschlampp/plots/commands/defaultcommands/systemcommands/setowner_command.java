package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.ObjectPair;
import de.emilschlampp.plots.utils.OfflineGetter;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class setowner_command extends PlotSubCommand implements HelpCommandInterface {
    public setowner_command() {
        super("setowner","splots.setowner", "so");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length < 2) {
            return OfflineGetter.getOfflinenames();
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(!isOnPlotCheck(player)) {
            return;
        }
        if(args.length == 0) {
            player.sendMessage(PREFIX+"Syntax: /plot setowner <name>");
            return;
        }
        if(StorageMain.hasOwner(math_sys.getPlot(player.getLocation()))) {
            if(!StorageMain.getPlot(math_sys.getPlot(player.getLocation())).getOwner().equals(player.getUniqueId())) {
                if(!player.hasPermission("splots.admin")) {
                    player.sendMessage(PlotSubCommand.PREFIX+"Das ist nicht dein Plot!");
                    return;
                }
            }
        }
        OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
        Plot plot;
        if(!StorageMain.hasOwner(math_sys.getPlot(player.getLocation()))) {
            ObjectPair<Integer, Integer> pair = math_sys.getPlot(math_sys.getPlot(player.getLocation()));
            plot = new Plot(pair.a, pair.b, arg.getUniqueId(), new ArrayList<>(), new ArrayList<>());
            plot.setClaimBorder();
        } else {
            plot = StorageMain.getPlot(math_sys.getPlot(player.getLocation()));
        }
        plot.setOwner(arg.getUniqueId());
        plot.save();
        player.sendMessage(PREFIX+"Du hast den Besitzer gesetzt!");
    }

    @Override
    public String getHelp() {
        return "Verändert den Besitzer von dem Grundstück auf dem du stehst.";
    }
}
