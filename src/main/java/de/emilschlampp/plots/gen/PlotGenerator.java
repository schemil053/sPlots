package de.emilschlampp.plots.gen;

import de.emilschlampp.plots.utils.MathSys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.Random;

public class PlotGenerator extends ChunkGenerator {
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = Bukkit.createChunkData(world);

        for (int xX = 0; xX < 16; xX++) {
            for (int zZ = 0; zZ < 16; zZ++) {

                for (int i = 0; i < world.getMaxHeight(); i++) {
                    biome.setBiome(xX, i, zZ, Biome.PLAINS);
                }

                int x = chunkX * 16 + xX;
                int z = chunkZ * 16 + zZ;
                Material m;
                if (MathSys.isroad(x, z)) {
                    for (int i = MathSys.pheight; i >= 0; i--) {
                        chunk.setBlock(xX, i, zZ, MathSys.STREET);
                    }
                    for (int xa : Arrays.asList(1, 0, -1)) {
                        for (int za : Arrays.asList(1, 0, -1)) {
                            if (!MathSys.isroad(x + xa, z + za)) {
                                chunk.setBlock(xX, MathSys.pheight + 1, zZ, MathSys.BORDERBLOCK);
                                for (int i = MathSys.pheight; i >= 0; i--) {
                                    chunk.setBlock(xX, i, zZ, MathSys.WALLBLOCK);
                                }
                            }
                        }
                    }
                } else {
                    for (int i = MathSys.pheight; i >= 0; i--) {
                        chunk.setBlock(xX, i, zZ, MathSys.PLOTFLOOR);
                    }
                    chunk.setBlock(xX, MathSys.pheight, zZ, MathSys.PLOTFLOORTOP);
                }

                chunk.setBlock(xX, 0, zZ, Material.BEDROCK);

            }
        }
        return chunk;
    }
}

