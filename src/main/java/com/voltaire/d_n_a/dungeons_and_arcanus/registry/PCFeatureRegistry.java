package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class PCFeatureRegistry {
   public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Dungeons_and_arcanus.MOD_ID);

   public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNDERGROUND_CHEST = FEATURES.register(
           "underground_chest", () -> new UndergroundChestFeature(NoneFeatureConfiguration.CODEC)
   );
   public static final RegistryObject<Feature<NoneFeatureConfiguration>> SURFACE_CHEST = FEATURES.register(
           "surface_chest", () -> new SurfaceChestFeature(NoneFeatureConfiguration.CODEC)
   );
   public static final RegistryObject<Feature<PCPotFeatureConfig>> NORMAL_POT = FEATURES.register(
           "normal_pot", () -> new NormalPotFeature(PCPotFeatureConfig.CODEC)
   );
   public static final RegistryObject<Feature<PCPotFeatureConfig>> LUSH_POT = FEATURES.register(
           "lush_pot", () -> new LushPotFeature(PCPotFeatureConfig.CODEC)
   );
   public static final RegistryObject<Feature<PCPotFeatureConfig>> ROCKY_POT = FEATURES.register(
           "rocky_pot", () -> new RockyPotFeature(PCPotFeatureConfig.CODEC)
   );
   public static final RegistryObject<Feature<PCPotFeatureConfig>> NETHER_POT = FEATURES.register(
           "nether_pot", () -> new NetherPotFeature(PCPotFeatureConfig.CODEC)
   );
}
