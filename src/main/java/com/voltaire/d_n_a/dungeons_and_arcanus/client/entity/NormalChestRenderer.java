package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.NormalChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.NormalChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class NormalChestRenderer extends GeoBlockRenderer<NormalChestBlockEntity> {
   public NormalChestRenderer(String texture) {
      super(new NormalChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(NormalChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
