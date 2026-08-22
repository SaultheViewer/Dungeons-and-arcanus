package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class PCStatistics {
   public static final ResourceLocation MIMIC_ENCOUNTERS = Dungeons_and_arcanus.id("mimic_encounters");
   public static final ResourceLocation ABANDONED_MIMICS = Dungeons_and_arcanus.id("abandoned_mimics");

   public static void init() {
      Registry.register(BuiltInRegistries.CUSTOM_STAT, "mimic_encounters", MIMIC_ENCOUNTERS);
      Stats.CUSTOM.get(MIMIC_ENCOUNTERS, StatFormatter.DEFAULT);
      Registry.register(BuiltInRegistries.CUSTOM_STAT, "abandoned_mimics", ABANDONED_MIMICS);
      Stats.CUSTOM.get(ABANDONED_MIMICS, StatFormatter.DEFAULT);
   }
}
