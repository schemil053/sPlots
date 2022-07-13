package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.PlotSubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class home_command extends PlotSubCommand {

    public home_command() {
        super("home", "splots.home", Arrays.asList("h"));
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        List<String> a = new ArrayList<>();
        for(int i = 0; i< StorageMain.getPlots(player.getUniqueId()).size(); i++) {
            a.add((i+1)+"");
        }
        return a;
    }

    @Override
    public void execute(Player player, String[] args) {
        int id = 1;
        if(args.length == 1) {
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception exception) {
                player.sendMessage(PREFIX+"Das ist keine gültige Zahl!");
                return;
            }
        }

        if(id < 1) {
            player.sendMessage(PREFIX+"Das ist kein gültiges Plot!");
            return;
        }

        if(StorageMain.getPlots(player.getUniqueId()).size() < id) {
            if(id == 1) {
                player.sendMessage(PREFIX+"Es wurden keine Suchergebnisse erziehlt!");
                return;
            }
            player.sendMessage(PREFIX+"Das ist kein gültiges Plot!");
            return;
        }

        player.teleport(StorageMain.getPlot(StorageMain.getPlots(player.getUniqueId()).get(id-1)).getTPLocation());
        player.sendMessage(PREFIX+"Du wurdest teleportiert!");
    }
}
