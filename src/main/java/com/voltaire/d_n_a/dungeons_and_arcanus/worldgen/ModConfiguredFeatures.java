package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen;


import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCFeatureRegistry;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature.PCPotFeatureConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    private static final Feature<NoneFeatureConfiguration> UNDERGROUND_CHEST_FEATURE = PCFeatureRegistry.UNDERGROUND_CHEST.get();
    private static final Feature<NoneFeatureConfiguration> SURFACE_CHEST_FEATURE = PCFeatureRegistry.SURFACE_CHEST.get();
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
    public static final ResourceKey<ConfiguredFeature<? , ?>> OVERWORLD_MITHRIL_ORE_KEY = registerKey("mithril_ore");
    public static final ResourceKey<ConfiguredFeature<? , ?>> OVERWORLD_RUBY_ORE_KEY = registerKey("ruby_ore");
    public static final ResourceKey<ConfiguredFeature<? , ?>> OVERWORLD_SILVER_ORE_KEY = registerKey("silver_ore");
    public static final ResourceKey<ConfiguredFeature<? , ?>> NETHER_ORICALCIUM_ORE_KEY = registerKey("oricalcium_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<? , ?>> context) {
        RuleTest stoneReplacables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest blackstonereplacables = new BlockMatchTest(Blocks.BLACKSTONE);

        List<OreConfiguration.TargetBlockState> overworldMithrilOres = List.of(OreConfiguration.target(stoneReplacables,
                        ModBlocks.MITHRIL_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_MITHRIL_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> overworldSilverOres = List.of(OreConfiguration.target(stoneReplacables,
                        ModBlocks.SILVER_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 5));
        register(context, OVERWORLD_SILVER_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSilverOres, 6));
        register(context, NETHER_ORICALCIUM_ORE_KEY, Feature.ORE, new OreConfiguration(blackstonereplacables,
                ModBlocks.ORICALCIUM_ORE.get().defaultBlockState(), 9));
        register(context, OVERWORLD_RUBY_ORE_KEY, Feature.ORE, new OreConfiguration(stoneReplacables,
                ModBlocks.RUBY_ORE.get().defaultBlockState(), 7));

        register(context, SURFACE_CHEST_KEY, SURFACE_CHEST_FEATURE, NoneFeatureConfiguration.INSTANCE);
        register(context, UNDERGROUND_CHEST_KEY, UNDERGROUND_CHEST_FEATURE, NoneFeatureConfiguration.INSTANCE);
        register(context, NETHER_CHEST_KEY, UNDERGROUND_CHEST_FEATURE, NoneFeatureConfiguration.INSTANCE);

        // ========== POTS ==========
        register(context, NORMAL_POT_KEY,  NORMAL_POT_FEATURE,  new PCPotFeatureConfig(ConstantFloat.of(2.0F)));
        register(context, LUSH_POT_KEY,    LUSH_POT_FEATURE,    new PCPotFeatureConfig(ConstantFloat.of(2.0F)));
        register(context, ROCKY_POT_KEY,   ROCKY_POT_FEATURE,   new PCPotFeatureConfig(ConstantFloat.of(2.0F)));
        register(context, NETHER_POT_KEY,  NETHER_POT_FEATURE,  new PCPotFeatureConfig(ConstantFloat.of(2.0F)));

    }



    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Dungeons_and_arcanus.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
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
