package de.emilschlampp.plots.commands.defaultcommands.systemcommands;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.commands.HelpCommandInterface;
import de.emilschlampp.plots.commands.PlotSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ConfirmCommand extends PlotSubCommand implements HelpCommandInterface {
    public ConfirmCommand() {
        super("confirm", "splots.confirm");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    private static Map<UUID, Runnable> map = new HashMap<>();
    private static Map<UUID, BukkitTask> map1 = new HashMap<>();

    @Override
    public void execute(Player player, String[] args) {
        if(!map.containsKey(player.getUniqueId())) {
            player.sendMessage(PREFIX+"Du hast nichts zu bestätigen!");
            return;
        }
        map.get(player.getUniqueId()).run();
        map.remove(player.getUniqueId());

        if(map1.containsKey(player.getUniqueId())) {
            if(!map1.get(player.getUniqueId()).isCancelled()) {
                map1.get(player.getUniqueId()).cancel();
            }
        }
    }

    public static void confirm(Player player, Runnable task) {
        if(!ToggleCommand.isValue(player.getUniqueId(), "confirm")) {
            task.run();
            return;
        }

        player.sendMessage(PREFIX+"Tippe in den nächsten 30 Sekunden §b/plot confirm §6zum bestätigen!");
        BukkitTask t = Bukkit.getScheduler().runTaskLaterAsynchronously(Plots.instance, () -> {
            if(map.containsKey(player.getUniqueId())) {
                map.remove(player.getUniqueId());
                player.sendMessage(PREFIX+"Zeit abgelaufen. Versuche es erneut!");
            }
        }, 30*20);
        map1.put(player.getUniqueId(), t);
        map.put(player.getUniqueId(), task);
    }

    @Override
    public String getHelp() {
        return "Bestätigt die Aktion, lässt sich mit /plot toggle confirm deaktivieren.";
    }
}
