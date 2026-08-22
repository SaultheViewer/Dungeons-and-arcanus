package com.voltaire.d_n_a.dungeons_and_arcanus.client;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ModEntitys;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.ModBlockEntitys;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.PCChestRenderer;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCScreenHandlerType;
import com.voltaire.d_n_a.dungeons_and_arcanus.screen.PCChestScreen;
import com.voltaire.d_n_a.dungeons_and_arcanus.screen.PCMimicScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PC_Client {

    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(PCScreenHandlerType.PC_CHEST, PCChestScreen::new);
            MenuScreens.register(PCScreenHandlerType.PC_CHEST_MIMIC, PCMimicScreen::new);
        });
    }
    
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntitys.LUSH_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.LUSH.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.NORMAL_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.NORMAL.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.AZURE_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.AZURE.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.STONE_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.STONE.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.GOLD_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.GOLD.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.NETHER_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.NETHER.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.SHADOW_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.SHADOW.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.ICE_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.ICE.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.CORAL_CHEST_BLOCK_ENTITY.get(), rendererDispatcherIn -> new PCChestRenderer(PCChestTypes.CORAL.name));

        event.registerEntityRenderer(ModEntitys.NORMAL_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "normal_mimic"));
        event.registerEntityRenderer(ModEntitys.LUSH_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "lush_mimic"));
        event.registerEntityRenderer(ModEntitys.AZURE_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "azure_mimic"));
        event.registerEntityRenderer(ModEntitys.STONE_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "stone_mimic"));
        event.registerEntityRenderer(ModEntitys.GOLD_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "gold_mimic"));
        event.registerEntityRenderer(ModEntitys.NETHER_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "nether_mimic"));
        event.registerEntityRenderer(ModEntitys.SHADOW_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "shadow_mimic"));
        event.registerEntityRenderer(ModEntitys.ICE_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "ice_mimic"));
        event.registerEntityRenderer(ModEntitys.CORAL_CHEST_MIMIC.get(), rendererDispatcherIn -> new ChestMimicRenderer(rendererDispatcherIn, "coral_mimic"));

        event.registerEntityRenderer(ModEntitys.NORMAL_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "normal_mimic"));
        event.registerEntityRenderer(ModEntitys.LUSH_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "lush_mimic"));
        event.registerEntityRenderer(ModEntitys.AZURE_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "azure_mimic"));
        event.registerEntityRenderer(ModEntitys.STONE_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "stone_mimic"));
        event.registerEntityRenderer(ModEntitys.GOLD_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "gold_mimic"));
        event.registerEntityRenderer(ModEntitys.NETHER_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "nether_mimic"));
        event.registerEntityRenderer(ModEntitys.SHADOW_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "shadow_mimic"));
        event.registerEntityRenderer(ModEntitys.ICE_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "ice_mimic"));
        event.registerEntityRenderer(ModEntitys.CORAL_CHEST_MIMIC_PET.get(), rendererDispatcherIn -> new ChestMimic_PetRenderer(rendererDispatcherIn, "coral_mimic"));
    }
}
