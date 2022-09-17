package de.emilschlampp.plots.listener;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.listener.events.PlayerEntryPlotEvent;
import de.emilschlampp.plots.listener.events.PlayerExitPlotEvent;
import de.emilschlampp.plots.utils.EComList;
import de.emilschlampp.plots.utils.PlayerQuitClearList;
import de.emilschlampp.plots.utils.math_sys;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

import java.util.*;

public class PlayerListener implements Listener {
    @EventHandler
    public void onInter(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) {
            return;
        }
        if(event.getPlayer().hasPermission("splots.admin")) {
           return;
        }
        Block block = event.getClickedBlock();
        if(!block.getType().isInteractable()) {
            return;
        }
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        if(event.getPlayer().isSneaking()) {
            if(event.getItem() != null) {
                return;
            }
        }
        if(!math_sys.isW(block.getWorld())) {
            return;
        }
        if(math_sys.isroad(block.getLocation())) {
            event.setCancelled(true);
            return;
        }
        String id = math_sys.getPlot(block.getLocation());
        if(!StorageMain.hasOwner(id)) {
            event.setCancelled(true);
            return;
        } else {
            Plot plot = StorageMain.getPlot(id);
            if(!plot.canBuild(event.getPlayer())) {
                if(plot.getFlag("use") != null) {
                    Plot.Flag flag = plot.getFlag("use");
                    if(flag.getValue() != null) {
                        EComList list = new EComList(flag.getValue());
                        if(list.contains(block.getType().getKey().getKey()) || list.contains(block.getType().getKey().getNamespace()+":"+list.contains(block.getType().getKey().getKey()))) {
                            return;
                        }
                    }
                }
                event.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getTo().getBlock().equals(event.getFrom().getBlock())) {
            return;
        }
        if(!math_sys.isW(event.getTo().getWorld())) {
            if(playerlastplot.containsKey(event.getPlayer().getUniqueId())) {
                playerlastplot.remove(event.getPlayer().getUniqueId());
            }
            return;
        }
        if(math_sys.isroad(event.getTo())) {
            if(playerlastplot.containsKey(event.getPlayer().getUniqueId())) {
                PlayerExitPlotEvent event1 = new PlayerExitPlotEvent(event.getPlayer(), playerlastplot.get(event.getPlayer().getUniqueId()),
                        PlayerExitPlotEvent.Cause.MOVE);
                Bukkit.getPluginManager().callEvent(event1);
                if(event1.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }
                playerlastplot.remove(event.getPlayer().getUniqueId());
            }
            return;
        }
        String lastp = playerlastplot.get(event.getPlayer().getUniqueId());
        if(lastp != null) {
            if(lastp.equalsIgnoreCase(math_sys.getPlot(event.getTo()))) {
                return;
            } else {
                PlayerExitPlotEvent event1 = new PlayerExitPlotEvent(event.getPlayer(), playerlastplot.get(event.getPlayer().getUniqueId()),
                        PlayerExitPlotEvent.Cause.MOVE);
                Bukkit.getPluginManager().callEvent(event1);
                if(event1.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
        PlayerEntryPlotEvent event1 = new PlayerEntryPlotEvent(event.getPlayer(), math_sys.getPlot(event.getTo()), PlayerEntryPlotEvent.Cause.MOVE);
        Bukkit.getPluginManager().callEvent(event1);
        if(event1.isCancelled()) {
            event.setCancelled(true);
            if(event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
                if (!math_sys.isroad(event.getFrom())) {
                    if (math_sys.getPlot(event.getFrom()).equals(math_sys.getPlot(event.getTo()))) {
                        Bukkit.getScheduler().runTaskLater(Plots.instance, () -> {
                            event.getPlayer().teleport(event.getTo().getWorld().getSpawnLocation());
                        }, 1);
                    }
                }
            }
            return;
        }
        playerlastplot.put(event.getPlayer().getUniqueId(), math_sys.getPlot(event.getTo()));
    }

    @EventHandler
    public void onTP(PlayerTeleportEvent event) {
        if(event.getTo().getBlock().equals(event.getFrom().getBlock())) {
            return;
        }
        if(!math_sys.isW(event.getTo().getWorld())) {
            if(playerlastplot.containsKey(event.getPlayer().getUniqueId())) {
                playerlastplot.remove(event.getPlayer().getUniqueId());
            }
            return;
        }
        if(math_sys.isroad(event.getTo())) {
            if(playerlastplot.containsKey(event.getPlayer().getUniqueId())) {
                PlayerExitPlotEvent event1 = new PlayerExitPlotEvent(event.getPlayer(), playerlastplot.get(event.getPlayer().getUniqueId()),
                        PlayerExitPlotEvent.Cause.TELEPORT);
                Bukkit.getPluginManager().callEvent(event1);
                if(event1.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }
                playerlastplot.remove(event.getPlayer().getUniqueId());
            }
            return;
        }
        String lastp = playerlastplot.get(event.getPlayer().getUniqueId());
        if(lastp != null) {
            if(lastp.equalsIgnoreCase(math_sys.getPlot(event.getTo()))) {
                return;
            } else {
                PlayerExitPlotEvent event1 = new PlayerExitPlotEvent(event.getPlayer(), playerlastplot.get(event.getPlayer().getUniqueId()),
                        PlayerExitPlotEvent.Cause.MOVE);
                Bukkit.getPluginManager().callEvent(event1);
                if(event1.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
        PlayerEntryPlotEvent event1 = new PlayerEntryPlotEvent(event.getPlayer(), math_sys.getPlot(event.getTo()), PlayerEntryPlotEvent.Cause.TELEPORT);
        Bukkit.getPluginManager().callEvent(event1);
        if(event1.isCancelled()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(event.getTo()+"");
            if(event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
                if (!math_sys.isroad(event.getFrom())) {
                    if (math_sys.getPlot(event.getFrom()).equals(math_sys.getPlot(event.getTo()))) {
                        event.getPlayer().teleport(event.getTo().getWorld().getSpawnLocation());
                    }
                }
            }
            return;
        }
        playerlastplot.put(event.getPlayer().getUniqueId(), math_sys.getPlot(event.getTo()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        while (playerlastplot.containsKey(event.getPlayer().getUniqueId())) {
            playerlastplot.remove(event.getPlayer().getUniqueId());
        }
    }

    private static Map<UUID, String> playerlastplot = new HashMap<>();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(event.getBlock() == null) {
            return;
        }
        if(event.getPlayer().hasPermission("splots.admin")) {
            return;
        }
        Block block = event.getBlock();
        if(!math_sys.isW(block.getWorld())) {
            return;
        }
        if(math_sys.isroad(block.getLocation())) {
            event.setCancelled(true);
            return;
        }
        String id = math_sys.getPlot(block.getLocation());
        if(!StorageMain.hasOwner(id)) {
            event.setCancelled(true);
            return;
        } else {
            Plot plot = StorageMain.getPlot(id);
            if(!plot.canBuild(event.getPlayer())) {
                if(plot.getFlag("break") != null) {
                    Plot.Flag flag = plot.getFlag("break");
                    if(flag.getValue() != null) {
                        EComList list = new EComList(flag.getValue());
                        if(list.contains(block.getType().getKey().getKey()) || list.contains(block.getType().getKey().getNamespace()+":"+list.contains(block.getType().getKey().getKey()))) {
                            return;
                        }
                    }
                }
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(event.getBlock() == null) {
            return;
        }
        if(event.getPlayer().hasPermission("splots.admin")) {
            return;
        }
        Block block = event.getBlock();
        if(!math_sys.isW(block.getWorld())) {
            return;
        }
        if(math_sys.isroad(block.getLocation())) {
            event.setCancelled(true);
            return;
        }
        String id = math_sys.getPlot(block.getLocation());
        if(!StorageMain.hasOwner(id)) {
            event.setCancelled(true);
            return;
        } else {
            Plot plot = StorageMain.getPlot(id);
            if(!plot.canBuild(event.getPlayer())) {
                event.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    public void onWaterPlace(PlayerBucketEmptyEvent event) {
        if(event.getBlock() == null) {
            return;
        }
        if(event.getPlayer().hasPermission("splots.admin")) {
            return;
        }
        Block block = event.getBlock();
        if(!math_sys.isW(block.getWorld())) {
            return;
        }
        if(math_sys.isroad(block.getLocation())) {
            event.setCancelled(true);
            return;
        }
        String id = math_sys.getPlot(block.getLocation());
        if(!StorageMain.hasOwner(id)) {
            event.setCancelled(true);
            return;
        } else {
            Plot plot = StorageMain.getPlot(id);
            if(!plot.canBuild(event.getPlayer())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onWaterBreak(PlayerBucketFillEvent event) {
        if(event.getBlock() == null) {
            return;
        }
        if(event.getPlayer().hasPermission("splots.admin")) {
            return;
        }
        Block block = event.getBlock();
        if(!math_sys.isW(block.getWorld())) {
            return;
        }
        if(math_sys.isroad(block.getLocation())) {
            event.setCancelled(true);
            return;
        }
        String id = math_sys.getPlot(block.getLocation());
        if(!StorageMain.hasOwner(id)) {
            event.setCancelled(true);
            return;
        } else {
            Plot plot = StorageMain.getPlot(id);
            if(!plot.canBuild(event.getPlayer())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onLiquid(BlockFromToEvent event) {
        if(!math_sys.isW(event.getBlock().getWorld())) {
            return;
        }
        if(math_sys.isroad(event.getBlock().getLocation()) != math_sys.isroad(event.getToBlock().getLocation())) {
            event.setCancelled(true);
        }
    }



    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        if(!math_sys.isW(event.getEntity().getWorld())) {
            return;
        }
        if(math_sys.isroad(event.getEntity().getLocation())) {
            if(event.getDamager() instanceof Player) {
                if(event.getDamager().hasPermission("splots.admin")) {
                    return;
                }
            }
            event.setCancelled(true);
        } else {
            String plot = math_sys.getPlot(event.getEntity().getLocation());
            if(StorageMain.hasOwner(plot)) {
                if(event.getEntity() instanceof Player) {
                    if(event.getDamager() instanceof Player) {
                        if(event.getDamager().hasPermission("splots.admin")) {
                            return;
                        }
                        if(!StorageMain.getPlot(plot).isBooleanFlagSet("pvp")) {
                            event.setCancelled(true);
                        }
                    } else {
                        if(!StorageMain.getPlot(plot).isBooleanFlagSet("pve")) {
                            event.setCancelled(true);
                        }
                    }
                } else {
                    if(event.getDamager() instanceof Player) {
                        if(event.getDamager().hasPermission("splots.admin")) {
                            return;
                        }
                    }
                    if(!StorageMain.getPlot(plot).isBooleanFlagSet("pve")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }



    private static List<UUID> timeonplot = new ArrayList<>();


    private static PlayerQuitClearList sendid_title_data = new PlayerQuitClearList(true);

    @EventHandler
    public void onPlotEntry(PlayerEntryPlotEvent event) {
        if (StorageMain.hasOwner(event.getPlotid())) {
            Plot plot = StorageMain.getPlot(event.getPlotid());
            if(plot.isBooleanFlagSet("sendid")) {
                sendid_title_data.add(event.getPlayer());
                event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6Plot §b" + event.getPlotid() + " §6"));
            }
        }
        if(StorageMain.hasOwner(event.getPlotid())) {
            Plot plot = StorageMain.getPlot(event.getPlotid());
            if(plot.getFlag("greeting") != null) {
                if(plot.getFlag("greeting").getValue() != null) {
                    event.getPlayer().sendMessage(PlotSubCommand.PREFIX+"§b"+plot.id()+"§6:");
                    for(String a : plot.getFlag("greeting").getValue().split("\n")) {
                        event.getPlayer().sendMessage(PlotSubCommand.PREFIX + ChatColor.translateAlternateColorCodes('&',a));
                    }
                }
            }
        }

        if(StorageMain.hasOwner(event.getPlotid())) {
            Plot plot = StorageMain.getPlot(event.getPlotid());
            if(plot.getFlag("time") != null) {
                if(plot.getFlag("time").getValue() != null) {
                    Plot.Flag flag = plot.getFlag("time");
                    if(!flag.canVal(flag.getValue())) {
                        if (timeonplot.contains(event.getPlayer().getUniqueId())) {
                            event.getPlayer().resetPlayerTime();
                            while (timeonplot.remove(event.getPlayer().getUniqueId()));
                        }
                        return;
                    }
                    try {
                        event.getPlayer().setPlayerTime(Integer.parseInt(flag.getValue()), false);
                        if(!timeonplot.contains(event.getPlayer().getUniqueId())) {
                            timeonplot.add(event.getPlayer().getUniqueId());
                        }
                    } catch (Exception exception) {

                    }
                } else {

                    if (timeonplot.contains(event.getPlayer().getUniqueId())) {
                        event.getPlayer().resetPlayerTime();
                        while (timeonplot.remove(event.getPlayer().getUniqueId()));
                    }
                }
            } else {
                if (timeonplot.contains(event.getPlayer().getUniqueId())) {
                    event.getPlayer().resetPlayerTime();
                    while (timeonplot.remove(event.getPlayer().getUniqueId()));
                }
            }
        } else {
            if (timeonplot.contains(event.getPlayer().getUniqueId())) {
                event.getPlayer().resetPlayerTime();
                while (timeonplot.remove(event.getPlayer().getUniqueId()));
            }
        }
    }

    @EventHandler
    public void onPlotLeave(PlayerExitPlotEvent event) {
        if(sendid_title_data.contains(event.getPlayer())) {
            sendid_title_data.remove(event.getPlayer());
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6Straße"));
        }


        if(StorageMain.hasOwner(event.getPlotid())) {
            Plot plot = StorageMain.getPlot(event.getPlotid());
            if(plot.getFlag("farewell") != null) {
                if(plot.getFlag("farewell").getValue() != null) {
                    event.getPlayer().sendMessage(PlotSubCommand.PREFIX+"§b"+plot.id()+"§6:");
                    for(String a : plot.getFlag("farewell").getValue().split("\n")) {
                        event.getPlayer().sendMessage(PlotSubCommand.PREFIX + ChatColor.translateAlternateColorCodes('&',a));
                    }
                }
            }
        }

        if (timeonplot.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().resetPlayerTime();
            while (timeonplot.remove(event.getPlayer().getUniqueId()));
        }
    }
}
