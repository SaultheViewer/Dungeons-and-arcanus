package com.voltaire.d_n_a.dungeons_and_arcanus.datagen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Dungeons_and_arcanus.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.MITHRIL_INGOT.get());
        basicItem(ModItems.RAW_MITHRIL.get());
        basicItem(ModItems.RUBY.get());
        basicItem(ModItems.RAW_SILVER.get());
        basicItem(ModItems.ORICALCIUM_GEM.get());
        basicItem(ModItems.ORICALCIUM_PASTE.get());
        basicItem(ModItems.SILVER_INGOT.get());

    }
}
