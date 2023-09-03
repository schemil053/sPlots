package de.emilschlampp.plots.storage;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.utils.EComList;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorageMain {
    private static File file = new File(Plots.instance.getDataFolder(), "storage.yml");
    public static YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    public static void save() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removePlot(String id) {
        if(yamlConfiguration.isSet("plot."+id+".owner")) {
            String owner = yamlConfiguration.getString("plot."+id+".owner");
            try {
                List<String> p = getPlots(UUID.fromString(owner));
                while (p.remove(id));
                setPlots(UUID.fromString(owner), p);
            } catch (Exception exception) {

            }
        }
        yamlConfiguration.set("plot."+id, null);
        save();
    }

    public static List<String> getPlots(UUID uuid) {
        return new ArrayList<>(yamlConfiguration.getStringList("player.plots."+uuid.toString()));
    }

    public static void setPlots(UUID uuid, List<String> plots) {
        yamlConfiguration.set("player.plots."+uuid.toString(), plots);
        save();
    }

    public static boolean canClaimMore(Player player, int amount) {
        return player.hasPermission("splots.plot."+(getPlots(player.getUniqueId()).size()+amount));
    }

    public static boolean canClaimMore(Player player) {
        return canClaimMore(player, 1);
    }

    public static void savePlot(Plot plot) {
        if(getPlot(plot.id()) != null) {
            if (getPlot(plot.id()).getOwner() != null){
                if(!getPlot(plot.id()).getOwner().equals(plot.getOwner())) {
                    List<String> plots = getPlots(getPlot(plot.id()).getOwner());
                    while(plots.remove(plot.id()));
                    setPlots(getPlot(plot.id()).getOwner(), plots);
                }
            }
        }

        if(plot.getOwner() != null) {
            if (!getPlots(plot.getOwner()).contains(plot.id())) {
                List<String> a = getPlots(plot.getOwner());
                a.add(plot.id());
                setPlots(plot.getOwner(), a);
            }
        }
        yamlConfiguration.set("plot."+plot.id()+".owner", plot.getOwner().toString());
        yamlConfiguration.set("plot."+plot.id()+".trusted", destoList(plot.getTrusted()));
        yamlConfiguration.set("plot."+plot.id()+".flags", destoList(plot.getFlags()));
        save();
    }

    public static boolean hasOwner(String id) {
        return yamlConfiguration.isConfigurationSection("plot."+id);
    }

    public static Plot getPlot(String id) {
        String[] ss = id.split(";");

        try {
            Integer.parseInt(ss[0]);
            Integer.parseInt(ss[1]);
        } catch (Exception e) {
            return null;
        }

        if(!hasOwner(id)) {
            return null;
        }

        List<UUID> trusted = new ArrayList<>();

        for(String a : yamlConfiguration.getStringList("plot."+id+".trusted")) {
            try {
                trusted.add(UUID.fromString(a));
            }catch (Exception exception) {

            }
        }

        List<Plot.Flag> flags = new ArrayList<>();

        try {
            for (String flag : yamlConfiguration.getStringList("plot." + id + ".flags")) {
                try {
                    flags.add(new Plot.Flag(flag));
                }catch (Exception exception) {

                }
            }
        }catch (Exception exception) {

        }


        return new Plot(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
                UUID.fromString(yamlConfiguration.getString("plot."+id+".owner")),
                trusted, flags);
    }

    public static Plot getPlotMerged(String id) {
        Plot plot = getPlot(id);

        if(plot == null) {
            return null;
        }

        Plot.Flag merged = plot.getFlag("merged");

        if(merged == null) {
            return plot;
        }

        EComList list = new EComList(merged.getValue());

        int currX = plot.getX();
        int currZ = plot.getZ();
        for (String s : list.toList()) {
            try {
                if(getPlot(s) != null) {
                    String[] sp = s.split(";");
                    int xa = Integer.parseInt(sp[0]);
                    int za = Integer.parseInt(sp[1]);
                    if (currX > xa) {
                        currX = xa;
                    }
                    if (currZ > za) {
                        currZ = za;
                    }
                }
            } catch (Throwable ignored) {
                //Keine Fehler...
            }
        }

        return getPlot(currX+";"+currZ);
    }

    private static List<String> destoList(List list) {
        List<String> a = new ArrayList<>();
        for(Object o : list) {
            a.add(o.toString());
        }
        return a;
    }
}
