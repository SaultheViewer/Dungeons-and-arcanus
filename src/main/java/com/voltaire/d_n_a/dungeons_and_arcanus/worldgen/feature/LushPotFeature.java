package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class LushPotFeature extends Feature<PCPotFeatureConfig> {
   public LushPotFeature(Codec<PCPotFeatureConfig> configCodec) {
      super(configCodec);
   }

   public boolean place(FeaturePlaceContext<PCPotFeatureConfig> context) {
      RandomSource random = context.random();
      WorldGenLevel structureWorldAccess = context.level();
      BlockPos pos = context.origin();
      PCPotFeatureConfig config = (PCPotFeatureConfig)context.config();
      structureWorldAccess.setBlock(pos, ModBlocks.LUSH_POT.get().defaultBlockState(), 3);
      return true;
   }
}
