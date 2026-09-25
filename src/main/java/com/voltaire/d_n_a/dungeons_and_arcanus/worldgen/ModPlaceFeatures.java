package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature.PCGroundPlacementModifier;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature.PCRarityFilterPlacementModifier;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlaceFeatures {
    // --- ores ---
    public static final ResourceKey<PlacedFeature> MITHRIL_ORE_PLACED_KEY = registerKey("mithril_ore_placed");
    public static final ResourceKey<PlacedFeature> SILVER_ORE_PLACED_KEY = registerKey("silver_ore_placed");
    public static final ResourceKey<PlacedFeature> RUBY_ORE_PLACED_KEY = registerKey("ruby_ore_placed");
    public static final ResourceKey<PlacedFeature> ORICALCIUM_ORE_PLACED_KEY = registerKey("oricalcium_ore_placed");

    // --- chests  ---
    public static final ResourceKey<PlacedFeature> SURFACE_CHEST_PLACED_KEY = registerKey("surface_chest_placed");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_CHEST_PLACED_KEY = registerKey("underground_chest_placed");
    public static final ResourceKey<PlacedFeature> NETHER_CHEST_PLACED_KEY = registerKey("nether_chest_placed");

    // --- pots ---
    public static final ResourceKey<PlacedFeature> NORMAL_POT_PLACED_KEY = registerKey("normal_pot_placed");
    public static final ResourceKey<PlacedFeature> LUSH_POT_PLACED_KEY = registerKey("lush_pot_placed");
    public static final ResourceKey<PlacedFeature> ROCKY_POT_PLACED_KEY = registerKey("rocky_pot_placed");
    public static final ResourceKey<PlacedFeature> NETHER_POT_PLACED_KEY = registerKey("nether_pot_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        float chestChance = Dungeons_and_arcanus.loadedConfig.worldGen.chestSpawnChance;
        float potChance = Dungeons_and_arcanus.loadedConfig.worldGen.potSpawnChance;

        register(context, MITHRIL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_MITHRIL_ORE_KEY),
                ModOrePlacement.commonOrePlacement(10,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(70))));
        register(context, SILVER_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_SILVER_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));
        register(context, RUBY_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_RUBY_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));
        register(context, ORICALCIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_ORICALCIUM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(1), VerticalAnchor.absolute(120))));


        // ========== SURFACE CHEST ==========
        register(context, SURFACE_CHEST_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SURFACE_CHEST_KEY),
                List.of(
                        PCRarityFilterPlacementModifier.of(chestChance * 0.02F),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                        BiomeFilter.biome()
                ));

        // ========== UNDERGROUND CHEST ==========
        register(context, UNDERGROUND_CHEST_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.UNDERGROUND_CHEST_KEY),
                List.of(
                        CountPlacement.of(2),
                        InSquarePlacement.spread(),
                        PCRarityFilterPlacementModifier.of(chestChance * 0.85F),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(64)),
                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), 32),
                        BiomeFilter.biome()
                ));

        // ========== NETHER CHEST ==========
        register(context, NETHER_CHEST_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_CHEST_KEY),
                List.of(
                        CountPlacement.of(2),
                        InSquarePlacement.spread(),
                        PCRarityFilterPlacementModifier.of(chestChance * 0.75F),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.belowTop(6)),
                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), 12),
                        BiomeFilter.biome()
                ));

        // ========== POTS ==========
        register(context, NORMAL_POT_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.NORMAL_POT_KEY),
                potModifiers(potChance));

        register(context, LUSH_POT_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.LUSH_POT_KEY),
                potModifiers(potChance));

        register(context, ROCKY_POT_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.ROCKY_POT_KEY),
                potModifiers(potChance));

        register(context, NETHER_POT_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_POT_KEY),
                potModifiers(potChance));
    }

    private static List<PlacementModifier> potModifiers(float potChance) {
        return List.of(
                PCRarityFilterPlacementModifier.of(potChance),
                CountPlacement.of(8),
                InSquarePlacement.spread(),
                PCGroundPlacementModifier.of(
                        Direction.DOWN,
                        BlockPredicate.hasSturdyFace(Direction.UP),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                        20,
                        Heightmap.Types.WORLD_SURFACE_WG,
                        300
                ),
                BiomeFilter.biome()
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Dungeons_and_arcanus.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
