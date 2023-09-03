package de.emilschlampp.plots;

import de.emilschlampp.plots.commands.PlotMainCommand;
import de.emilschlampp.plots.commands.defaultcommands.Register;
import de.emilschlampp.plots.gen.PlotGenerator;
import de.emilschlampp.plots.listener.EntityListener;
import de.emilschlampp.plots.listener.PlayerListener;
import de.emilschlampp.plots.utils.GenConfigLoader;
import de.emilschlampp.plots.utils.MathSys;
import de.emilschlampp.plots.utils.PlayerQuitClearList;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Plots extends JavaPlugin {

    public static Plots instance;


    @Override
    public void onEnable() {
        instance = this;

        if(GenConfigLoader.issetup()) {
            GenConfigLoader.startSetup();
            return;
        }
        GenConfigLoader.loadData();

        // Plugin startup logic
        Bukkit.createWorld(new WorldCreator(MathSys.plotworld)
                .generateStructures(false).generator(new PlotGenerator()));


        PlotMainCommand command = new PlotMainCommand();

        Objects.requireNonNull(getCommand("plot")).setExecutor(command);
        Objects.requireNonNull(getCommand("plot")).setTabCompleter(command);

        new Register(command);

        registerListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerListener() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitClearList.EListen(), this);
    }
}
