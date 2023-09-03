package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.MathSys;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPCommand extends PlotSubCommand implements HelpCommandInterface {
    public TPCommand() {
        super("tp", "splots.tp");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 0) {
            player.sendMessage(PREFIX+"Syntax: /plot tp <id>");
            return;
        }
        if(!MathSys.isPlotID(args[0])) {
            player.sendMessage(PREFIX+"Das ist keine Plot-ID!");
            return;
        }
        player.teleport(MathSys.getTeleportLocation(args[0]));
        player.sendMessage(PREFIX+"Du wurdest teleportiert!");
    }

    @Override
    public String getHelp() {
        return "Teleportiert dich zu der angegebenen Grundstücks-ID.";
    }
}
