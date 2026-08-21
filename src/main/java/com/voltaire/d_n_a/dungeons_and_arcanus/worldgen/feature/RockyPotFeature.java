package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.cloudwarp.probablychests.registry.PCBlocks;

public class RockyPotFeature extends Feature<PCPotFeatureConfig> {
   public RockyPotFeature(Codec<PCPotFeatureConfig> configCodec) {
      super(configCodec);
   }

   public boolean place(FeaturePlaceContext<PCPotFeatureConfig> context) {
      RandomSource random = context.random();
      WorldGenLevel structureWorldAccess = context.level();
      BlockPos pos = context.origin();
      PCPotFeatureConfig config = (PCPotFeatureConfig)context.config();
      structureWorldAccess.setBlock(pos, PCBlocks.ROCKY_POT.defaultBlockState(), 3);
      return true;
   }
}
