package com.voltaire.d_n_a.dungeons_and_arcanus.datagen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Dungeons_and_arcanus.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.MITHRIL_INGOT);
        simpleItem(ModItems.RAW_MITHRIL);
        simpleItem(ModItems.RUBY);
        simpleItem(ModItems.RAW_SILVER);
        simpleItem(ModItems.ORICALCIUM_GEM);
        simpleItem(ModItems.ORICALCIUM_PASTE);
        simpleItem(ModItems.SILVER_INGOT);

    }
    //Basic Item method
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Dungeons_and_arcanus.MOD_ID, "item" + item.getId().getPath()));
    }
}
