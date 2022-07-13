package de.emilschlampp.plots.commands;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.commands.defaultcommands.systemcommands.help_command;
import de.emilschlampp.plots.utils.Utils;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlotMainCommand implements TabExecutor {

    public boolean reg = false;

    private static String[] removeFirstElement(String[] arr) {
        if(arr.length == 0) { return arr; }
        String newArr[] = new String[arr.length - 1];
        for (int i = 1; i < arr.length; i++) { newArr[i-1] = arr[i]; }
        return newArr;
    }

    private static List<PlotSubCommand> commands = new ArrayList<>();

    public static String getNearest(String a, Player exec) {
        List<String> b;
        if(exec != null) {
            b = getCommands().stream().filter(s -> {
               return getCommand(s).canuse(exec);
            }).collect(Collectors.toList());
        } else {
            b = getCommands();
        }
        b.sort(Comparator.comparing(s -> {
            return -Utils.findSimilarity(s, a);
        }));

        if(b.size() < 1) {
            return null;
        }
        String ac = b.get(0);
        if(Utils.findSimilarity(ac, a) < 0.05) {
            return null;
        }
        return ac;
    }

    public static void register(PlotSubCommand command) {
        commands.add(command);
        if(command instanceof Listener) {
            Plots.instance.getServer().getPluginManager().registerEvents((Listener) command, Plots.instance);
        }
        if(command instanceof HelpCommandInterface) {
            help_command.setHelp(((HelpCommandInterface) command).getHelp(), command.getName());
            for(String a : command.getAliases()) {
                help_command.setHelp(((HelpCommandInterface) command).getHelp(), a);
            }
        }
    }

    public static PlotSubCommand getCommand(String name) {
        for(PlotSubCommand command : commands) {
            if(command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        for(PlotSubCommand command : commands) {
            if(command.getAliases().contains(name)) {
                return command;
            }
        }
        return null;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(sender instanceof Player) {
            if(args.length > 1) {
                if(getCommand(args[0]) != null) {
                    if(!getCommand(args[0]).canuse(((Player) sender).getPlayer())) {
                        return new ArrayList<>();
                    }
                    List<String> a = getCommand(args[0]).tabComplete(((Player) sender).getPlayer(), removeFirstElement(args));
                    if(a == null) {
                        return null;
                    }
                    return copyMatches(a, args[args.length-1]);
                }
            } else {
                List<String> a = new ArrayList<>();
                for(PlotSubCommand commanda : commands) {
                    if(!commanda.canuse(((Player) sender).getPlayer())) {
                        continue;
                    }
                    a.add(commanda.getName());
                    for(String al : commanda.getAliases()) {
                        if(!a.contains(al)) {
                            a.add(al);
                        }
                    }
                }
                return copyMatches(a, args[args.length-1]);
            }
        }
        return new ArrayList<>();
    }

    public static List<String> getCommands() {
        List<String> a = new ArrayList<>();
        for(PlotSubCommand commanda : commands) {
            a.add(commanda.getName());
            for(String al : commanda.getAliases()) {
                if(!a.contains(al)) {
                    a.add(al);
                }
            }
        }
        return a;
    }


    public static List<String> copyMatches(List<String> o, String matches) {
        List a = new ArrayList<>();
        for(String o1 : o) {
            if(startsWithIgnoreCase(o1, matches)) {
                a.add(o1);
            }
        }
        return a;
    }

    public static boolean startsWithIgnoreCase(final String string, final String prefix) throws IllegalArgumentException, NullPointerException {
        if (string.length() < prefix.length()) {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            if (args.length == 0) {
                if(getCommand("help") != null) {
                    if(getCommand("help").canuse(((Player) sender).getPlayer())) {
                        getCommand("help").execute(((Player) sender).getPlayer(), removeFirstElement(args));
                    } else {
                        sender.sendMessage(PlotSubCommand.PREFIX+"Keine Rechte!");
                        return true;
                    }
                } else {
                    sender.sendMessage(PlotSubCommand.PREFIX+"Uff, die Hilfe wurde nicht gefunden...");
                }
                return true;
            }
            if (getCommand(args[0]) == null) {
                String aa = getNearest(args[0], ((Player) sender).getPlayer());
                if(aa == null) {
                    sender.sendMessage(PlotSubCommand.PREFIX+"Unbekannter Befehl!");
                    return true;
                }
                sender.sendMessage(PlotSubCommand.PREFIX+"Unbekannter Befehl! Meintest du /plot §b"+aa+"§6?");
                return true;
            }
            if(!getCommand(args[0]).canuse(((Player) sender).getPlayer())) {
                sender.sendMessage(PlotSubCommand.PREFIX+"Keine Rechte!");
                return true;
            }
            getCommand(args[0]).execute(((Player) sender).getPlayer(), removeFirstElement(args));
        }
        return true;
    }
}
