package de.emilschlampp.plots.commands.defaultcommands.chunkanalyse;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.MathSys;
import de.emilschlampp.plots.utils.ObjectPair;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TPChunkCommand extends PlotSubCommand implements HelpCommandInterface {
    public TPChunkCommand() {
        super("tpchunk", "splots.tpchunk");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 1) {
            if(!MathSys.isPlotID(args[0])) {
                player.sendMessage(PREFIX+"Syntax: /plot tpchunk <chunk>");
                player.sendMessage(PREFIX+"Beispiel: /plot tpchunk -5;2");
                return;
            }
            ObjectPair<Integer, Integer> a = MathSys.getPlot(args[0]);
            player.teleport(new Location(player.getWorld(), a.a*16+8, player.getLocation().getY(), a.b*16+8));
        } else {
            player.sendMessage(PREFIX+"Syntax: /plot tpchunk <chunk>");
            player.sendMessage(PREFIX+"Beispiel: /plot tpchunk -5;2");
        }
    }

    @Override
    public String getHelp() {
        return "Teleportiert dich zu dem Chunk in deiner Welt.\nSyntax: §a/p tpchunk <x;y>";
    }
}
