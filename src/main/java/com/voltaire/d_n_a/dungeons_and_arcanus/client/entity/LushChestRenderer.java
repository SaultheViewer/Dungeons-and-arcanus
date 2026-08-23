package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.LushChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.LushChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LushChestRenderer extends GeoBlockRenderer<LushChestBlockEntity> {
   public LushChestRenderer(String texture) {
      super(new LushChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(LushChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
