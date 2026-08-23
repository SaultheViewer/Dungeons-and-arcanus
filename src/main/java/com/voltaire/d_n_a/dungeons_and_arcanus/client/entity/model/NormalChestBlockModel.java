package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.NormalChestBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class NormalChestBlockModel extends GeoModel<NormalChestBlockEntity> {
   private static final ResourceLocation MODEL_IDENTIFIER = new ResourceLocation("d_n_a", "geo/normal_chest_block.json");
   private static final ResourceLocation ANIMATION_IDENTIFIER = new ResourceLocation("d_n_a", "animations/normal_chest_block.animation.json");
   private ResourceLocation TEXTURE_IDENTIFIER = new ResourceLocation("d_n_a", "textures/block/normal_chest.png");

   public NormalChestBlockModel(String texture) {
   }

   public ResourceLocation getTextureResource(NormalChestBlockEntity entity) {
      return this.TEXTURE_IDENTIFIER;
   }

   public ResourceLocation getModelResource(NormalChestBlockEntity entity) {
      return MODEL_IDENTIFIER;
   }

   public void setCustomAnimations(NormalChestBlockEntity entity, long instanceId, AnimationState<NormalChestBlockEntity> state) {
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

   public ResourceLocation getAnimationResource(NormalChestBlockEntity entity) {
      return ANIMATION_IDENTIFIER;
   }
}
