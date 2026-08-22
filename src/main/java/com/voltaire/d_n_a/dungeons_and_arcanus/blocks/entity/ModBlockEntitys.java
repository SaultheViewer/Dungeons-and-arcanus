package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntitys {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Dungeons_and_arcanus.MOD_ID);

    public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> CORAL_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("coral_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.CORAL, pos, state),
                            ModBlocks.CORAL_CHEST.get()
                    ).build(null)
            );
    public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> LUSH_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("lush_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.LUSH, pos, state),
                            ModBlocks.LUSH_CHEST.get()
                    ).build(null)
            );
    public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> AZURE_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("azure_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.AZURE, pos, state),
                            ModBlocks.AZURE_CHEST.get()
                    ).build(null)
            );
    public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> STONE_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("stone_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.STONE, pos, state),
                            ModBlocks.STONE_CHEST.get()
                    ).build(null)
            );
    public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> NORMAL_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("normal_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.NORMAL, pos, state),
                            ModBlocks.NORMAL_CHEST.get()
                    ).build(null)
            );
    public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> GOLD_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("gold_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.GOLD, pos, state),
                            ModBlocks.GOLD_CHEST.get()
                    ).build(null)
            );
    public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> NETHER_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("nether_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.NETHER, pos, state),
                            ModBlocks.NETHER_CHEST.get()
                    ).build(null)
            );public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> SHADOW_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("shadow_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.SHADOW, pos, state),
                            ModBlocks.SHADOW_CHEST.get()
                    ).build(null)
            );public static final RegistryObject<BlockEntityType<PC_BaseChestBlockEntity>> ICE_CHEST_BLOCK_ENTITY =
            BLOCK_ENTITYS.register("ice_chest_block_entity", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new PC_BaseChestBlockEntity(PCChestTypes.ICE, pos, state),
                            ModBlocks.ICE_CHEST.get()
                    ).build(null)
            );
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITYS.register(eventBus);
    }
}
