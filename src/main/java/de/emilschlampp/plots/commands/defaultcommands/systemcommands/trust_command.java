package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.OfflineGetter;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class trust_command extends PlotSubCommand {
    public trust_command() {
        super("trust", "splots.trust", "t");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length < 2) {
            List<String> a = OfflineGetter.getOfflinenames();
            a.add("*");
            return a;
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 0) {
            player.sendMessage(PREFIX+"Syntax: /plot trust <name>");
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
            if(plot.getFlag("buildall") != null) {
                if(plot.getFlag("buildall").getValue() != null) {
                    if(plot.getFlag("buildall").getValue().equalsIgnoreCase("true")) {
                        player.sendMessage(PREFIX+"Alle Spieler ist bereits vertraut!");
                        return;
                    } else {
                        plot.getFlag("buildall").setValue("true");
                        player.sendMessage(PREFIX+"Du hast alle Spieler erfolgreich vertraut.");
                        return;
                    }
                } else {
                    plot.getFlag("buildall").setValue("true");
                    player.sendMessage(PREFIX+"Du hast alle Spieler erfolgreich vertraut.");
                    return;
                }
            }

            List<Plot.Flag> flags = plot.getFlags();
            flags.add(new Plot.Flag("buildall", "true", Plot.Flag.FlagType.BOOLEAN));
            plot.setFlags(flags);
            plot.save();

            player.sendMessage(PREFIX+"Du hast alle Spieler erfolgreich vertraut.");
            return;
        }

        UUID t = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        List<UUID> trusted = new ArrayList<>(plot.getTrusted());
        if(trusted.contains(t)) {
            player.sendMessage(PREFIX+"Dieser Spieler ist bereits vertraut!");
            return;
        }
        trusted.add(t);
        plot.setTrusted(trusted);
        plot.save();
        player.sendMessage(PREFIX+"Du hast diesen Spieler erfolgreich vertraut.");
    }
}
