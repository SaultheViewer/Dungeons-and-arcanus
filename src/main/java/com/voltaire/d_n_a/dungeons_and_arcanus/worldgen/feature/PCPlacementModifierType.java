package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PCPlacementModifierType {
   public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Dungeons_and_arcanus.MOD_ID);

   public static final RegistryObject<PlacementModifierType<PCGroundPlacementModifier>> CHEST_SCAN = register("chest_scan", PCGroundPlacementModifier.MODIFIER_CODEC);
   public static final RegistryObject<PlacementModifierType<PCRarityFilterPlacementModifier>> PC_RARITY = register("pc_rarity", PCRarityFilterPlacementModifier.MODIFIER_CODEC);
   public static final RegistryObject<PlacementModifierType<PCSolidGroundPlacementModifier>> SOLID_CHECK = register("sold_check", PCSolidGroundPlacementModifier.MODIFIER_CODEC);

   private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> register(String name, Codec<P> codec) {
      return PLACEMENT_MODIFIERS.register(name, () -> () -> codec);
   }
}
