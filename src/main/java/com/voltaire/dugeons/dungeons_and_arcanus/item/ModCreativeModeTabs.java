package com.voltaire.dugeons.dungeons_and_arcanus.item;

import com.voltaire.dugeons.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dungeons_and_arcanus.MOD_ID);

    public static final RegistryObject<CreativeModeTab> COOKIE_ITEMS_TAB = CREATIVE_MODE_TABS.register("dungeons_and_arcanus",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.dungeons_and_arcanus"))
                    .displayItems((pParameters, pOutput) -> {
                        //example item output remove when first item is added
                        // pOutput.accept(ModItems.CCC.get());

                        //example block output
                        //pOutput.accept(ModBlocks.TEST_BLOCK.get());
                        // example vanilla item inclusion
                        // pOutput.accept(Items.SUGAR);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
