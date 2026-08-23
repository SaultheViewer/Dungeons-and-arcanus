package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.IceChestBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class IceChestBlockModel extends GeoModel<IceChestBlockEntity> {
   private static final ResourceLocation MODEL_IDENTIFIER = new ResourceLocation("d_n_a", "geo/ice_chest_block.json");
   private static final ResourceLocation ANIMATION_IDENTIFIER = new ResourceLocation("d_n_a", "animations/ice_chest_block.animation.json");
   private ResourceLocation TEXTURE_IDENTIFIER = new ResourceLocation("d_n_a", "textures/block/ice_chest.png");

   public IceChestBlockModel(String texture) {
   }

   public ResourceLocation getTextureResource(IceChestBlockEntity entity) {
      return this.TEXTURE_IDENTIFIER;
   }

   public ResourceLocation getModelResource(IceChestBlockEntity entity) {
      return MODEL_IDENTIFIER;
   }

   public void setCustomAnimations(IceChestBlockEntity entity, long instanceId, AnimationState<IceChestBlockEntity> state) {
      super.setCustomAnimations(entity, instanceId, state);
      CoreGeoBone lid = this.getAnimationProcessor().getBone("Lid");
      if (lid != null) {
         float tickDelta = state.getPartialTick();
         float angle = entity.prevLidAngle + (entity.lidAngle - entity.prevLidAngle) * tickDelta;
         float f = 1.0F - angle;
         f = 1.0F - f * f * f;
         float rotation = (float)Math.toRadians(90.0F * f);
         lid.setRotX(rotation);
      }
   }

   public ResourceLocation getAnimationResource(IceChestBlockEntity entity) {
      return ANIMATION_IDENTIFIER;
   }
}
