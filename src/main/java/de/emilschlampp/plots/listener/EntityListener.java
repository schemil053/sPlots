package de.emilschlampp.plots.listener;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.storage.Plot;
import de.emilschlampp.plots.storage.StorageMain;
import de.emilschlampp.plots.utils.MathSys;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.Arrays;
import java.util.List;

public class EntityListener implements Listener {
    @EventHandler
    public void onEEE(EntityExplodeEvent event) {
        if(!MathSys.isW(event.getLocation().getWorld())) {
            return;
        }
        removeBlocks(event.blockList());
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if(!MathSys.isW(event.getBlock().getWorld())) {
            return;
        }
        if(MathSys.isroad(event.getBlock().getLocation())) {
            return;
        }
        for(Block block : event.getBlocks()) {
            if(MathSys.isroad(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if(!MathSys.isW(event.getBlock().getWorld())) {
            return;
        }
        if(MathSys.isroad(event.getBlock().getLocation())) {
            return;
        }
        for(Block block : event.getBlocks()) {
            if(MathSys.isroad(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBEE(BlockExplodeEvent event) {
        if(!MathSys.isW(event.getBlock().getLocation().getWorld())) {
            return;
        }
        removeBlocks(event.blockList());
    }

    public static final List<EntityType> whitelist = Arrays.asList(
            EntityType.PLAYER,
            EntityType.DROPPED_ITEM,
            EntityType.FIREWORK
    );

    @EventHandler
    public void onLighting(LightningStrikeEvent event) {
        if(!MathSys.isW(event.getWorld())) {
            return;
        }
        if(MathSys.isroad(event.getLightning().getLocation())) {
            event.setCancelled(true);
        }
        if(!StorageMain.hasOwner(MathSys.getPlot(event.getLightning().getLocation()))) {
            event.setCancelled(true);
            return;
        }
        if(!StorageMain.getPlot(MathSys.getPlot(event.getLightning().getLocation())).isBooleanFlagSet("lightningstrike")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if(whitelist.contains(event.getVehicle().getType())) {
            return;
        }
        if(!MathSys.isW(event.getTo().getWorld())) {
            return;
        }
        if(event.getTo().getBlock().equals(event.getFrom().getBlock())) {
            return;
        }
        if(MathSys.isroad(event.getFrom()) && MathSys.isroad(event.getTo())) {
            return;
        }
        if(MathSys.isroad(event.getTo()) && (!MathSys.isroad(event.getFrom()))) {
            Bukkit.getScheduler().runTaskLater(Plots.instance, () -> {
                event.getVehicle().teleport(event.getFrom());
            }, 1);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if(!MathSys.isW(event.getLocation().getWorld())) {
            return;
        }
        if(whitelist.contains(event.getEntityType())) {
            return;
        }
        if(MathSys.isroad(event.getLocation())) {
            event.setCancelled(true);
            return;
        }
        if(!StorageMain.hasOwner(MathSys.getPlot(event.getLocation()))) {
            event.setCancelled(true);
            return;
        }
        if(event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            if(!StorageMain.getPlot(MathSys.getPlot(event.getLocation())).isBooleanFlagSet("mobspawn")) {
                event.setCancelled(true);
                return;
            }
        }
    }



    private static void removeBlocks(List<Block> bl) {
        bl.removeIf( p -> {
            boolean a = false;
            if(!MathSys.isroad(p.getLocation())) {
                if (!StorageMain.hasOwner(MathSys.getPlot(p.getLocation()))) {
                    a = true;
                } else {
                    Plot plot = StorageMain.getPlot(MathSys.getPlot(p.getLocation()));
                    boolean ffound = false;
                    for (Plot.Flag flag : plot.getFlags()) {
                        if(flag.getName() == null) {
                            continue;
                        }
                        if(flag.getName().equals("explosion")) {
                            if(flag.getValue() == null) {
                                continue;
                            }
                            if(flag.getValue().equals("true")) {
                                ffound = true;
                            }
                        }
                    }
                    if(!ffound) {
                        a = true;
                    }
                }
            }
            boolean c = MathSys.isroad(p.getLocation()) || a;
            //   Bukkit.broadcastMessage(p.getLocation().toString()+"   "+c);
            return c;
        });
    }


    @EventHandler
    public void onEntStreetMove(EntityMoveEvent event) {
        if(whitelist.contains(event.getEntityType())) {
            return;
        }
        if(!MathSys.isW(event.getTo().getWorld())) {
            return;
        }
        if(event.getTo().getBlock().equals(event.getFrom().getBlock())) {
            return;
        }
        if(MathSys.isroad(event.getFrom()) && MathSys.isroad(event.getTo())) {
            return;
        }
        if(MathSys.isroad(event.getTo()) && (!MathSys.isroad(event.getFrom()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntStreetTP(EntityTeleportEvent event) {
        if(whitelist.contains(event.getEntityType())) {
            return;
        }
        if(!MathSys.isW(event.getTo().getWorld())) {
            return;
        }
        if(event.getTo().getBlock().equals(event.getFrom().getBlock())) {
            return;
        }
        if(MathSys.isroad(event.getFrom()) && MathSys.isroad(event.getTo())) {
            return;
        }
        if(MathSys.isroad(event.getTo()) && (!MathSys.isroad(event.getFrom()))) {
            event.setCancelled(true);
        }
    }
}
