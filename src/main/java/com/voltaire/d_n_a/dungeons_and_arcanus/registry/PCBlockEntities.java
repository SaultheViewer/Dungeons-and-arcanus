package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.mojang.datafixers.types.Type;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.LinkedHashMap;
import java.util.Map;

public class PCBlockEntities {
   private static final Map<BlockEntityType<?>, ResourceLocation> BLOCK_ENTITY_TYPES = new LinkedHashMap();
   public static final BlockEntityType<LushChestBlockEntity> LUSH_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<NormalChestBlockEntity> NORMAL_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<AzureChestBlockEntity> AZURE_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<GoldChestBlockEntity> GOLD_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<NetherChestBlockEntity> NETHER_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<ShadowChestBlockEntity> SHADOW_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<IceChestBlockEntity> ICE_CHEST_BLOCK_ENTITY;
   public static final BlockEntityType<CoralChestBlockEntity> CORAL_CHEST_BLOCK_ENTITY;

   public static void init() {
      BLOCK_ENTITY_TYPES.keySet().forEach((blockEntityType) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, (ResourceLocation)BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
   }

   private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
      BLOCK_ENTITY_TYPES.put(type, new ResourceLocation("probablychests", name));
      return type;
   }

   static {
      LUSH_CHEST_BLOCK_ENTITY = register("lush_chest_block_entity", FabricBlockEntityTypeBuilder.create(LushChestBlockEntity::new, new Block[]{PCBlocks.LUSH_CHEST}).build((Type)null));
      NORMAL_CHEST_BLOCK_ENTITY = register("normal_chest_block_entity", FabricBlockEntityTypeBuilder.create(NormalChestBlockEntity::new, new Block[]{PCBlocks.NORMAL_CHEST}).build((Type)null));
      AZURE_CHEST_BLOCK_ENTITY = register("azure_chest_block_entity", FabricBlockEntityTypeBuilder.create(AzureChestBlockEntity::new, new Block[]{PCBlocks.AZURE_CHEST}).build((Type)null));
      STONE_CHEST_BLOCK_ENTITY = register("stone_chest_block_entity", FabricBlockEntityTypeBuilder.create(StoneChestBlockEntity::new, new Block[]{PCBlocks.STONE_CHEST}).build((Type)null));
      GOLD_CHEST_BLOCK_ENTITY = register("gold_chest_block_entity", FabricBlockEntityTypeBuilder.create(GoldChestBlockEntity::new, new Block[]{PCBlocks.GOLD_CHEST}).build((Type)null));
      NETHER_CHEST_BLOCK_ENTITY = register("nether_chest_block_entity", FabricBlockEntityTypeBuilder.create(NetherChestBlockEntity::new, new Block[]{PCBlocks.NETHER_CHEST}).build((Type)null));
      SHADOW_CHEST_BLOCK_ENTITY = register("shadow_chest_block_entity", FabricBlockEntityTypeBuilder.create(ShadowChestBlockEntity::new, new Block[]{PCBlocks.SHADOW_CHEST}).build((Type)null));
      ICE_CHEST_BLOCK_ENTITY = register("ice_chest_block_entity", FabricBlockEntityTypeBuilder.create(IceChestBlockEntity::new, new Block[]{PCBlocks.ICE_CHEST}).build((Type)null));
      CORAL_CHEST_BLOCK_ENTITY = register("coral_chest_block_entity", FabricBlockEntityTypeBuilder.create(CoralChestBlockEntity::new, new Block[]{PCBlocks.CORAL_CHEST}).build((Type)null));
   }
}
