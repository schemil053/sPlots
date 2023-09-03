package de.emilschlampp.plots.listener.events;

import de.emilschlampp.plots.utils.MathSys;
import de.emilschlampp.plots.utils.ObjectPair;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerEntryPlotEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private String plotid;
    private Player player;
    private ObjectPair<Integer, Integer> id;
    private Cause cause;

    public PlayerEntryPlotEvent(@NotNull Player player, @NotNull String plotid, Cause cause) {
        Validate.notNull(player);
        Validate.notNull(plotid);
        if(!MathSys.isPlotID(plotid)) {
            throw new IllegalArgumentException("'plotid' is'nt a PlotID!");
        }
        id = MathSys.getPlot(plotid);
        this.player = player;
        this.plotid = plotid;
        this.cause = cause;
        if(cause == null) {
            this.cause = Cause.UNKNOWN;
        }
    }

    public PlayerEntryPlotEvent(@NotNull Player player, @NotNull String plotid) {
        this(player, plotid, Cause.UNKNOWN);
    }

    public Cause getCause() {
        return cause;
    }

    private boolean cn = false;

    @Override
    public boolean isCancelled() {
        return cn;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cn = cancel;
    }

    public ObjectPair<Integer, Integer> getId() {
        return id;
    }

    public String getPlotid() {
        return plotid;
    }

    public Player getPlayer() {
        return player;
    }

    public static enum Cause {
        UNKNOWN, TELEPORT, MOVE;
    }
}
