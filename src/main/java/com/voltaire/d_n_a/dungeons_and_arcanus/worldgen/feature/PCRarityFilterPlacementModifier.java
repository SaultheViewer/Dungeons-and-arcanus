package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class PCRarityFilterPlacementModifier extends PlacementFilter {

   public enum Type {
      POT,
      SURFACE_CHEST,
      UNDERGROUND_CHEST,
      NETHER_CHEST
   }

   public static final Codec<PCRarityFilterPlacementModifier> MODIFIER_CODEC =
           RecordCodecBuilder.create(instance ->
                   instance.group(
                           Codec.STRING.fieldOf("kind").forGetter(m -> m.type.name().toLowerCase())
                   ).apply(instance, s -> new PCRarityFilterPlacementModifier(Type.valueOf(s.toUpperCase())))
           );

   private final Type type;

   private PCRarityFilterPlacementModifier(Type type) {
      this.type = type;
   }

   public static PCRarityFilterPlacementModifier of(Type type) {
      return new PCRarityFilterPlacementModifier(type);
   }

   @Override
   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
      float chance = switch (this.type) {
         case POT -> Dungeons_and_arcanus.loadedConfig.worldGen.potSpawnChance;
         case SURFACE_CHEST -> Dungeons_and_arcanus.loadedConfig.worldGen.chestSpawnChance * 0.02F;
         case UNDERGROUND_CHEST -> Dungeons_and_arcanus.loadedConfig.worldGen.chestSpawnChance * 0.85F;
         case NETHER_CHEST -> Dungeons_and_arcanus.loadedConfig.worldGen.chestSpawnChance * 0.75F;
      };
      return random.nextFloat() < chance;
   }

   @Override
   public PlacementModifierType<?> type() {
      return PCPlacementModifierType.PC_RARITY.get();
   }
}