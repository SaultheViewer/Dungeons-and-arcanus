package com.voltaire.d_n_a.dungeons_and_arcanus.world.feature;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCFeatureRegistry;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;


public class PCFeatures {
   private static final Feature<NoneFeatureConfiguration> UNDERGROUND_CHEST_FEATURE = PCFeatureRegistry.UNDERGROUND_CHEST.get();
   private static final Feature<PCPotFeatureConfig> NORMAL_POT_FEATURE = PCFeatureRegistry.NORMAL_POT.get();
   private static final Feature<PCPotFeatureConfig> LUSH_POT_FEATURE = PCFeatureRegistry.LUSH_POT.get();
   private static final Feature<PCPotFeatureConfig> ROCKY_POT_FEATURE = PCFeatureRegistry.ROCKY_POT.get();
   private static final Feature<PCPotFeatureConfig> NETHER_POT_FEATURE = PCFeatureRegistry.NETHER_POT.get();
   public static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_CHEST_KEY = configuredKey("surface_chest");
   public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERGROUND_CHEST_KEY = configuredKey("underground_chest");
   public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_CHEST_KEY = configuredKey("nether_chest");
   public static final ResourceKey<ConfiguredFeature<?, ?>> NORMAL_POT_KEY = configuredKey("normal_pot");
   public static final ResourceKey<ConfiguredFeature<?, ?>> LUSH_POT_KEY = configuredKey("lush_pot");
   public static final ResourceKey<ConfiguredFeature<?, ?>> ROCKY_POT_KEY = configuredKey("rocky_pot");
   public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_POT_KEY = configuredKey("nether_pot");
   public static final ResourceKey<PlacedFeature> SURFACE_CHEST_PLACED_KEY = placedKey("surface_chest_placed");
   public static final ResourceKey<PlacedFeature> UNDERGROUND_CHEST_PLACED_KEY = placedKey("underground_chest_placed");
   public static final ResourceKey<PlacedFeature> NETHER_CHEST_PLACED_KEY = placedKey("nether_chest_placed");
   public static final ResourceKey<PlacedFeature> NORMAL_POT_PLACED_KEY = placedKey("normal_pot_placed");
   public static final ResourceKey<PlacedFeature> LUSH_POT_PLACED_KEY = placedKey("lush_pot_placed");
   public static final ResourceKey<PlacedFeature> ROCKY_POT_PLACED_KEY = placedKey("rocky_pot_placed");
   public static final ResourceKey<PlacedFeature> NETHER_POT_PLACED_KEY = placedKey("nether_pot_placed");

   public static void bootstrapConfigured(BootstapContext<ConfiguredFeature<?, ?>> context) {
      registerConfigured(context, UNDERGROUND_CHEST_KEY, UNDERGROUND_CHEST_FEATURE, new NoneFeatureConfiguration());
      registerConfigured(context, NETHER_CHEST_KEY, UNDERGROUND_CHEST_FEATURE, new NoneFeatureConfiguration());
      registerConfigured(context, NORMAL_POT_KEY, NORMAL_POT_FEATURE, new PCPotFeatureConfig(ConstantFloat.of(1.0F)));
      registerConfigured(context, LUSH_POT_KEY, LUSH_POT_FEATURE, new PCPotFeatureConfig(ConstantFloat.of(1.0F)));
      registerConfigured(context, ROCKY_POT_KEY, ROCKY_POT_FEATURE, new PCPotFeatureConfig(ConstantFloat.of(1.0F)));
      registerConfigured(context, NETHER_POT_KEY, NETHER_POT_FEATURE, new PCPotFeatureConfig(ConstantFloat.of(1.0F)));
   }

   public static void bootstrapPlaced(BootstapContext<PlacedFeature> context) {
      PCConfig config = Dungeons_and_arcanus.loadedConfig;
      float chestChance = config.worldGen.chestSpawnChance;
      float potChance = config.worldGen.potSpawnChance;
      HolderGetter<ConfiguredFeature<?, ?>> lookup = context.lookup(Registries.CONFIGURED_FEATURE);
      registerPlaced(
         context,
         UNDERGROUND_CHEST_PLACED_KEY,
         lookup.getOrThrow(UNDERGROUND_CHEST_KEY),
         CountPlacement.of(2),
         InSquarePlacement.spread(),
         PCRarityFilterPlacementModifier.of(chestChance * 0.85F),
         HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(64)),
         EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), 32),
         BiomeFilter.biome()
      );
      registerPlaced(
         context,
         NETHER_CHEST_PLACED_KEY,
         lookup.getOrThrow(NETHER_CHEST_KEY),
         CountPlacement.of(2),
         InSquarePlacement.spread(),
         PCRarityFilterPlacementModifier.of(chestChance * 0.75F),
         HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.belowTop(6)),
         EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), 12),
         BiomeFilter.biome()
      );
      registerPlaced(
         context,
         NORMAL_POT_PLACED_KEY,
         lookup.getOrThrow(NORMAL_POT_KEY),
         PCRarityFilterPlacementModifier.of(potChance),
         CountPlacement.of(8),
         InSquarePlacement.spread(),
         PCGroundPlacementModifier.of(
            Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.ONLY_IN_AIR_PREDICATE, 20, Types.WORLD_SURFACE_WG, 300
         ),
         BiomeFilter.biome()
      );
      registerPlaced(
         context,
         LUSH_POT_PLACED_KEY,
         lookup.getOrThrow(LUSH_POT_KEY),
         PCRarityFilterPlacementModifier.of(potChance),
         CountPlacement.of(8),
         InSquarePlacement.spread(),
         PCGroundPlacementModifier.of(
            Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.ONLY_IN_AIR_PREDICATE, 20, Types.WORLD_SURFACE_WG, 300
         ),
         BiomeFilter.biome()
      );
      registerPlaced(
         context,
         ROCKY_POT_PLACED_KEY,
         lookup.getOrThrow(ROCKY_POT_KEY),
         PCRarityFilterPlacementModifier.of(potChance),
         CountPlacement.of(8),
         InSquarePlacement.spread(),
         PCGroundPlacementModifier.of(
            Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.ONLY_IN_AIR_PREDICATE, 20, Types.WORLD_SURFACE_WG, 300
         ),
         BiomeFilter.biome()
      );
      registerPlaced(
         context,
         NETHER_POT_PLACED_KEY,
         lookup.getOrThrow(NETHER_POT_KEY),
         PCRarityFilterPlacementModifier.of(potChance),
         CountPlacement.of(8),
         InSquarePlacement.spread(),
         PCGroundPlacementModifier.of(
            Direction.DOWN, BlockPredicate.hasSturdyFace(Direction.UP), BlockPredicate.ONLY_IN_AIR_PREDICATE, 20, Types.WORLD_SURFACE_WG, 300
         ),
         BiomeFilter.biome()
      );
   }

   private static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String name) {
      return ResourceKey.create(Registries.CONFIGURED_FEATURE, Dungeons_and_arcanus.id(name));
   }

   private static ResourceKey<PlacedFeature> placedKey(String name) {
      return ResourceKey.create(Registries.PLACED_FEATURE, Dungeons_and_arcanus.id(name));
   }

   private static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerConfigured(
      BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config
   ) {
      context.register(key, new ConfiguredFeature(feature, config));
   }

   private static void registerPlaced(
      BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configured, PlacementModifier... modifiers
   ) {
      context.register(key, new PlacedFeature(configured, List.of(modifiers)));
   }
}
