package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCPlacementModifierType<P extends PlacementModifier> {
   public static PlacementModifierType<PCGroundPlacementModifier> CHEST_SCAN;
   public static PlacementModifierType<PCRarityFilterPlacementModifier> PC_RARITY;
   public static PlacementModifierType<PCSolidGroundPlacementModifier> SOLID_CHECK;

   private static <P extends PlacementModifier> PlacementModifierType<P> register(ResourceLocation id, Codec<P> codec) {
      return (PlacementModifierType)Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, id, (PlacementModifierType)() -> codec);
   }

   public static void init() {
      CHEST_SCAN = register(ProbablyChests.id("chest_scan"), PCGroundPlacementModifier.MODIFIER_CODEC);
      PC_RARITY = register(ProbablyChests.id("pc_rarity"), PCRarityFilterPlacementModifier.MODIFIER_CODEC);
      SOLID_CHECK = register(ProbablyChests.id("sold_check"), PCSolidGroundPlacementModifier.MODIFIER_CODEC);
   }
}
