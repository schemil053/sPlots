package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.OfflineGetter;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class untrust_command extends PlotSubCommand {
    public untrust_command() {
        super("untrust", "splots.untrust", "unt");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length < 2) {
            List<String> a = new ArrayList<>();
            if(!math_sys.isroad(player.getLocation())) {
                if(StorageMain.hasOwner(math_sys.getPlot(player.getLocation()))) {
                    Plot plot = StorageMain.getPlot(math_sys.getPlot(player.getLocation()));
                    for(UUID ab : plot.getTrusted()) {
                        a.add(Bukkit.getOfflinePlayer(ab).getName());
                    }
                    if(plot.isBooleanFlagSet("buildall")) {
                        a.add("*");
                    }
                }
            }
            return a;
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 0) {
            player.sendMessage(PREFIX+"Syntax: /plot untrust <name>");
            return;
        }
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

        if(args[0].equalsIgnoreCase("*")) {
            if(plot.getFlag("buildall") == null) {
                player.sendMessage(PREFIX+"Alle Spieler sind nicht vertraut!");
                return;
            }

            List<Plot.Flag> flags = plot.getFlags();
            while(flags.remove(new Plot.Flag("buildall", "true", Plot.Flag.FlagType.BOOLEAN)));
            plot.setFlags(flags);
            plot.save();

            player.sendMessage(PREFIX+"Du hast alle Spieler erfolgreich aus den vertrauten Spielern entfernt.");
            return;
        }

        UUID t = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        List<UUID> trusted = new ArrayList<>(plot.getTrusted());
        if(!trusted.contains(t)) {
            player.sendMessage(PREFIX+"Dieser Spieler ist nicht vertraut!");
            return;
        }
        while(trusted.remove(t));
        plot.setTrusted(trusted);
        plot.save();
        player.sendMessage(PREFIX+"Du hast diesen Spieler erfolgreich aus den vertrauten Spielern entfernt.");
    }
}
