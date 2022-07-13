package de.emilschlampp.plots.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.function.Consumer;

public class BlockSetter {
    public static int rep(BlockData data, Material from, Location a, Location b) {
        int bs = 0;
        World world = a.getWorld();

        Integer aY = a.getBlockY();
        Integer aX = a.getBlockX();
        Integer aZ = a.getBlockZ();

        Integer bY = b.getBlockY();
        Integer bX = b.getBlockX();
        Integer bZ = b.getBlockZ();


        if(aY > bY) {
            bY = aY;
            aY = b.getBlockY();
        }

        if(aX > bX) {
            bX = aX;
            aX = b.getBlockX();
        }
        if(aZ > bZ) {
            bZ = aZ;
            aZ = b.getBlockZ();
        }
        for(Integer y = aY; y <= bY; y++) {
            for(Integer x = aX; x <= bX; x++) {
                for(Integer z = aZ; z <= bZ; z++) {
                    Location locBlock = new Location(world, x, y, z);
                    locBlock.getChunk().load();
                    if(from != null) {
                        if (locBlock.getBlock().getType().equals(from)) {
                            locBlock.getBlock().setBlockData(data, false);
                            bs++;
                        }
                    } else {
                        locBlock.getBlock().setBlockData(data, false);
                        bs++;
                    }
                }
            }
        }
        return bs;
    }

    public static int rep(Material to, Material from, Location a, Location b) {
        int bs = 0;
        World world = a.getWorld();

        Integer aY = a.getBlockY();
        Integer aX = a.getBlockX();
        Integer aZ = a.getBlockZ();

        Integer bY = b.getBlockY();
        Integer bX = b.getBlockX();
        Integer bZ = b.getBlockZ();


        if(aY > bY) {
            bY = aY;
            aY = b.getBlockY();
        }

        if(aX > bX) {
            bX = aX;
            aX = b.getBlockX();
        }
        if(aZ > bZ) {
            bZ = aZ;
            aZ = b.getBlockZ();
        }
        for(Integer y = aY; y <= bY; y++) {
            for(Integer x = aX; x <= bX; x++) {
                for(Integer z = aZ; z <= bZ; z++) {
                    Location locBlock = new Location(world, x, y, z);
                    locBlock.getChunk().load();
                    if(from != null) {
                        if (locBlock.getBlock().getType().equals(from)) {
                            locBlock.getBlock().setType(to, false);
                            bs++;
                        }
                    } else {
                        locBlock.getBlock().setType(to, false);
                        bs++;
                    }
                }
            }
        }
        return bs;
    }

    public static void runLocTask(Location a, Location b, Consumer<Location> task) {
        World world = a.getWorld();

        Integer aY = a.getBlockY();
        Integer aX = a.getBlockX();
        Integer aZ = a.getBlockZ();

        Integer bY = b.getBlockY();
        Integer bX = b.getBlockX();
        Integer bZ = b.getBlockZ();


        if(aY > bY) {
            bY = aY;
            aY = b.getBlockY();
        }

        if(aX > bX) {
            bX = aX;
            aX = b.getBlockX();
        }
        if(aZ > bZ) {
            bZ = aZ;
            aZ = b.getBlockZ();
        }
        for(Integer y = aY; y <= bY; y++) {
            for(Integer x = aX; x <= bX; x++) {
                for(Integer z = aZ; z <= bZ; z++) {
                    Location locBlock = new Location(world, x, y, z);
                    locBlock.getChunk().load();
                    task.accept(locBlock);
                }
            }
        }
    }

    public static void set(Location a, Location b, BlockData data) {
        rep(data, null, a, b);
    }

    public static void set(Location a, Location b, Material data) {
        rep(data, null, a, b);
    }
}
