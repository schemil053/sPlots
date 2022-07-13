package de.emilschlampp.plots.utils;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class math_sys {


    public static int pw = 50;
    public static int pheight = 80;
    public static int rw = 7;
    public static String plotworld = "plots";

    public static Material PLOTFLOORTOP = Material.GRASS_BLOCK;
    public static Material PLOTFLOOR = Material.DIRT;

    public static Material STREET = Material.STONE;

    public static Material WALLBLOCK = Material.STONE;
    public static Material WALLBLOCKCLAIMED = Material.STONE;

    public static Material BORDERBLOCK = Material.STONE_SLAB;
    public static Material BORDERBLOCKCLAIMED = Material.BRICK_SLAB;





    public static boolean isroad(Location location) {
        return getPlot(location).equalsIgnoreCase("road");
    }

    public static boolean isroad(int x, int z) {
        return getPlot(x, z).equalsIgnoreCase("road");
    }

    public static Location getLoc(String id) {
        String[] ss = id.split(";");

        try {
            Integer.parseInt(ss[0]);
            Integer.parseInt(ss[1]);
        } catch (Exception e) {
            return null;
        }
        int x = Integer.parseInt(ss[0])*(pw+rw)+rw/2;
        int z = Integer.parseInt(ss[1])*(pw+rw)+rw/2;

        if (rw % 2 == 0) {
            x = x-1;
            z = z-1;
        }

        return new Location(Bukkit.getWorld(plotworld), x, pheight, z);
    }

    public static void setRand(String id, BlockData rand) {
        Location location = getLoc(id);
        location.add(0, 1, 0);
        BlockSetter.set(location, location.clone().add(0,0,pw+1), rand);
        BlockSetter.set(location.clone().add(0,0,pw+1), location.clone().add(pw+1,0,pw+1), rand);
        BlockSetter.set(location, location.clone().add(pw+1,0,0), rand);
        BlockSetter.set(location.clone().add(pw+1,0,0), location.clone().add(pw+1,0,pw+1), rand);
    }

    public static void setWall(String id, BlockData rand) {
        Location location = getLoc(id);
        Location b = getLoc(id);
        b.setY(1);
        BlockSetter.set(b, location.clone().add(0,0,pw+1), rand);
        BlockSetter.set(b.clone().add(0,0,pw+1), location.clone().add(pw+1,0,pw+1), rand);
        BlockSetter.set(b, location.clone().add(pw+1,0,0), rand);
        BlockSetter.set(b.clone().add(pw+1,0,0), location.clone().add(pw+1,0,pw+1), rand);
    }


    public static String getPlot(Location loc) {
        int valx = loc.getBlockX();
        int valz = loc.getBlockZ();
        return getPlot(valx, valz);
    }

    public static Location getTeleportLocation(String id) {
        Location a = getLoc(id);
        a.add(pw/2, 2, 0);
        a.add(0.5, 0, 0.5);
        return a;
    }

    public static Location getMiddleLocation(String id) {
        Location a = getLoc(id);
        a.add(pw/2, 1, pw/2);
        return a;
    }

    public static ObjectPair<Integer, Integer> getPlot(String id) {
        String[] ss = id.split(";");

        try {
            Integer.parseInt(ss[0]);
            Integer.parseInt(ss[1]);
        } catch (Exception e) {
            return null;
        }
        return new ObjectPair<>(Integer.parseInt(ss[0]),
        Integer.parseInt(ss[1]));
    }


    public static boolean isPlotID(String id) {
        String[] ss = id.split(";");

        try {
            Integer.parseInt(ss[0]);
            Integer.parseInt(ss[1]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String generateID(String a) {
        String b = "";
        for(byte ba : a.getBytes()) {
            b = b+ba;
        }
        while (b.length()/2 > 5) {
            b = b.substring(0, b.length()/2);
        }
        return a+" §7#§k"+ b;
    }

    public static boolean isW(World world) {
        return world.getName().equalsIgnoreCase(plotworld);
    }

    public static void clearPlot(String id, boolean rand) {
        Location a = getLoc(id).add(1,0,1);
        Location b = a.clone().add(pw-1, 0, pw-1);
        b.setY(pheight);
        a.setY(0);
        removeEntities(id);
        BlockSetter.set(a,b, Bukkit.createBlockData(math_sys.PLOTFLOOR));
        a.setY(pheight);
        b.setY(pheight);
        BlockSetter.set(a, b, Bukkit.createBlockData(math_sys.PLOTFLOORTOP));
        a.setY(a.getWorld().getMaxHeight()-1);
        b.setY(pheight+1);
        BlockSetter.set(a, b, Bukkit.createBlockData(Material.AIR));
        a.setY(0);
        b.setY(0);
        BlockSetter.set(a, b, Material.BEDROCK);
        removeEntities(id);
        if(rand) {
            setRand(id, Bukkit.createBlockData(math_sys.BORDERBLOCK));
            setWall(id, Bukkit.createBlockData(math_sys.WALLBLOCK));
        }
    }

    public static void clearPlot(String id) {
        clearPlot(id, true);
    }

    public static void removeEntities(String id) {
        Location a = getLoc(id).add(1,0,1);
        Location b = a.clone().add(pw-1, 0, pw-1);
        List<Chunk> l = new ArrayList<>();
        BlockSetter.runLocTask(a, b, location -> {
            if(!l.contains(location.getChunk())) {
                l.add(location.getChunk());
            }
        });

        for (Chunk chunk : l) {
            for (Entity entity : chunk.getEntities()) {
                if(entity.getType().equals(EntityType.PLAYER)) {
                    continue;
                }
                if(getPlot(entity.getLocation()).equalsIgnoreCase(id)) {
                    try {
                        entity.remove();
                    } catch (Exception exception) {

                    }
                }
            }
        }
    }

    public static String getPlot(int xa, int za) {
        double n3;
        int valx = xa;
        int valz = za;
        int size = pw + rw;
        int pathsize = rw;
        boolean road = false;
        int mod2 = 0;
        int mod1 = 1;
        int x = (int)Math.ceil(valx / size);
        int z = (int)Math.ceil(valz / size);
        if (pathsize % 2 == 1) {
            n3 = Math.ceil(pathsize / 2.0D);
            mod2 = -1;
        } else {
            n3 = Math.floor(pathsize / 2.0D);
        }
        for (double i = n3; i >= 0.0D; i--) {
            if ((valx - i + mod1) % size == 0.0D || (valx + i + mod2) % size == 0.0D) {
                road = true;
                x = (int)Math.ceil((valx - n3) / size);
            }
            if ((valz - i + mod1) % size == 0.0D || (valz + i + mod2) % size == 0.0D) {
                road = true;
                z = (int)Math.ceil((valz - n3) / size);
            }
        }
        if (road)
            return "road";
        if(xa < 0) {
            x = x-1;
        }
        if(za < 0) {
            z = z-1;
        }
        return x + ";" + z;
    }

}
