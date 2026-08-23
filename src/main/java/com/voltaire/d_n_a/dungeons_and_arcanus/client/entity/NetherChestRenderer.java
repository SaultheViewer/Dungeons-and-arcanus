package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.NetherChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.NetherChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class NetherChestRenderer extends GeoBlockRenderer<NetherChestBlockEntity> {
   public NetherChestRenderer(String texture) {
      super(new NetherChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(NetherChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
