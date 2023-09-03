package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.storage.Plot;
import de.emilschlampp.plots.storage.StorageMain;
import de.emilschlampp.plots.utils.EComList;
import de.emilschlampp.plots.utils.MathSys;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MergeCommand extends PlotSubCommand {
    public MergeCommand() {
        super("merge", "splots.merge");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(PREFIX+"§eDieser Befehl ist in Entwicklung! Erwarte nicht zu viel.");
        if(!isPlotAdmin(player)) {
            return;
        }
        Block block = player.getTargetBlock(100);
        if(block == null) {
            player.sendMessage(PREFIX+"§cBitte schaue auf ein anderes Plot!");
            return;
        }
        Plot ploto = StorageMain.getPlot(MathSys.getPlot(block.getLocation()));
        Plot plot = StorageMain.getPlot(MathSys.getPlot(player.getLocation()));
        if(ploto == null) {
            player.sendMessage(PREFIX+"§cDas andere Plot hat keinen Besitzer!");
            return;
        }
        if(plot == null) {
            return;
        }
        if(getDistance(plot.getX(), ploto.getX()) > 1 || getDistance(plot.getZ(), ploto.getZ()) > 1) {
            player.sendMessage("DA: "+getDistance(plot.getX(), ploto.getX())+"  "+getDistance(plot.getZ(), ploto.getZ()));
            player.sendMessage(PREFIX+"§cDie Plots müssen nebeneinander liegen!");
            return;
        }
        EComList listo = new EComList();
        Plot.Flag flago = ploto.getFlag("merged");
        if(flago != null) {
            listo = new EComList(flago.getValue());
        } else {
            flago = new Plot.Flag("merged", Plot.Flag.FlagType.ELIST);
        }
        listo.add(MathSys.getPlot(player.getLocation()));
        flago.setValue(listo.toString());
        ploto.setFlag(flago);

        EComList lista = new EComList();
        Plot.Flag flaga = ploto.getFlag("merged");
        if(flaga != null) {
            lista = new EComList(flago.getValue());
        } else {
            flaga = new Plot.Flag("merged", Plot.Flag.FlagType.ELIST);
        }
        lista.add(ploto.id());
        flaga.setValue(lista.toString());
        plot.setFlag(flago);

        MathSys.setRoad(plot.id(), ploto.id());

        StorageMain.savePlot(plot);
        StorageMain.savePlot(ploto);
    }

    private static int getDistance(int a, int b) {
        if(a-b > 0) {
            return a-b;
        } else {
            return -(a-b);
        }
    }
}
