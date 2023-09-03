package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.storage.Plot;
import de.emilschlampp.plots.storage.StorageMain;
import de.emilschlampp.plots.utils.MathSys;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClearCommand extends PlotSubCommand implements HelpCommandInterface {
    public ClearCommand() {
        super("clear", "splots.clear", Arrays.asList("cl"));
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
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return;
        }
        Plot plot = StorageMain.getPlot(MathSys.getPlot(player.getLocation()));
        if(!plot.canDoAdmin(player)) {
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return;
        }
        ConfirmCommand.confirm(player, new Runnable() {
            @Override
            public void run() {
                long a = System.currentTimeMillis();
                plot.clear();
                long ms = System.currentTimeMillis()-a;
                player.sendMessage(PREFIX+"Das Plot §b"+plot.id()+" §6wurde erfolgreich geleert. Dieser Vorgang dauerte "+ms+"ms.");
            }
        });
    }

    @Override
    public String getHelp() {
        return "Leert das Grundstück auf dem du stehst.";
    }
}
