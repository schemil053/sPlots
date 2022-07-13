package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class flag_command extends PlotSubCommand {
    public static void registerFlag(Plot.Flag flag) {
        flags.add(flag.useclone());
    }

    public flag_command() {
        super("flag", "splots.flag");
        if(!flags.isEmpty()) {
            return;
        }
        flags.add(new Plot.Flag("explosion", Plot.Flag.FlagType.BOOLEAN));
        flags.add(new Plot.Flag("mobspawn", Plot.Flag.FlagType.BOOLEAN));
        flags.add(new Plot.Flag("lightningstrike", Plot.Flag.FlagType.BOOLEAN));
        flags.add(new Plot.Flag("use", Plot.Flag.FlagType.ELIST));
        flags.add(new Plot.Flag("break", Plot.Flag.FlagType.ELIST));
        flags.add(new Plot.Flag("time", Plot.Flag.FlagType.INTEGER));
        flags.add(new Plot.Flag("greeting", Plot.Flag.FlagType.STRING));
        flags.add(new Plot.Flag("farewell", Plot.Flag.FlagType.STRING));
    }

    public static List<Plot.Flag> getFlags() {
        return new ArrayList<>(flags);
    }

    private static List<Plot.Flag> flags = new ArrayList<>();

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length < 2) {
            return Arrays.asList("set", "remove");
        }
        if(args.length < 3) {
            List<String> a = new ArrayList<>();
            for(Plot.Flag flag : flags) {
                if(player.hasPermission("splots.flag."+flag.getName())) {
                    a.add(flag.getName());
                }
            }
            return a;
        }
        if(args.length >= 3) {
            List<Plot.Flag> a = new ArrayList<>();
            for(Plot.Flag flag : flags) {
                if(player.hasPermission("splots.flag."+flag.getName())) {
                    a.add(flag.useclone());
                }
            }
            List<String> ab = new ArrayList<>();
            for(Plot.Flag b : a) {
                if(b.getName().equalsIgnoreCase(args[1])) {
                    if(b.getType().equals(Plot.Flag.FlagType.BOOLEAN)) {
                        ab.add("true");
                        ab.add("false");
                    }
                    if(b.getType().equals(Plot.Flag.FlagType.INTEGER)) {
                        for(int i = 0; i<10; i++) {
                            ab.add(args[2]+i+"");
                        }
                    }
                }
            }
            return ab;
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length < 3) {
            if(args.length == 2) {
                if(!args[0].equalsIgnoreCase("remove")) {
                    player.sendMessage(PREFIX+"Syntax: /p flag <set/remove> <name> <wert>");
                    return;
                }
            } else {
                player.sendMessage(PREFIX+"Syntax: /p flag <set/remove> <name> <wert>");
                return;
            }
        }
        if(!isOnPlotCheck(player)) {
            return;
        }
        if(!StorageMain.hasOwner(math_sys.getPlot(player.getLocation()))) {
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return;
        }
        Plot plot = StorageMain.getPlot(math_sys.getPlot(player.getLocation()));
        if(!plot.canDoAdmin(player)) {
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return;
        }
        if(!Arrays.asList("set", "remove").contains(args[0])) {
            player.sendMessage(PREFIX+"Syntax: /p flag <set/remove> <name> <wert>");
            return;
        }
        String flag = args[1];
        boolean set = args[0].equalsIgnoreCase("set");
        String value = "";
        if(set) {
            for (int i = 2; i < args.length; i++) {
                value = value.replace("SPL§TERATOR", " ") + args[i] + "SPL§TERATOR";
            }
            value = value.replace("SPL§TERATOR", "");
        }

        Plot.Flag flag1 = null;

        for(Plot.Flag flag2 : flags) {
            if(flag2.getName().equalsIgnoreCase(flag)) {
                flag1 = flag2.useclone();
            }
        }

        if(flag1 == null) {
            player.sendMessage(PREFIX+"Diese Flag wurde nicht gefunden.");
            return;
        }

        if(!player.hasPermission("splots.flag."+flag)) {
            player.sendMessage(PREFIX+"Du hast keine Rechte um die Flag §b"+flag+" §6zu setzen.");
            return;
        }

        List<Plot.Flag> pfl = plot.getFlags();
        pfl.removeIf(pfla -> {
            return pfla.getName().equalsIgnoreCase(flag);
        });
        plot.setFlags(pfl);
        if(set) {
            if(!flag1.canVal(value)) {
                player.sendMessage(PREFIX+"Das ist kein gültiger Wert!");
                return;
            }
            List<Plot.Flag> pfla = plot.getFlags();
            flag1.setValue(value);
            pfla.add(flag1);
            plot.setFlags(pfla);
        }
        plot.save();
        if(set) {
            player.sendMessage(PREFIX+"Flag gesetzt.");
        } else {
            player.sendMessage(PREFIX+"Flag entfernt.");
        }
    }
}
