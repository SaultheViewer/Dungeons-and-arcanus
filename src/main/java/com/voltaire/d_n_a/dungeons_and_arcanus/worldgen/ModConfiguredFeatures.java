package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {

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


    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Dungeons_and_arcanus.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
