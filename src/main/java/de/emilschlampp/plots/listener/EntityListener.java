package de.emilschlampp.plots.listener;

import de.emilschlampp.plots.Storage.Plot;
import de.emilschlampp.plots.Storage.StorageMain;
import de.emilschlampp.plots.utils.math_sys;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.Arrays;
import java.util.List;

public class EntityListener implements Listener {
    @EventHandler
    public void onEEE(EntityExplodeEvent event) {
        if(!math_sys.isW(event.getLocation().getWorld())) {
            return;
        }
        removeBlocks(event.blockList());
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if(!math_sys.isW(event.getBlock().getWorld())) {
            return;
        }
        if(math_sys.isroad(event.getBlock().getLocation())) {
            return;
        }
        for(Block block : event.getBlocks()) {
            if(math_sys.isroad(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if(!math_sys.isW(event.getBlock().getWorld())) {
            return;
        }
        if(math_sys.isroad(event.getBlock().getLocation())) {
            return;
        }
        for(Block block : event.getBlocks()) {
            if(math_sys.isroad(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBEE(BlockExplodeEvent event) {
        if(!math_sys.isW(event.getBlock().getLocation().getWorld())) {
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
        if(!math_sys.isW(event.getWorld())) {
            return;
        }
        if(math_sys.isroad(event.getLightning().getLocation())) {
            event.setCancelled(true);
        }
        if(!StorageMain.hasOwner(math_sys.getPlot(event.getLightning().getLocation()))) {
            event.setCancelled(true);
            return;
        }
        if(!StorageMain.getPlot(math_sys.getPlot(event.getLightning().getLocation())).isBooleanFlagSet("lightningstrike")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if(!math_sys.isW(event.getLocation().getWorld())) {
            return;
        }
        if(whitelist.contains(event.getEntityType())) {
            return;
        }
        if(math_sys.isroad(event.getLocation())) {
            event.setCancelled(true);
            return;
        }
        if(!StorageMain.hasOwner(math_sys.getPlot(event.getLocation()))) {
            event.setCancelled(true);
            return;
        }
        if(event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            if(!StorageMain.getPlot(math_sys.getPlot(event.getLocation())).isBooleanFlagSet("mobspawn")) {
                event.setCancelled(true);
                return;
            }
        }
    }



    private static void removeBlocks(List<Block> bl) {
        bl.removeIf( p -> {
            boolean a = false;
            if(!math_sys.isroad(p.getLocation())) {
                if (!StorageMain.hasOwner(math_sys.getPlot(p.getLocation()))) {
                    a = true;
                } else {
                    Plot plot = StorageMain.getPlot(math_sys.getPlot(p.getLocation()));
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
            boolean c = math_sys.isroad(p.getLocation()) || a;
            //   Bukkit.broadcastMessage(p.getLocation().toString()+"   "+c);
            return c;
        });
    }


    @EventHandler
    public void onEntStreetMove(EntityMoveEvent event) {
        if(whitelist.contains(event.getEntityType())) {
            return;
        }
        if(!math_sys.isW(event.getTo().getWorld())) {
            return;
        }
        if(event.getTo().getBlock().equals(event.getFrom().getBlock())) {
            return;
        }
        if(math_sys.isroad(event.getFrom()) && math_sys.isroad(event.getTo())) {
            return;
        }
        if(math_sys.isroad(event.getTo()) && (!math_sys.isroad(event.getFrom()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntStreetTP(EntityTeleportEvent event) {
        if(whitelist.contains(event.getEntityType())) {
            return;
        }
        if(!math_sys.isW(event.getTo().getWorld())) {
            return;
        }
        if(event.getTo().getBlock().equals(event.getFrom().getBlock())) {
            return;
        }
        if(math_sys.isroad(event.getFrom()) && math_sys.isroad(event.getTo())) {
            return;
        }
        if(math_sys.isroad(event.getTo()) && (!math_sys.isroad(event.getFrom()))) {
            event.setCancelled(true);
        }
    }
}
