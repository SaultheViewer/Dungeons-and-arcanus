package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom;

import com.voltaire.d_n_a.dungeons_and_arcanus.registry.ModEntitys;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.PCChestMimic;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.ModBlockEntitys;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PCBaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCLootTables;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCScreenHandlerType;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCChestScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public enum PCChestTypes {
    LUSH(54, 9, new ResourceLocation("d_n_a", "lush_chest"), "lush_chest"),
    NORMAL(54, 9, new ResourceLocation("d_n_a", "normal_chest"), "normal_chest"),
    AZURE(54, 9, new ResourceLocation("d_n_a", "azure_chest"), "azure_chest"),
    STONE(54, 9, new ResourceLocation("d_n_a", "stone_chest"), "stone_chest"),
    GOLD(54, 9, new ResourceLocation("d_n_a", "gold_chest"), "gold_chest"),
    NETHER(54, 9, new ResourceLocation("d_n_a", "nether_chest"), "nether_chest"),
    SHADOW(54, 9, new ResourceLocation("d_n_a", "shadow_chest"), "shadow_chest"),
    ICE(54, 9, new ResourceLocation("d_n_a", "ice_chest"), "ice_chest"),
    CORAL(54, 9, new ResourceLocation("d_n_a", "coral_chest"), "coral_chest");

    public final int size;
    public final int rowLength;
    public final ResourceLocation texture;
    public final String name;

    PCChestTypes(int size, int rowLength, ResourceLocation texture, String name) {
        this.size = size;
        this.rowLength = rowLength;
        this.texture = texture;
        this.name = name;
    }

    public EntityType<PCChestMimic> getMimicType() {
        return switch (this) {
            case LUSH -> ModEntitys.LUSH_CHEST_MIMIC.get();
            case NORMAL -> ModEntitys.NORMAL_CHEST_MIMIC.get();
            case AZURE -> ModEntitys.AZURE_CHEST_MIMIC.get();
            case STONE -> ModEntitys.STONE_CHEST_MIMIC.get();
            case GOLD -> ModEntitys.GOLD_CHEST_MIMIC.get();
            case NETHER -> ModEntitys.NETHER_CHEST_MIMIC.get();
            case SHADOW -> ModEntitys.SHADOW_CHEST_MIMIC.get();
            case ICE -> ModEntitys.ICE_CHEST_MIMIC.get();
            case CORAL -> ModEntitys.CORAL_CHEST_MIMIC.get();
        };
    }

    public ResourceLocation getLootTable() {
        return switch (this) {
            case LUSH -> PCLootTables.LUSH_CHEST;
            case NORMAL -> PCLootTables.NORMAL_CHEST;
            case AZURE -> PCLootTables.AZURE_CHEST;
            case STONE -> PCLootTables.STONE_CHEST;
            case GOLD -> PCLootTables.GOLD_CHEST;
            case NETHER -> PCLootTables.NETHER_CHEST;
            case SHADOW -> PCLootTables.SHADOW_CHEST;
            case ICE -> PCLootTables.ICE_CHEST;
            case CORAL -> PCLootTables.CORAL_CHEST;
        };
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public BlockEntityType<? extends PCBaseChestBlockEntity> getBlockEntityType() {
        return switch (this) {
            case LUSH -> ModBlockEntitys.LUSH_CHEST_BLOCK_ENTITY.get();
            case NORMAL -> ModBlockEntitys.NORMAL_CHEST_BLOCK_ENTITY.get();
            case AZURE -> ModBlockEntitys.AZURE_CHEST_BLOCK_ENTITY.get();
            case STONE -> ModBlockEntitys.STONE_CHEST_BLOCK_ENTITY.get();
            case GOLD -> ModBlockEntitys.GOLD_CHEST_BLOCK_ENTITY.get();
            case NETHER -> ModBlockEntitys.NETHER_CHEST_BLOCK_ENTITY.get();
            case SHADOW -> ModBlockEntitys.SHADOW_CHEST_BLOCK_ENTITY.get();
            case ICE -> ModBlockEntitys.ICE_CHEST_BLOCK_ENTITY.get();
            case CORAL -> ModBlockEntitys.CORAL_CHEST_BLOCK_ENTITY.get();
        };
    }

    public PCBaseChestBlockEntity makeEntity(BlockPos pos, BlockState state) {
        return (PCBaseChestBlockEntity) this.getBlockEntityType().create(pos, state);
    }

    public MenuType<PCChestScreenHandler> getScreenHandlerType() {
        return PCScreenHandlerType.PC_CHEST.get();
    }

    public BlockBehaviour.Properties setting() {
        return switch (this) {
            case LUSH, NORMAL -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F, 3600000.0F).sound(SoundType.WOOD);
            case AZURE, STONE -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).strength(2.0F, 3600000.0F).sound(SoundType.STONE);
            case GOLD, SHADOW -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD).strength(2.0F, 3600000.0F).sound(SoundType.METAL);
            case NETHER -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER).strength(2.0F, 3600000.0F).sound(SoundType.STONE).lightLevel(state -> 9);
            case ICE -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE).strength(2.0F, 3600000.0F).sound(SoundType.GLASS).lightLevel(state -> 7).friction(0.98F);
            case CORAL -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(2.0F, 3600000.0F).sound(SoundType.CORAL_BLOCK).lightLevel(state -> PC_ChestBlock.isDry(state) ? 0 : 12).friction(0.99F);
        };
    }

    public EntityType<? extends Tameable_Pet_With_Inv> getPetMimicType() {
        return switch (this) {
            case LUSH -> ModEntitys.LUSH_CHEST_MIMIC_PET.get();
            case NORMAL -> ModEntitys.NORMAL_CHEST_MIMIC_PET.get();
            case AZURE -> ModEntitys.AZURE_CHEST_MIMIC_PET.get();
            case STONE -> ModEntitys.STONE_CHEST_MIMIC_PET.get();
            case GOLD -> ModEntitys.GOLD_CHEST_MIMIC_PET.get();
            case NETHER -> ModEntitys.NETHER_CHEST_MIMIC_PET.get();
            case SHADOW -> ModEntitys.SHADOW_CHEST_MIMIC_PET.get();
            case ICE -> ModEntitys.ICE_CHEST_MIMIC_PET.get();
            case CORAL -> ModEntitys.CORAL_CHEST_MIMIC_PET.get();
        };
    }
}
