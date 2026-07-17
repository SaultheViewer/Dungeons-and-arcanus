package com.voltaire.d_n_a.dungeons_and_arcanus.datagen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Dungeons_and_arcanus.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        //this.tag(ModTags) use for custom block tags none exist yet

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MITHRIL_ORE.get(),
                        ModBlocks.DEEPSLATE_MITHRIL_ORE.get(),
                        ModBlocks.ORICALCIUM_ORE.get(),
                        ModBlocks.ORICALCIUM_BLOCK.get(),
                        ModBlocks.SILVER_ORE.get(),
                        ModBlocks.DEEPSLATE_SILVER_ORE.get(),
                        ModBlocks.RUBY_BLOCK.get(),
                        ModBlocks.RUBY_ORE.get(),
                        ModBlocks.RAW_SILVER_BLOCK.get(),
                        ModBlocks.MITHRIL_BLOCK.get()

                );

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.MITHRIL_ORE.get(),
                        ModBlocks.DEEPSLATE_MITHRIL_ORE.get(),
                        ModBlocks.ORICALCIUM_ORE.get()
                );


        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ORICALCIUM_BLOCK.get(),
                    ModBlocks.SILVER_ORE.get(),
                    ModBlocks.DEEPSLATE_SILVER_ORE.get(),
                    ModBlocks.RUBY_BLOCK.get(),
                    ModBlocks.RUBY_ORE.get(),
                    ModBlocks.RAW_SILVER_BLOCK.get(),
                    ModBlocks.MITHRIL_BLOCK.get()
                );



        this.tag(BlockTags.NEEDS_STONE_TOOL);;

    }
}
