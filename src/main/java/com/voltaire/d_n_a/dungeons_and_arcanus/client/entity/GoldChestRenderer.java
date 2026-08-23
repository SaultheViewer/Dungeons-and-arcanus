package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.GoldChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.GoldChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GoldChestRenderer extends GeoBlockRenderer<GoldChestBlockEntity> {
   public GoldChestRenderer(String texture) {
      super(new GoldChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(GoldChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
