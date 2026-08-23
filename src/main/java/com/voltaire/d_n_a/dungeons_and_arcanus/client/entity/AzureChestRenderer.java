package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.AzureChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.AzureChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AzureChestRenderer extends GeoBlockRenderer<AzureChestBlockEntity> {
   public AzureChestRenderer(String texture) {
      super(new AzureChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(AzureChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
