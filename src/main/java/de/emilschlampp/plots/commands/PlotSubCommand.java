package de.emilschlampp.plots.commands;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.utils.math_sys;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class PlotSubCommand {

    public boolean isOnPlotCheck(Player player) {
        if(math_sys.isroad(player.getLocation())) {
            player.sendMessage(PREFIX+"Bitte gehe auf ein Plot!");
            return false;
        }
        return true;
    }

    public boolean isPlotAdmin(Player player) {
        if(!isOnPlotCheck(player)) {
            return false;
        }
        if(!StorageMain.hasOwner(math_sys.getPlot(player.getLocation()))) {
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return false;
        }
        Plot plot = StorageMain.getPlot(math_sys.getPlot(player.getLocation()));
        if(!plot.canDoAdmin(player)) {
            player.sendMessage(PREFIX+"Das ist nicht dein Plot!");
            return false;
        }
        return true;
    }

    public static final String PREFIX = "§7[§6Plots§7] §a● §6";

    public PlotSubCommand(@NotNull String name) {
        Validate.notNull(name);
        setName(name);
    }

    public PlotSubCommand(@NotNull String name, String perm) {
        this(name);
        setPerm(perm);
    }

    public PlotSubCommand(@NotNull String name, @NotNull List<String> aliases) {
        this(name);
        Validate.notNull(aliases);
        setAliases(aliases);
    }

    public PlotSubCommand(@NotNull String name, String perm, String... aliases) {
        this(name, perm, Arrays.asList(aliases));
    }

    public PlotSubCommand(@NotNull String name, @NotNull List<String> aliases, String perm) {
        this(name);
        Validate.notNull(aliases);
        setAliases(aliases);
        setPerm(perm);
    }

    public PlotSubCommand(@NotNull String name, String perm, @NotNull List<String> aliases) {
        this(name);
        Validate.notNull(aliases);
        setAliases(aliases);
        setPerm(perm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlotSubCommand that = (PlotSubCommand) o;
        return name.equals(that.name) && Objects.equals(perm, that.perm) && alias.equals(that.alias);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, perm, alias);
    }

    private String name;

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String perm = null;

    private List<String> alias = new ArrayList<>();

    public List<String> getAliases() {
        return alias;
    }

    public void setAliases(@NotNull List<String> aliases) {
        Validate.notNull(aliases);
        this.alias = aliases;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public boolean canuse(Player player) {
        if(perm == null) {
            return true;
        }
        if(perm.equalsIgnoreCase("")) {
            return true;
        }
        return player.hasPermission(perm);
    }

    public abstract List<String> tabComplete(Player player, String[] args);
    public abstract void execute(Player player, String[] args);
}
