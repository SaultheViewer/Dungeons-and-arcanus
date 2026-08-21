package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestBlock;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.block.PCPot;
import org.cloudwarp.probablychests.block.PCPotTypes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PCBlocks {
   private static final Map<Block, ResourceLocation> BLOCKS = new LinkedHashMap();
   private static final Map<Item, ResourceLocation> ITEMS = new LinkedHashMap();
   public static final Block LUSH_CHEST;
   public static final Block NORMAL_CHEST;
   public static final Block AZURE_CHEST;
   public static final Block STONE_CHEST;
   public static final Block GOLD_CHEST;
   public static final Block NETHER_CHEST;
   public static final Block SHADOW_CHEST;
   public static final Block ICE_CHEST;
   public static final Block CORAL_CHEST;
   public static final Block LUSH_POT;
   public static final Block NORMAL_POT;
   public static final Block ROCKY_POT;
   public static final Block NETHER_POT;

   private static <T extends Block> T create(String name, T block, boolean createItem) {
      BLOCKS.put(block, new ResourceLocation("probablychests", name));
      if (createItem) {
         BlockItem blockItem = new BlockItem(block, new Item.Properties());
         ITEMS.put(blockItem, (ResourceLocation)BLOCKS.get(block));
      }

      return block;
   }

   public static void init() {
      BLOCKS.keySet().forEach((block) -> Registry.register(BuiltInRegistries.BLOCK, (ResourceLocation)BLOCKS.get(block), block));
      ITEMS.keySet().forEach((item) -> Registry.register(BuiltInRegistries.ITEM, (ResourceLocation)ITEMS.get(item), item));
      ItemGroupEvents.modifyEntriesEvent(ProbablyChests.PROBABLY_CHESTS_GROUP).register((ItemGroupEvents.ModifyEntries)(content) -> {
         Set var10000 = ITEMS.keySet();
         Objects.requireNonNull(content);
         var10000.forEach(content::accept);
      });
   }

   static {
      LUSH_CHEST = create("lush_chest", new PCChestBlock(PCChestTypes.LUSH.setting(), PCChestTypes.LUSH), true);
      NORMAL_CHEST = create("normal_chest", new PCChestBlock(PCChestTypes.NORMAL.setting(), PCChestTypes.NORMAL), true);
      AZURE_CHEST = create("azure_chest", new PCChestBlock(PCChestTypes.AZURE.setting(), PCChestTypes.AZURE), true);
      STONE_CHEST = create("stone_chest", new PCChestBlock(PCChestTypes.STONE.setting(), PCChestTypes.STONE), true);
      GOLD_CHEST = create("gold_chest", new PCChestBlock(PCChestTypes.GOLD.setting(), PCChestTypes.GOLD), true);
      NETHER_CHEST = create("nether_chest", new PCChestBlock(PCChestTypes.NETHER.setting(), PCChestTypes.NETHER), true);
      SHADOW_CHEST = create("shadow_chest", new PCChestBlock(PCChestTypes.SHADOW.setting(), PCChestTypes.SHADOW), true);
      ICE_CHEST = create("ice_chest", new PCChestBlock(PCChestTypes.ICE.setting(), PCChestTypes.ICE), true);
      CORAL_CHEST = create("coral_chest", new PCChestBlock(PCChestTypes.CORAL.setting(), PCChestTypes.CORAL), true);
      LUSH_POT = create("lush_pot", new PCPot(PCPotTypes.LUSH.setting(), PCVoxelShapes.POT_VOXELSHAPE), true);
      NORMAL_POT = create("normal_pot", new PCPot(PCPotTypes.NORMAL.setting(), PCVoxelShapes.POT_VOXELSHAPE), true);
      ROCKY_POT = create("rocky_pot", new PCPot(PCPotTypes.ROCKY.setting(), PCVoxelShapes.POT_VOXELSHAPE), true);
      NETHER_POT = create("nether_pot", new PCPot(PCPotTypes.NETHER.setting(), PCVoxelShapes.POT_VOXELSHAPE), true);
   }
}
