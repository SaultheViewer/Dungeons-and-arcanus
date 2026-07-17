package com.voltaire.d_n_a.dungeons_and_arcanus.datagen.loot;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        //normal blocks
        this.dropSelf(ModBlocks.MITHRIL_BLOCK.get());
        this.dropSelf(ModBlocks.RUBY_BLOCK.get());
        this.dropSelf(ModBlocks.SILVER_BLOCK.get());
        this.dropSelf(ModBlocks.ORICALCIUM_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_SILVER_BLOCK.get());
        //ores
        this.add(ModBlocks.DEEPSLATE_MITHRIL_ORE.get(),
        block -> createCustomRarityDrops1(ModBlocks.DEEPSLATE_MITHRIL_ORE.get(), ModItems.RAW_MITHRIL.get()));
        this.add(ModBlocks.MITHRIL_ORE.get(),
        block -> createCustomRarityDrops1(ModBlocks.MITHRIL_ORE.get(), ModItems.RAW_MITHRIL.get()));
        this.add(ModBlocks.SILVER_ORE.get(),
        block -> createCustomRarityDrops2(ModBlocks.SILVER_ORE.get(), ModItems.RAW_SILVER.get()));
        this.add(ModBlocks.DEEPSLATE_SILVER_ORE.get(),
        block -> createCustomRarityDrops1(ModBlocks.DEEPSLATE_SILVER_ORE.get(), ModItems.RAW_SILVER.get()));
        this.add(ModBlocks.RUBY_ORE.get(),
        block -> createCustomRarityDrops1(ModBlocks.RUBY_ORE.get(), ModItems.RUBY.get()));
        this.add(ModBlocks.ORICALCIUM_ORE.get(),
        block -> createCustomRarityDrops1(ModBlocks.ORICALCIUM_ORE.get(), ModItems.ORICALCIUM_GEM.get()));
    }
    //higher rarity ore drops
    protected LootTable.Builder createCustomRarityDrops1(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
    //normal ore rarity
    protected LootTable.Builder createCustomRarityDrops2(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
