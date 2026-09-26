package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PCStatistics {
   public static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
           DeferredRegister.create(Registries.CUSTOM_STAT, Dungeons_and_arcanus.MOD_ID);

   public static final RegistryObject<ResourceLocation> MIMIC_ENCOUNTERS =
           CUSTOM_STATS.register("mimic_encounters",
                   () -> new ResourceLocation(Dungeons_and_arcanus.MOD_ID, "mimic_encounters"));
   public static final RegistryObject<ResourceLocation> ABANDONED_MIMICS =
           CUSTOM_STATS.register("abandoned_mimics",
                   () -> new ResourceLocation(Dungeons_and_arcanus.MOD_ID, "abandoned_mimics"));
}
