package com.voltaire.d_n_a.dungeons_and_arcanus.datagen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.ModBiomeModifiers;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.ModConfiguredFeatures;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.ModPlaceFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;


import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldgenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlaceFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Dungeons_and_arcanus.MOD_ID));
    }
}
