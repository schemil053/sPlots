package de.emilschlampp.plots.commands.defaultcommands.border;

import de.emilschlampp.plots.Plots;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RandSaver {
    private static File file = new File(Plots.instance.getDataFolder(), "borders.yml");
    public static YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    public static void save() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Material translate(Material old) {
        if(old == null) {
            return null;
        }
        if(old.equals(Material.LAVA_BUCKET)) {
            return Material.LAVA;
        }
        if(old.equals(Material.WATER_BUCKET)) {
            return Material.WATER;
        }
        if(old.equals(Material.REDSTONE)) {
            return Material.REDSTONE_WIRE;
        }
        return null;
    }
}
