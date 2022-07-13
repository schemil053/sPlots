package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.commands.PlotMainCommand;
import de.emilschlampp.plots.commands.PlotSubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class help_command extends PlotSubCommand {
    public help_command() {
        super("help", "splots.help", "?");


        helpmap.put("HELP",
                "" +
                        "---------- Hilfe ----------\n" +
                        "Zufälliges Plot bekommen: §b/plot auto\n" +
                        "Plot bekommen auf dem du stehst: §b/plot claim\n" +
                        "Weitere Hilfe mit §b/plot help <Befehl>\n" +
                        "---------- Hilfe ----------"
                );
    }

    private static Map<String, String> helpmap = new HashMap<>();

    public static void setHelp(String help, String command) {
        PlotSubCommand command1 = PlotMainCommand.getCommand(command);
        if(command1 == null) {
            helpmap.put(command, help);
            return;
        }
        helpmap.put(command1.getName(), help);
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length < 2) {
            return PlotMainCommand.getCommands().stream().filter(cmd -> PlotMainCommand.getCommand(cmd).canuse(player) && helpmap.containsKey(PlotMainCommand.getCommand(cmd).getName())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 0) {
            for(String a : helpmap.getOrDefault("HELP", "Hilfe nicht gefunden!").split("\n")) {
                player.sendMessage(PREFIX +a);
            }
        } else {
            String b = args[0];
            if(!helpmap.containsKey(b)) {
                if(PlotMainCommand.getCommand(b) != null) {
                    b = PlotMainCommand.getCommand(b).getName();
                }
            }
            for(String a : helpmap.getOrDefault(b, "Hilfe nicht gefunden!").split("\n")) {
                player.sendMessage(PREFIX +a);
            }
        }
    }
}
