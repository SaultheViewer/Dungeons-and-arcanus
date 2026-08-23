package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.CoralChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.CoralChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CoralChestRenderer extends GeoBlockRenderer<CoralChestBlockEntity> {
   public CoralChestRenderer(String texture) {
      super(new CoralChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(CoralChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
