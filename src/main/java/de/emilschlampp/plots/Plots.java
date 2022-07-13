package de.emilschlampp.plots;

import de.emilschlampp.plots.commands.PlotMainCommand;
import de.emilschlampp.plots.commands.defaultcommands.Register;
import de.emilschlampp.plots.gen.main_gen;
import de.emilschlampp.plots.listener.EntityListener;
import de.emilschlampp.plots.listener.PlayerListener;
import de.emilschlampp.plots.utils.GenConfigLoader;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public final class Plots extends JavaPlugin {

    public static Plots instance;

/*
    public static void deleteDir(File path) {
        for (File file : path.listFiles()) {
            if (file.isDirectory())
                deleteDir(file);
            file.delete();
        }
        path.delete();
    }

 */

    @Override
    public void onEnable() {
        instance = this;

        if(GenConfigLoader.issetup()) {
            GenConfigLoader.startSetup();
            return;
        }
        GenConfigLoader.loadData();

        // Plugin startup logic
        Bukkit.createWorld(new WorldCreator(math_sys.plotworld)
                .generateStructures(false).generator(new main_gen()));


        PlotMainCommand command = new PlotMainCommand();

        Objects.requireNonNull(getCommand("plot")).setExecutor(command);
        Objects.requireNonNull(getCommand("plot")).setTabCompleter(command);

        new Register(command);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
