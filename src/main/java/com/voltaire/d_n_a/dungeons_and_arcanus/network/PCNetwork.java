package com.voltaire.d_n_a.dungeons_and_arcanus.network;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PCNetwork {
   private static final String PROTOCOL_VERSION = "1";
   public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
      new ResourceLocation(Dungeons_and_arcanus.MOD_ID, "main"),
      () -> PROTOCOL_VERSION,
      PROTOCOL_VERSION::equals,
      PROTOCOL_VERSION::equals
   );

   private static int packetId = 0;

   public static void register() {
      CHANNEL.registerMessage(
         packetId++,
         SyncMimicInventoryPacket.class,
         SyncMimicInventoryPacket::toBytes,
         SyncMimicInventoryPacket::new,
         SyncMimicInventoryPacket::handle,
         Optional.of(NetworkDirection.PLAY_TO_CLIENT)
      );
   }

   public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
      CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
   }

   public static <MSG> void sendToTracking(MSG message, net.minecraft.world.entity.Entity entity) {
      CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
   }
}
