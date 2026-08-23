package com.voltaire.d_n_a.dungeons_and_arcanus.network;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCMimicScreenHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;


import java.util.function.Supplier;

public class SyncMimicInventoryPacket {
   private final int size;
   private final int entityId;
   private final int syncId;

   public SyncMimicInventoryPacket(int size, int entityId, int syncId) {
      this.size = size;
      this.entityId = entityId;
      this.syncId = syncId;
   }

   public SyncMimicInventoryPacket(FriendlyByteBuf buf) {
      this.size = buf.readInt();
      this.entityId = buf.readVarInt();
      this.syncId = buf.readUnsignedByte();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeInt(this.size);
      buf.writeVarInt(this.entityId);
      buf.writeByte(this.syncId);
   }

   public void handle(Supplier<NetworkEvent.Context> supplier) {
      NetworkEvent.Context context = supplier.get();
      context.enqueueWork(() -> {
         DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.level().getEntity(this.entityId) instanceof Tameable_Pet_With_Inv mimicEntity) {
               if (player.containerMenu instanceof PCMimicScreenHandler mimicScreenHandler) {
                  mimicScreenHandler.setMimicEntity(mimicEntity);
               }
            }
         });
      });
      context.setPacketHandled(true);
   }
}
