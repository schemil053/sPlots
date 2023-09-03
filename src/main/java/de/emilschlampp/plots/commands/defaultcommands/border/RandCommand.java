package de.emilschlampp.plots.commands.defaultcommands.border;

import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.utils.MathSys;
import de.emilschlampp.plots.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class RandCommand extends PlotSubCommand implements Listener {


    public RandCommand() {
        super("rand", "splots.rand", "border");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    public static Material getRandItem(int slot) {
        if(!RandSaver.yamlConfiguration.isSet("rand."+slot)) {
            return null;
        }
        return Material.valueOf(RandSaver.yamlConfiguration.getString("rand."+slot));
    }

    public static void setRandItem(int slot, Material material) {
        if(material == null) {
            RandSaver.yamlConfiguration.set("rand."+slot, null);
            return;
        }
        RandSaver.yamlConfiguration.set("rand."+slot, material.name());
    }

    public static boolean canUse(int slot, Player player) {
        return player.hasPermission("splots.rand."+slot);
    }

    @EventHandler
    public void onICLick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals(MathSys.generateID(PREFIX+"Ränder"))) {
            return;
        }
        event.setCancelled(true);
        if(event.getRawSlot() >= 54) {
            return;
        }
        Inventory i = event.getView().getTopInventory();
        if(getRandItem(event.getRawSlot()) == null) {
            return;
        }
        if(!canUse(event.getRawSlot(), (Player) event.getWhoClicked())) {
            return;
        }
        setRand((Player) event.getWhoClicked(), getRandItem(event.getRawSlot()));
    }

    private void setRand(Player player, Material material) {
        player.closeInventory();
        if(!isPlotAdmin(player)) {
            return;
        }
        Material material1 = material;
        if(!material.isBlock()) {
            if(RandSaver.translate(material) == null) {
                player.sendMessage(PREFIX + "Dieses Item ist kein Block, deshalb kann der Rand nicht gesetzt werden!");
                return;
            }
            material1 = RandSaver.translate(material);
        }
        if(!material1.isBlock()) {
            player.sendMessage(PREFIX + "Dieses Item ist kein Block, deshalb kann die Wand nicht gesetzt werden!");
            return;
        }
        MathSys.setRand(MathSys.getPlot(player.getLocation()), material1.createBlockData());
    }

    @Override
    public void execute(Player player, String[] args) {
        Inventory inventory = Bukkit.createInventory(null, 54, MathSys.generateID(PREFIX+"Ränder"));
        for(int i = 0; i<inventory.getSize(); i++) {
            if(getRandItem(i) == null) {
                continue;
            }
            if(getRandItem(i).equals(Material.AIR)) {
                inventory.setItem(i, Utils.createGuiItem(Material.BARRIER, "§r§fLuft", canUse(i, player), canUse(i, player)? "§aVerfügbar":"§cNicht verfügbar"));
                continue;
            }
            inventory.setItem(i, Utils.createGuiItem(getRandItem(i), "", canUse(i, player), canUse(i, player)? "§aVerfügbar":"§cNicht verfügbar"));
        }
        player.openInventory(inventory);
    }
}
