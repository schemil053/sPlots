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

public class visit_command extends PlotSubCommand {
    public visit_command() {
        super("visit", "splots.visit", "v");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length < 2) {
            List<String> a = OfflineGetter.getOfflinenames();
            return a;
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 0) {
            player.sendMessage(PREFIX+"Syntax: /plot visit <name> <plot>");
            return;
        }
        String t = args[0];
        int plot = 1;
        if(args.length == 2) {
            try {
                plot = Integer.parseInt(args[1]);
            } catch (Exception exception) {
                player.sendMessage(PREFIX+"Syntax: /plot visit <name> <plot>");
                return;
            }
        }
        if(plot < 1) {
            player.sendMessage(PREFIX+"Das ist kein gültiges Plot!");
            return;
        }
        if((!OfflineGetter.getOfflinenames().contains(t)) && (Bukkit.getPlayerExact(args[0]) == null)) {
            player.sendMessage(PREFIX+"Spieler nicht gefunden!");
            return;
        }
        UUID ta = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        List<String> plots = StorageMain.getPlots(ta);

        if(plots.size() < plot) {
            player.sendMessage(PREFIX+"Es wurden keine Suchergebnisse erziehlt!");
            return;
        }
        Plot tpplot = StorageMain.getPlot(plots.get(plot-1));
        if(tpplot == null) {
            player.sendMessage(PREFIX+"Ein Fehler ist aufgetreten.");
            return;
        }
        player.teleport(tpplot.getTPLocation());
        player.sendMessage(PREFIX+"Du wurdest teleportiert!");
    }
}
