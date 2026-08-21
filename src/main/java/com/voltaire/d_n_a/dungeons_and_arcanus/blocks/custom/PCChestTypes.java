package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai.PCChestMimic;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PC_BaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCScreenHandlerType;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCChestScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public enum PCChestTypes {
    LUSH(54, 9, "lush_chest"),
    NORMAL(54, 9, "normal_chest"),
    AZURE(54, 9, "azure_chest"),
    STONE(54, 9, "stone_chest"),
    GOLD(54, 9, "gold_chest"),
    NETHER(54, 9, "nether_chest"),
    SHADOW(54, 9, "shadow_chest"),
    ICE(54, 9, "ice_chest"),
    CORAL(54, 9, "coral_chest");

    public final int size;
    public final int rowLength;
    public final String name;

    PCChestTypes(int size, int rowLength, String name) {
        this.size = size;
        this.rowLength = rowLength;
        this.name = name;
    }

    public EntityType<PCChestMimic> getMimicType() {
        return switch (this) {
            case LUSH   -> PCEntitys.LUSH_CHEST_MIMIC;
            case NORMAL -> PCEntitys.NORMAL_CHEST_MIMIC;
            case AZURE  -> PCEntitys.AZURE_CHEST_MIMIC;
            case STONE  -> PCEntitys.STONE_CHEST_MIMIC;
            case GOLD   -> PCEntitys.GOLD_CHEST_MIMIC;
            case NETHER -> PCEntitys.NETHER_CHEST_MIMIC;
            case SHADOW -> PCEntitys.SHADOW_CHEST_MIMIC;
            case ICE    -> PCEntitys.ICE_CHEST_MIMIC;
            case CORAL  -> PCEntitys.CORAL_CHEST_MIMIC;
        };
    }


    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public BlockEntityType<? extends PC_BaseChestBlockEntity> getBlockEntityType() {
        return switch (this) {
            case LUSH   -> PCBlockEntitys.LUSH_CHEST_BLOCK_ENTITY;
            case NORMAL -> PCBlockEntitys.NORMAL_CHEST_BLOCK_ENTITY;
            case AZURE  -> PCBlockEntitys.AZURE_CHEST_BLOCK_ENTITY;
            case STONE  -> PCBlockEntitys.STONE_CHEST_BLOCK_ENTITY;
            case GOLD   -> PCBlockEntitys.GOLD_CHEST_BLOCK_ENTITY;
            case NETHER -> PCBlockEntitys.NETHER_CHEST_BLOCK_ENTITY;
            case SHADOW -> PCBlockEntitys.SHADOW_CHEST_BLOCK_ENTITY;
            case ICE    -> PCBlockEntitys.ICE_CHEST_BLOCK_ENTITY;
            case CORAL  -> PCBlockEntitys.CORAL_CHEST_BLOCK_ENTITY;
        };
    }

    public PC_BaseChestBlockEntity makeEntity(BlockPos pos, BlockState state) {
        return this.getBlockEntityType().create(pos, state);
    }

    public MenuType<PCChestScreenHandler> getScreenHandlerType() {
        return PCScreenHandlerType.PC_CHEST;
    }

    public BlockBehaviour.Properties setting() {
        return switch (this) {
            case LUSH, NORMAL -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3600000.0F)
                    .sound(SoundType.WOOD);

            case AZURE, STONE -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0F, 3600000.0F)
                    .sound(SoundType.STONE);

            case GOLD, SHADOW -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(2.0F, 3600000.0F)
                    .sound(SoundType.METAL);

            case NETHER -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .strength(2.0F, 3600000.0F)
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 9);

            case ICE -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .strength(2.0F, 3600000.0F)
                    .sound(SoundType.GLASS)
                    .lightLevel(state -> 7)
                    .friction(0.98F);

            case CORAL -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0F, 3600000.0F)
                    .sound(SoundType.CORAL_BLOCK)
                    .lightLevel(state -> PC_ChestBlock.isDry(state) ? 0 : 12)
                    .friction(0.99F);
        };
    }

    public EntityType<? extends Tameable_Pet_With_Inv> getPetMimicType() {
        return switch (this) {
            case LUSH   -> PCEntitys.LUSH_CHEST_MIMIC_PET;
            case NORMAL -> PCEntitys.NORMAL_CHEST_MIMIC_PET;
            case AZURE  -> PCEntitys.AZURE_CHEST_MIMIC_PET;
            case STONE  -> PCEntitys.STONE_CHEST_MIMIC_PET;
            case GOLD   -> PCEntitys.GOLD_CHEST_MIMIC_PET;
            case NETHER -> PCEntitys.NETHER_CHEST_MIMIC_PET;
            case SHADOW -> PCEntitys.SHADOW_CHEST_MIMIC_PET;
            case ICE    -> PCEntitys.ICE_CHEST_MIMIC_PET;
            case CORAL  -> PCEntitys.CORAL_CHEST_MIMIC_PET;
        };
    }
}
