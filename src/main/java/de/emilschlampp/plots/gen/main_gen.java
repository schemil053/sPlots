package de.emilschlampp.plots.gen;

import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class main_gen extends ChunkGenerator {
  @Override
  public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
      ChunkData chunk = Bukkit.createChunkData(world);

      for (int xX = 0; xX < 16; xX++) {
          for (int zZ = 0; zZ < 16; zZ++) {

              for(int i = 0; i<world.getMaxHeight(); i++) {
                  biome.setBiome(xX, i, zZ, Biome.PLAINS);
              }

              int x = chunkX*16+xX;
              int z = chunkZ*16+zZ;
              Material m;
              if(math_sys.isroad(x, z)) {
                  for(int i = math_sys.pheight; i>=0; i--) {
                      chunk.setBlock(xX, i, zZ, math_sys.STREET);
                  }
                  for(int xa : Arrays.asList(1, 0, -1)) {
                      for(int za : Arrays.asList(1, 0, -1)) {
                          if(!math_sys.isroad(x+xa, z+za)) {
                              chunk.setBlock(xX, math_sys.pheight+1, zZ, math_sys.BORDERBLOCK);
                              for(int i = math_sys.pheight; i>=0; i--) {
                                  chunk.setBlock(xX, i, zZ, math_sys.WALLBLOCK);
                              }
                          }
                      }
                  }
              } else {
                  for(int i = math_sys.pheight; i>=0; i--) {
                      chunk.setBlock(xX, i, zZ, math_sys.PLOTFLOOR);
                  }
                  chunk.setBlock(xX, math_sys.pheight, zZ, math_sys.PLOTFLOORTOP);
              }

              chunk.setBlock(xX, 0, zZ, Material.BEDROCK);

          }
      }
      return chunk;
  }
}

