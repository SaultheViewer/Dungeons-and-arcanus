package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.StoneChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.StoneChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StoneChestRenderer extends GeoBlockRenderer<StoneChestBlockEntity> {
   public StoneChestRenderer(String texture) {
      super(new StoneChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(StoneChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
