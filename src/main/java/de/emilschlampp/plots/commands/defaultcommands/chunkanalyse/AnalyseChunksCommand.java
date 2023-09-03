package de.emilschlampp.plots.commands.defaultcommands.chunkanalyse;

import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.ObjectPair;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AnalyseChunksCommand extends PlotSubCommand implements HelpCommandInterface {
    public AnalyseChunksCommand() {
        super("analysechunks", "splots.analysechunks");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        World world = player.getWorld();
        List<ObjectPair<Chunk, Double>> list = new ArrayList<>();
        for(Chunk chunk : world.getLoadedChunks()) {
            list.add(new ObjectPair<Chunk, Double>(chunk, (chunk.getEntities().length*1.5)+chunk.getTileEntities().length));
        }
        int i = 0;
        int max = 3;
        if(args.length == 1) {
            try {
                max = Integer.parseInt(args[0]);
            } catch (Exception exception) {

            }
        }
        list.sort(Comparator.comparing(a -> -a.b.intValue()));
        player.sendMessage(PREFIX+"Chunk-Analyse (Zeige max. §b"+max+"§6 Chunks):");
        for(ObjectPair<Chunk, Double> pair : list) {
            i++;
            player.sendMessage();
            TextComponent textComponent = new TextComponent(PREFIX+"Chunk: "+pair.a.getX()+";"+pair.a.getZ()+" Score: "+pair.b);
            if(player.hasPermission("splots.tpchunk")) {
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/plot tpchunk " + pair.a.getX() + ";" + pair.a.getZ()));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aKlicken zum teleportieren")));
            }
          //  player.sendMessage(textComponent);
            player.spigot().sendMessage(textComponent);
            if(i >= max) {
                break;
            }
        }
    }

    @Override
    public String getHelp() {
        return "Analysiert die Chunks in deiner Welt.";
    }
}
