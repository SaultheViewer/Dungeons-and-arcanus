package com.voltaire.d_n_a.dungeons_and_arcanus.client;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.PCChestRenderer;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.ModBlockEntitys;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.ChestMimicRenderer;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.ChestMimic_PetRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Dungeons_and_arcanus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PC_Client {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Screen / Menu registration
            MenuScreens.register(PCScreenHandlerType.PC_CHEST, PCChestScreen::new);
            MenuScreens.register(PCScreenHandlerType.PC_CHEST_MIMIC, PCMimicScreen::new);
        });

        // ---------------------------------------------------------------
        // Networking – replace with your Forge SimpleChannel / packet system
        //
        // Example (you will need matching server packets):
        //
        // dungeons_and_arcanus.NETWORK.registerMessage(
        //     id++, ConfigUpdatePacket.class,
        //     ConfigUpdatePacket::encode, ConfigUpdatePacket::decode,
        //     ConfigUpdatePacket::handle
        // );
        //
        // For the mimic inventory packet:
        // On client handle:
        //   int size = buf.readInt();
        //   int entityId = buf.readVarInt();
        //   int syncId = buf.readUnsignedByte();
        //   Minecraft.getInstance().execute(() -> {
        //       var player = Minecraft.getInstance().player;
        //       if (player == null) return;
        //       Entity entity = player.level().getEntity(entityId);
        //       if (entity instanceof Tamable_Pet_With_Inv mimicEntity
        //               && player.containerMenu instanceof PCMimicScreenHandler mimicScreenHandler) {
        //           mimicScreenHandler.setMimicEntity(mimicEntity);
        //       }
        //   });
        // ---------------------------------------------------------------
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // ---------------- Block Entity Renderers ----------------
        event.registerBlockEntityRenderer(ModBlockEntitys.LUSH_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.LUSH.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.NORMAL_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.NORMAL.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.AZURE_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.AZURE.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.STONE_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.STONE.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.GOLD_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.GOLD.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.NETHER_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.NETHER.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.SHADOW_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.SHADOW.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.ICE_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.ICE.name));
        event.registerBlockEntityRenderer(ModBlockEntitys.CORAL_CHEST_BLOCK_ENTITY,
                ctx -> new PCChestRenderer(PCChestTypes.CORAL.name));

        // ---------------- Hostile Mimic Entity Renderers ----------------
        event.registerEntityRenderer(ModEntitys.NORMAL_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "normal_mimic"));
        event.registerEntityRenderer(ModEntitys.LUSH_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "lush_mimic"));
        event.registerEntityRenderer(ModEntitys.AZURE_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "azure_mimic"));
        event.registerEntityRenderer(ModEntitys.STONE_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "stone_mimic"));
        event.registerEntityRenderer(ModEntitys.GOLD_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "gold_mimic"));
        event.registerEntityRenderer(ModEntitys.NETHER_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "nether_mimic"));
        event.registerEntityRenderer(ModEntitys.SHADOW_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "shadow_mimic"));
        event.registerEntityRenderer(ModEntitys.ICE_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "ice_mimic"));
        event.registerEntityRenderer(ModEntitys.CORAL_CHEST_MIMIC,
                ctx -> new ChestMimicRenderer(ctx, "coral_mimic"));

        // ---------------- Pet Mimic Entity Renderers ----------------
        event.registerEntityRenderer(ModEntitys.NORMAL_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "normal_mimic"));
        event.registerEntityRenderer(ModEntitys.LUSH_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "lush_mimic"));
        event.registerEntityRenderer(ModEntitys.AZURE_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "azure_mimic"));
        event.registerEntityRenderer(ModEntitys.STONE_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "stone_mimic"));
        event.registerEntityRenderer(ModEntitys.GOLD_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "gold_mimic"));
        event.registerEntityRenderer(ModEntitys.NETHER_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "nether_mimic"));
        event.registerEntityRenderer(ModEntitys.SHADOW_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "shadow_mimic"));
        event.registerEntityRenderer(ModEntitys.ICE_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "ice_mimic"));
        event.registerEntityRenderer(ModEntitys.CORAL_CHEST_MIMIC_PET,
                ctx -> new ChestMimic_PetRenderer(ctx, "coral_mimic"));
    }
}
