package de.emilschlampp.plots.commands.defaultcommands.border;

import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.Utils;
import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static de.emilschlampp.plots.commands.defaultcommands.border.rand_command.getRandItem;

public class editrand_command extends PlotSubCommand implements Listener {
    public editrand_command() {
        super("editrand", "splots.admin");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    @EventHandler
    public void onICLOSE(InventoryCloseEvent event) {
        if(open == null) {
            return;
        }
        if(!open.isOnline()) {
            return;
        }
        if(!open.getUniqueId().equals(event.getPlayer().getUniqueId())) {
            return;
        }
        if(!event.getView().getTitle().equals(math_sys.generateID(PREFIX+"Ränder bearbeiten"))) {
            return;
        }
        Inventory inventory = event.getView().getTopInventory();
        for(int i = 0; i<inventory.getSize(); i++) {
            if(inventory.getItem(i) == null) {
                rand_command.setRandItem(i, null);
                continue;
            }
            if(inventory.getItem(i).getType().isAir()) {
                rand_command.setRandItem(i, null);
                continue;
            }
            if(inventory.getItem(i).getType().equals(Material.BARRIER)) {
                if(inventory.getItem(i).getItemMeta().hasDisplayName()) {
                    if(inventory.getItem(i).getItemMeta().getDisplayName().equals("air") || inventory.getItem(i).getItemMeta().getDisplayName().contains("§fLuft")) {
                        rand_command.setRandItem(i, Material.AIR);
                        continue;
                    }
                }
            }
            rand_command.setRandItem(i, inventory.getItem(i).getType());
        }
        open = null;
        Rand_Saver.save();
    }

    private Player open = null;

    @Override
    public void execute(Player player, String[] args) {
        if(open != null) {
            if(open.isOnline()) {
                player.sendMessage(PREFIX + "Das Inventar ist derzeit offen!");
                return;
            }
        }
        Inventory inventory = Bukkit.createInventory(null, 54, math_sys.generateID(PREFIX+"Ränder bearbeiten"));
        for(int i = 0; i<inventory.getSize(); i++) {
            if(getRandItem(i) == null) {
                continue;
            }
            if(getRandItem(i).equals(Material.AIR)) {
                inventory.setItem(i, Utils.createGuiItem(Material.BARRIER, "§r§fLuft", true));
                continue;
            }
            inventory.setItem(i, Utils.createGuiItem(getRandItem(i), "", true, "§a§lPermission: §c"+"splots.rand."+i));
        }
        player.openInventory(inventory);
        open = player;
    }
}
