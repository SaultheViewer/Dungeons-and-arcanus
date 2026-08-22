package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class PCFeatureRegistry {
   public static final Feature<NoneFeatureConfiguration> UNDERGROUND_CHEST;
   public static final Feature<PCPotFeatureConfig> NORMAL_POT;
   public static final Feature<PCPotFeatureConfig> LUSH_POT;
   public static final Feature<PCPotFeatureConfig> ROCKY_POT;
   public static final Feature<PCPotFeatureConfig> NETHER_POT;

   public static void init() {
   }

   static {
      UNDERGROUND_CHEST = (Feature)Registry.register(BuiltInRegistries.FEATURE, Dungeons_and_arcanus.id("underground_chest"), new UndergroundChestFeature(NoneFeatureConfiguration.CODEC));
      NORMAL_POT = (Feature)Registry.register(BuiltInRegistries.FEATURE, Dungeons_and_arcanus.id("normal_pot"), new NormalPotFeature(PCPotFeatureConfig.CODEC));
      LUSH_POT = (Feature)Registry.register(BuiltInRegistries.FEATURE, Dungeons_and_arcanus.id("lush_pot"), new LushPotFeature(PCPotFeatureConfig.CODEC));
      ROCKY_POT = (Feature)Registry.register(BuiltInRegistries.FEATURE, Dungeons_and_arcanus.id("rocky_pot"), new RockyPotFeature(PCPotFeatureConfig.CODEC));
      NETHER_POT = (Feature)Registry.register(BuiltInRegistries.FEATURE, Dungeons_and_arcanus.id("nether_pot"), new NetherPotFeature(PCPotFeatureConfig.CODEC));
   }
}
