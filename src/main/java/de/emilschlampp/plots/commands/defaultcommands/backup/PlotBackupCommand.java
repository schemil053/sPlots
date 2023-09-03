package de.emilschlampp.plots.commands.defaultcommands.backup;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.BlockSaver;
import de.emilschlampp.plots.utils.MathSys;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlotBackupCommand extends PlotSubCommand {
    public PlotBackupCommand() {
        super("backup", "splots.admin");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length == 1) {
            return Arrays.asList("save", "load", "delete");
        }
        if(args.length == 2) {
            List<String> a = new ArrayList<>();
            File file = new File(Plots.instance.getDataFolder(), "backups");
            if(!file.isDirectory()) {
                file.mkdirs();
            }
            if(!file.isDirectory()) {
                return new ArrayList<>();
            }
            for(File b : file.listFiles()) {
                a.add(b.getName().replace(".ecopy",""));
            }
            return a;
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(!isPlotAdmin(player)) {
            return;
        }
        player.sendMessage(PREFIX+"§cDies ist Experimentell!");
        if(args.length == 0) {
            player.sendMessage(PREFIX+"/plot backup save <name>");
            player.sendMessage(PREFIX+"/plot backup load <name>");
            player.sendMessage(PREFIX+"/plot backup delete <name>");
            return;
        }
        if(args.length == 2) {
            if(args[0].equals("delete")) {
                player.sendMessage(PREFIX+"Gelöscht!");
                File file = new File(Plots.instance.getDataFolder(), "backups");
                file.mkdirs();
                file = new File(file, args[1]+".ecopy");
                file.delete();
            }
            if(args[0].equals("save")) {
                Location location = MathSys.getLoc(MathSys.getPlot(player.getLocation()));
                location.add(1, 0, 1);
                Location location1 = location.clone().add(MathSys.pw-1, 0, MathSys.pw-1);
                location.setY(location.getWorld().getMinHeight());
                location1.setY(location1.getWorld().getMaxHeight());
                player.sendMessage(PREFIX+"§aKopiere...");
                BlockSaver.copy(location, location1, args[1], location);
                player.sendMessage(PREFIX+"§aKopieren abgeschlossen!");
            }
            if(args[0].equals("load")) {
                Location location = MathSys.getLoc(MathSys.getPlot(player.getLocation()));
                location.add(1, 0, 1);
                location.setY(location.getWorld().getMinHeight());
                player.sendMessage(PREFIX+"§aEinfügen...");
                BlockSaver.paste(args[1], location, MathSys.getPlot(player.getLocation()));
                player.sendMessage(PREFIX+"§aEinfügen abgeschlossen!");
            }
        }
    }
}
