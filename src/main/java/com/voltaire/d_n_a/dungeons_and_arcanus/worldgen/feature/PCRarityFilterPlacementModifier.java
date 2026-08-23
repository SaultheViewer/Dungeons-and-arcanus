package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class PCRarityFilterPlacementModifier extends PlacementFilter {
   public static final Codec<PCRarityFilterPlacementModifier> MODIFIER_CODEC;
   private final float chance;

   private PCRarityFilterPlacementModifier(float chance) {
      this.chance = chance;
   }

   public static PCRarityFilterPlacementModifier of(float chance) {
      return new PCRarityFilterPlacementModifier(chance);
   }

   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
      return random.nextFloat() < this.chance;
   }

   public PlacementModifierType<?> type() {
      return PCPlacementModifierType.PC_RARITY.get();
   }

   static {
      MODIFIER_CODEC = ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").xmap(PCRarityFilterPlacementModifier::new, (PCRarityFilterPlacementModifier) -> PCRarityFilterPlacementModifier.chance).codec();
   }
}
