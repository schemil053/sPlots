package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class delete_command extends PlotSubCommand {
    public delete_command() {
        super("delete", "splots.delete", Arrays.asList("del"));
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
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return;
        }
        Plot plot = StorageMain.getPlot(math_sys.getPlot(player.getLocation()));
        if(!plot.canDoAdmin(player)) {
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return;
        }
        confirm_command.confirm(player, new Runnable() {
            @Override
            public void run() {
                long a = System.currentTimeMillis();
                plot.delete();
                long ms = System.currentTimeMillis()-a;
                player.sendMessage(PREFIX+"Das Plot §b"+plot.id()+" §6wurde erfolgreich gelöscht. Dieser Vorgang dauerte "+ms+"ms.");
            }
        });
    }
}
