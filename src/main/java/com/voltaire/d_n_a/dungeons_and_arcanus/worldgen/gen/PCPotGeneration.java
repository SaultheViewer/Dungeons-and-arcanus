package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.worldgen.feature.PCFeatures;

import java.util.List;

public class PCPotGeneration {
   public static void generatePot() {
      PCConfig config = ProbablyChests.loadedConfig;
      if (config != null) {
         if (!(config.worldGen.potSpawnChance <= 0.0F)) {
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.excludeByKey(List.of(Biomes.DRIPSTONE_CAVES, Biomes.OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.FROZEN_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN))).and((context) -> {
               Biome biome = context.getBiome();
               float temp = biome.getBaseTemperature();
               return temp >= 0.5F && temp < 1.0F;
            }), Decoration.VEGETAL_DECORATION, PCFeatures.LUSH_POT_PLACED_KEY);
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.excludeByKey(List.of(Biomes.LUSH_CAVES, Biomes.DRIPSTONE_CAVES))).and((context) -> {
               Biome biome = context.getBiome();
               float temp = biome.getBaseTemperature();
               float downfall = biome.getBaseTemperature();
               return temp <= 0.5F || temp == 0.8F && downfall == 0.4F;
            }), Decoration.VEGETAL_DECORATION, PCFeatures.NORMAL_POT_PLACED_KEY);
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.excludeByKey(List.of(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.BEACH))).and((context) -> {
               Biome biome = context.getBiome();
               float temp = biome.getBaseTemperature();
               float downfall = biome.getBaseTemperature();
               return temp >= 1.0F || temp == 0.8F && downfall == 0.4F;
            }), Decoration.VEGETAL_DECORATION, PCFeatures.ROCKY_POT_PLACED_KEY);
            BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), Decoration.UNDERGROUND_DECORATION, PCFeatures.NETHER_POT_PLACED_KEY);
         }
      }
   }
}
