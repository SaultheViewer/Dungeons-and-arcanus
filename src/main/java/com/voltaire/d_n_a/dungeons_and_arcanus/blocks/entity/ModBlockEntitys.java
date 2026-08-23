package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntitys {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Dungeons_and_arcanus.MOD_ID);

    public static final RegistryObject<BlockEntityType<LushChestBlockEntity>> LUSH_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "lush_chest_block_entity", () -> BlockEntityType.Builder.of(LushChestBlockEntity::new, ModBlocks.LUSH_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<NormalChestBlockEntity>> NORMAL_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "normal_chest_block_entity", () -> BlockEntityType.Builder.of(NormalChestBlockEntity::new, ModBlocks.NORMAL_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<AzureChestBlockEntity>> AZURE_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "azure_chest_block_entity", () -> BlockEntityType.Builder.of(AzureChestBlockEntity::new, ModBlocks.AZURE_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<StoneChestBlockEntity>> STONE_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "stone_chest_block_entity", () -> BlockEntityType.Builder.of(StoneChestBlockEntity::new, ModBlocks.STONE_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<GoldChestBlockEntity>> GOLD_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "gold_chest_block_entity", () -> BlockEntityType.Builder.of(GoldChestBlockEntity::new, ModBlocks.GOLD_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<NetherChestBlockEntity>> NETHER_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "nether_chest_block_entity", () -> BlockEntityType.Builder.of(NetherChestBlockEntity::new, ModBlocks.NETHER_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<ShadowChestBlockEntity>> SHADOW_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "shadow_chest_block_entity", () -> BlockEntityType.Builder.of(ShadowChestBlockEntity::new, ModBlocks.SHADOW_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<IceChestBlockEntity>> ICE_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "ice_chest_block_entity", () -> BlockEntityType.Builder.of(IceChestBlockEntity::new, ModBlocks.ICE_CHEST.get()).build(null)
    );
    public static final RegistryObject<BlockEntityType<CoralChestBlockEntity>> CORAL_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "coral_chest_block_entity", () -> BlockEntityType.Builder.of(CoralChestBlockEntity::new, ModBlocks.CORAL_CHEST.get()).build(null)
    );
}
