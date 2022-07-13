package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.EComList;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class info_command extends PlotSubCommand {
    public info_command() {
        super("info", "splots.info", Arrays.asList("i"));
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
        if(!StorageMain.hasOwner(math_sys.getPlot(player.getLocation()))) {
            player.sendMessage(PREFIX+"Das Plot §b"+math_sys.getPlot(player.getLocation())+" §6hat keinen Besitzer.");
            return;
        }
        Plot playerplot = StorageMain.getPlot(math_sys.getPlot(player.getLocation()));
        player.sendMessage(PREFIX+"-------- Info --------");
        player.sendMessage(PREFIX+"ID: "+playerplot.id());
        player.sendMessage(PREFIX+"Owner: "+ Bukkit.getOfflinePlayer(playerplot.getOwner()).getName());
        player.sendMessage(PREFIX+"Vertraut: "+new EComList(changetostring(playerplot.getTrusted(), playerplot), true).toString());
        player.sendMessage(PREFIX+"Flags: "+new EComList(changetostringF(playerplot.getFlags()), true).toString());
        player.sendMessage(PREFIX+"-------- Info --------");
    }

    private static List<String> changetostring(List<UUID> uuids, Plot plot) {
        List<String> a = new ArrayList<>();
        for(UUID uuid : uuids) {
            a.add(Bukkit.getOfflinePlayer(uuid).getName());
        }
        if(plot.isBooleanFlagSet("buildall")) {
            a.add("*");
        }
        return a;
    }

    private static List<String> changetostringF(List<Plot.Flag> flags) {
        List<String> a = new ArrayList<>();
        for(Plot.Flag flag : flags) {
            if(flag_command.getFlags().contains(flag)) {
                a.add(flag.toSimpleString());
            }
        }
        return a;
    }
}
