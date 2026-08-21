package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class PCLootTables {
   private static final Set<ResourceLocation> PC_LOOT_TABLES = Sets.newHashSet();
   public static ResourceLocation LUSH_CHEST;
   public static ResourceLocation NORMAL_CHEST;
   public static ResourceLocation AZURE_CHEST;
   public static ResourceLocation STONE_CHEST;
   public static ResourceLocation GOLD_CHEST;
   public static ResourceLocation NETHER_CHEST;
   public static ResourceLocation SHADOW_CHEST;
   public static ResourceLocation ICE_CHEST;
   public static ResourceLocation CORAL_CHEST;

   private static ResourceLocation register(String id) {
      return registerLootTable(new ResourceLocation(id));
   }

   private static ResourceLocation registerLootTable(ResourceLocation id) {
      if (PC_LOOT_TABLES.add(id)) {
         return id;
      } else {
         throw new IllegalArgumentException(String.valueOf(id) + " is already a registered built-in loot table");
      }
   }

   public static void init() {
      LUSH_CHEST = register("probablychests:chests/lush_pc_chests");
      NORMAL_CHEST = register("probablychests:chests/normal_pc_chests");
      AZURE_CHEST = register("probablychests:chests/azure_pc_chests");
      STONE_CHEST = register("probablychests:chests/stone_pc_chests");
      GOLD_CHEST = register("probablychests:chests/gold_pc_chests");
      NETHER_CHEST = register("probablychests:chests/nether_pc_chests");
      SHADOW_CHEST = register("probablychests:chests/shadow_pc_chests");
      ICE_CHEST = register("probablychests:chests/ice_pc_chests");
      CORAL_CHEST = register("probablychests:chests/coral_pc_chests");
   }
}
