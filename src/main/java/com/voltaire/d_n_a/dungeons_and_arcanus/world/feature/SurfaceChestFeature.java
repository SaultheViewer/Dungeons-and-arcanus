package com.voltaire.d_n_a.dungeons_and_arcanus.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SurfaceChestFeature extends Feature<NoneFeatureConfiguration> {
   public SurfaceChestFeature(Codec<NoneFeatureConfiguration> configCodec) {
      super(configCodec);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      return true;
   }
}
