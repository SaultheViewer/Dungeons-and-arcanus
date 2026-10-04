package com.voltaire.d_n_a.dungeons_and_arcanus.datagen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.Cotton_crop;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;


public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Dungeons_and_arcanus.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //blocks go here
        blockWithItem(ModBlocks.MITHRIL_BLOCK);
        blockWithItem(ModBlocks.RUBY_BLOCK);
        blockWithItem(ModBlocks.ORICALCIUM_BLOCK);
        blockWithItem(ModBlocks.RAW_SILVER_BLOCK);
        blockWithItem(ModBlocks.SILVER_BLOCK);

        //ores go here
        blockWithItem(ModBlocks.DEEPSLATE_MITHRIL_ORE);
        blockWithItem(ModBlocks.MITHRIL_ORE);
        blockWithItem(ModBlocks.SILVER_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SILVER_ORE);
        blockWithItem(ModBlocks.RUBY_ORE);
        blockWithItem(ModBlocks.ORICALCIUM_ORE);


        //custom blocks go here
        blockItem(ModBlocks.ROCKY_POT);
        blockItem(ModBlocks.NETHER_POT);
        blockItem(ModBlocks.NORMAL_POT);
        blockItem(ModBlocks.LUSH_POT);

        //plants
        makeCottonCrop((CropBlock) ModBlocks.COTTON_CROP.get(), "cotton_stage", "cotton_stage");




    }
    public void makeCottonCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cottonStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }
    private ConfiguredModel[] cottonStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((Cotton_crop) block).getAgeProperty()),
                new ResourceLocation(Dungeons_and_arcanus.MOD_ID, "block/" + textureName + state.getValue(((Cotton_crop) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(
                blockRegistryObject.get(),
                models().getExistingFile(modLoc("block/" + blockRegistryObject.getId().getPath()))
        );
    }
}
