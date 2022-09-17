package de.emilschlampp.plots.utils;

import de.emilschlampp.plots.Plots;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.io.*;

public class BlockSaver {
    public static void paste(String name, Location location) {
        File file = new File(Plots.instance.getDataFolder(), "backups");
        file.mkdirs();
        file = new File(file, name+".ecopy");
        if(!file.exists()) {
            return;
        }
        try {
            String strng;
            BufferedReader obj = new BufferedReader(new FileReader(file));
            while ((strng = obj.readLine()) != null) {
                String[] d = strng.split(";hg;");
                if(d.length < 5) {
                    continue;
                }
                Integer x = Integer.parseInt(d[0])+location.getBlockX();
                Integer y = Integer.parseInt(d[1])+location.getBlockY();
                Integer z = Integer.parseInt(d[2])+location.getBlockZ();
                Material m = Material.valueOf(d[3]);
                BlockData data = Bukkit.createBlockData(d[4]);

                World w = location.getWorld();
     //           w.getBlockAt(x, y, z).setType(Material.AIR, false);
     //           w.getBlockAt(x, y, z).setType(m, false);
                w.getBlockAt(x, y, z).setBlockData(data, false);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(Location a, Location b, String fname, Location middle) {
        File file = new File(Plots.instance.getDataFolder(), "backups");
        file.mkdirs();
        file = new File(file, fname+".ecopy");
        if(file.exists()) {
            file.delete();
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        BufferedWriter w = new BufferedWriter(fileWriter);


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
                    Integer xb = x-middle.getBlockX();
                    Integer yb = y-middle.getBlockY();
                    Integer zb = z-middle.getBlockZ();

                    String data = "";
                    data = xb+";hg;"+yb+";hg;"+zb+";hg;"+locBlock.getBlock().getType()
                            +";hg;"+locBlock.getBlock().getBlockData().getAsString();
                    try {
                        w.write(data);
                        w.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
