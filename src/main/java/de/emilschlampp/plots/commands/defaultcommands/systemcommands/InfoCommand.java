package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.storage.Plot;
import de.emilschlampp.plots.storage.StorageMain;
import de.emilschlampp.plots.utils.EComList;
import de.emilschlampp.plots.utils.MathSys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InfoCommand extends PlotSubCommand implements HelpCommandInterface {
    public InfoCommand() {
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
        if(!StorageMain.hasOwner(MathSys.getPlot(player.getLocation()))) {
            player.sendMessage(PREFIX+"Das Plot §b"+ MathSys.getPlot(player.getLocation())+" §6hat keinen Besitzer.");
            return;
        }
        Plot playerplot = StorageMain.getPlot(MathSys.getPlot(player.getLocation()));
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
            if(FlagCommand.getFlags().contains(flag)) {
                a.add(flag.toSimpleString());
            }
        }
        return a;
    }

    @Override
    public String getHelp() {
        return "Gibt dir Infos über das Grundstück auf dem du stehst.";
    }
}
