package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.IceChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.IceChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class IceChestRenderer extends GeoBlockRenderer<IceChestBlockEntity> {
   public IceChestRenderer(String texture) {
      super(new IceChestBlockModel(texture));
      this.withScale(1.0F);
   }

   public RenderType getRenderType(IceChestBlockEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
   }
}
