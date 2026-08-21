package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.utils.PCConfig;
import org.cloudwarp.probablychests.worldgen.feature.PCFeatures;

public class UndergroundChestGeneration {
   public static void generateChest() {
      PCConfig config = ProbablyChests.loadedConfig;
      if (config != null) {
         if (!(config.worldGen.chestSpawnChance <= 0.0F)) {
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), Decoration.UNDERGROUND_DECORATION, PCFeatures.UNDERGROUND_CHEST_PLACED_KEY);
            BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), Decoration.UNDERGROUND_DECORATION, PCFeatures.NETHER_CHEST_PLACED_KEY);
         }
      }
   }
}
