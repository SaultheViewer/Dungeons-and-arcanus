package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.ShadowChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.ShadowChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ShadowChestRenderer extends GeoBlockRenderer<ShadowChestBlockEntity> {
   public ShadowChestRenderer(String texture) {
      super(new ShadowChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(ShadowChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
