package de.emilschlampp.plots.commands.defaultcommands.backup;

import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.BlockSaver;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class backup_command extends PlotSubCommand {
    public backup_command() {
        super("backup", "splots.admin");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length == 1) {
            return Arrays.asList("save", "load");
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
            return;
        }
        if(args.length == 2) {
            if(args[0].equals("save")) {
                Location location = math_sys.getLoc(math_sys.getPlot(player.getLocation()));
                location.add(1, 0, 1);
                Location location1 = location.clone().add(math_sys.pw-1, 0, math_sys.pw-1);
                location.setY(location.getWorld().getMinHeight());
                location1.setY(location1.getWorld().getMaxHeight());
                player.sendMessage(PREFIX+"§aKopiere...");
                BlockSaver.copy(location, location1, args[1], location);
                player.sendMessage(PREFIX+"§aKopieren abgeschlossen!");
            }
            if(args[0].equals("load")) {
                Location location = math_sys.getLoc(math_sys.getPlot(player.getLocation()));
                location.setY(location.getWorld().getMinHeight());
                player.sendMessage(PREFIX+"§aEinfügen...");
                BlockSaver.paste(args[1], location);
                player.sendMessage(PREFIX+"§aEinfügen abgeschlossen!");
            }
        }
    }
}
