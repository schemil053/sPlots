package de.emilschlampp.plots.utils;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.Storage.Plot;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class OfflineGetter implements Listener {

    public static void reg() {
        Plots.instance.getServer().getPluginManager().registerEvents(new OfflineGetter(), Plots.instance);
        update();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        update();
    }

    public static List<String> getOfflinenames() {
        return new ArrayList<>(offlinenames);
    }

    private static List<String> offlinenames = new ArrayList<>();

    private static void update() {
        List<String> an = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(Plots.instance, () -> {
           for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
               an.add(player.getName());
           }
           offlinenames = an;
        });
    }
}
