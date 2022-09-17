package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class toggle_command extends PlotSubCommand implements HelpCommandInterface {
    public toggle_command() {
        super("toggle", "splots.toggle", "tog");
        if(!togglevalues.isEmpty()) {
            return;
        }
        registerValue(new ToggleValue("confirm", true));
    }

    private static List<ToggleValue> togglevalues = new ArrayList<>();

    public static void setValue(UUID uuid, String name, Boolean value) {
        StorageMain.yamlConfiguration.set("player.toggle."+uuid+"."+name, value);
        StorageMain.save();
    }

    public static void registerValue(ToggleValue value) {
        togglevalues.add(value);
    }


    public static boolean isValue(UUID uuid, String name) {
        boolean s = false;
        if(searchValue(name) != null) {
            s = Objects.requireNonNull(searchValue(name)).standard;
        }

        return StorageMain.yamlConfiguration.getBoolean("player.toggle."+uuid+"."+name, s);
    }

    private static ToggleValue searchValue(String name) {
        for(ToggleValue value : togglevalues) {
            if(value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    private static List<String> getValuesString() {
        List<String> a = new ArrayList<>();
        for(ToggleValue value : togglevalues) {
            a.add(value.name);
        }
        return a;
    }


    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if(args.length > 1) {
            return new ArrayList<>();
        }
        return getValuesString();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 1) {
            if(searchValue(args[0]) == null) {
                player.sendMessage(PREFIX+"Unbekannter Wert!");
                return;
            }
            setValue(player.getUniqueId(), args[0], !isValue(player.getUniqueId(), args[0]));
            player.sendMessage(PREFIX+"Du hast "+args[0]+ (isValue(player.getUniqueId(), args[0]) ? " §aaktiviert§6." : " §cdeaktiviert§6."));
        } else {
            player.sendMessage(PREFIX+"Syntax: /plot toggle <wert>");
        }
    }

    @Override
    public String getHelp() {
        return "Verändert die angegebene Einstellung.";
    }

    public static class ToggleValue {
        public ToggleValue(String name, Boolean standard) {
            this.name = name;
            this.standard = standard;
        }
        private String name;
        private boolean standard;

        public String toString(boolean a) {
            return name+";"+a;
        }
    }
}
