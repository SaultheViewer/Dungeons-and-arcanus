package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCEventHandler {
   public static final ResourceLocation MIMIC_INVENTORY_PACKET_ID = ProbablyChests.id("mimic_inventory_packet");

   private PCEventHandler() {
   }

   public static void registerEvents() {
      ServerPlayConnectionEvents.JOIN.register((ServerPlayConnectionEvents.Join)(handler, sender, server) -> {
         FriendlyByteBuf data = PacketByteBufs.create();
         data.writeNbt(ProbablyChests.configToNBT());
         ServerPlayNetworking.send(handler.player, ProbablyChests.id("probably_chests_config_update"), data);
      });
   }
}
