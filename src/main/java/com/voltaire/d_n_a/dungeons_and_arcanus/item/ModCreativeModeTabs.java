package com.voltaire.d_n_a.dungeons_and_arcanus.item;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dungeons_and_arcanus.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DUNGEONS_AND_ARCANUS = CREATIVE_MODE_TABS.register("dungeons_and_arcanus",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.DEEPSLATE_MITHRIL_ORE.get()))
                    .title(Component.translatable("creativetab.dungeons_and_arcanus"))
                    .displayItems((pParameters, pOutput) -> {
                        //example item output remove when first item is added
                        pOutput.accept(ModItems.RAW_MITHRIL.get());
                        pOutput.accept(ModItems.RUBY.get());
                        pOutput.accept(ModItems.MITHRIL_INGOT.get());
                        pOutput.accept(ModItems.SILVER_INGOT.get());
                        pOutput.accept(ModItems.ORICALCIUM_GEM.get());
                        pOutput.accept(ModItems.ORICALCIUM_PASTE.get());
                        pOutput.accept(ModItems.RAW_SILVER.get());


                        //example block output
                        pOutput.accept(ModBlocks.DEEPSLATE_SILVER_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_MITHRIL_ORE.get());
                        pOutput.accept(ModBlocks.MITHRIL_BLOCK.get());
                        pOutput.accept(ModBlocks.MITHRIL_ORE.get());
                        pOutput.accept(ModBlocks.ORICALCIUM_BLOCK.get());
                        pOutput.accept(ModBlocks.ORICALCIUM_ORE.get());
                        pOutput.accept(ModBlocks.RAW_SILVER_BLOCK.get());
                        pOutput.accept(ModBlocks.SILVER_ORE.get());
                        pOutput.accept(ModBlocks.SILVER_BLOCK.get());
                        pOutput.accept(ModBlocks.RUBY_BLOCK.get());
                        pOutput.accept(ModBlocks.RUBY_ORE.get());



                        // example vanilla item inclusion
                        // pOutput.accept(Items.SUGAR);

                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> PROABLY_CHESTS = CREATIVE_MODE_TABS.register("proably_chests",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CORAL_CHEST.get()))
                    .title(Component.translatable("creativetab.pc"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModItems.GOLD_LOCK.get());
                        pOutput.accept(ModItems.GOLD_KEY.get());
                        pOutput.accept(ModItems.IRON_KEY.get());
                        pOutput.accept(ModItems.IRON_LOCK.get());
                        pOutput.accept(ModItems.MIMIC_CORE.get());
                        pOutput.accept(ModItems.MIMIC_HAND_BELL.get());
                        pOutput.accept(ModItems.MIMIC_KEY.get());
                        pOutput.accept(ModItems.MIMIC_KEY_FRAGMENT.get());
                        pOutput.accept(ModItems.PET_MIMIC_KEY.get());
                        pOutput.accept(ModItems.VOID_KEY.get());
                        pOutput.accept(ModItems.VOID_LOCK.get());
                        pOutput.accept(ModBlocks.LUSH_POT.get());
                        pOutput.accept(ModBlocks.NORMAL_POT.get());
                        pOutput.accept(ModBlocks.NETHER_POT.get());
                        pOutput.accept(ModBlocks.ROCKY_POT.get());

                        pOutput.accept(ModBlocks.ICE_CHEST.get());
                        pOutput.accept(ModBlocks.NETHER_CHEST.get());
                        pOutput.accept(ModBlocks.NORMAL_CHEST.get());
                        pOutput.accept(ModBlocks.LUSH_CHEST.get());
                        pOutput.accept(ModBlocks.AZURE_CHEST.get());
                        pOutput.accept(ModBlocks.GOLD_CHEST.get());
                        pOutput.accept(ModBlocks.SHADOW_CHEST.get());
                        pOutput.accept(ModBlocks.CORAL_CHEST.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}