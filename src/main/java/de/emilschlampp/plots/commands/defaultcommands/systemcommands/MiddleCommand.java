package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.MathSys;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MiddleCommand extends PlotSubCommand implements HelpCommandInterface {
    public MiddleCommand() {
        super("middle", "splots.middle");
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
        player.teleport(MathSys.getMiddleLocation(MathSys.getPlot(player.getLocation())).add(0.5,0,0.5));
        player.sendMessage(PREFIX+"Du wurdest zur Mitte teleportiert!");
    }

    @Override
    public String getHelp() {
        return "Teleportiert dich in die Mitte von dem Grundstück auf dem du stehst.";
    }
}
