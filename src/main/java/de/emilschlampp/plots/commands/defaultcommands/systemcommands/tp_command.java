package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class tp_command extends PlotSubCommand {
    public tp_command() {
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
        if(!math_sys.isPlotID(args[0])) {
            player.sendMessage(PREFIX+"Das ist keine Plot-ID!");
            return;
        }
        player.teleport(math_sys.getTeleportLocation(args[0]));
        player.sendMessage(PREFIX+"Du wurdest teleportiert!");
    }
}
