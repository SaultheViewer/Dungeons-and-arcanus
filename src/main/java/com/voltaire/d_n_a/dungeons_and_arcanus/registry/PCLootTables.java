package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class PCLootTables {
   private static final Set<ResourceLocation> PC_LOOT_TABLES = Sets.newHashSet();
   public static final ResourceLocation LUSH_CHEST = register("d_n_a:chests/lush_pc_chests");
   public static final ResourceLocation NORMAL_CHEST = register("d_n_a:chests/normal_pc_chests");
   public static final ResourceLocation AZURE_CHEST = register("d_n_a:chests/azure_pc_chests");
   public static final ResourceLocation STONE_CHEST = register("d_n_a:chests/stone_pc_chests");
   public static final ResourceLocation GOLD_CHEST = register("d_n_a:chests/gold_pc_chests");
   public static final ResourceLocation NETHER_CHEST = register("d_n_a:chests/nether_pc_chests");
   public static final ResourceLocation SHADOW_CHEST = register("d_n_a:chests/shadow_pc_chests");
   public static final ResourceLocation ICE_CHEST = register("d_n_a:chests/ice_pc_chests");
   public static final ResourceLocation CORAL_CHEST = register("d_n_a:chests/coral_pc_chests");

   private static ResourceLocation register(String id) {
      return registerLootTable(new ResourceLocation(id));
   }

   private static ResourceLocation registerLootTable(ResourceLocation id) {
      if (PC_LOOT_TABLES.add(id)) {
         return id;
      } else {
         throw new IllegalArgumentException(id + " is already a registered built-in loot table");
      }
   }

   public static void init() {
   }
}
