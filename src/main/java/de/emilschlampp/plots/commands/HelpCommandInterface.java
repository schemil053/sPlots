package de.emilschlampp.plots.commands;

import org.bukkit.entity.Player;

public interface HelpCommandInterface {
    public String getHelp();

    public default String getHelp(Player player) {
        return getHelp();
    }
}
