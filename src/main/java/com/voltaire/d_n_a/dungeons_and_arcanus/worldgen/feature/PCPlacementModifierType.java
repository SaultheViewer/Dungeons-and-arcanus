package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;


public class PCPlacementModifierType<P extends PlacementModifier> {
   public static PlacementModifierType<PCGroundPlacementModifier> CHEST_SCAN;
   public static PlacementModifierType<PCRarityFilterPlacementModifier> PC_RARITY;
   public static PlacementModifierType<PCSolidGroundPlacementModifier> SOLID_CHECK;

   private static <P extends PlacementModifier> PlacementModifierType<P> register(ResourceLocation id, Codec<P> codec) {
      return (PlacementModifierType)Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, id, (PlacementModifierType)() -> codec);
   }

   public static void init() {
      CHEST_SCAN = register(Dungeons_and_arcanus.id("chest_scan"), PCGroundPlacementModifier.MODIFIER_CODEC);
      PC_RARITY = register(Dungeons_and_arcanus.id("pc_rarity"), PCRarityFilterPlacementModifier.MODIFIER_CODEC);
      SOLID_CHECK = register(Dungeons_and_arcanus.id("sold_check"), PCSolidGroundPlacementModifier.MODIFIER_CODEC);
   }
}
