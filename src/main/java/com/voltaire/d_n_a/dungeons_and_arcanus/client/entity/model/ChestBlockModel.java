package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PC_BaseChestBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class ChestBlockModel extends GeoModel<PC_BaseChestBlockEntity> {

    private final ResourceLocation model;
    private final ResourceLocation animation;
    private final ResourceLocation texture;

    /**
     * @param name chest name, e.g. "azure_chest", "lush_chest" (from PCChestTypes.name)
     */
    public ChestBlockModel(String name) {
        this.model = new ResourceLocation("dungeons_and_arcanus", "geo/" + name + "_block.json");
        this.animation = new ResourceLocation("dungeons_and_arcanus", "animations/" + name + "_block.animation.json");
        this.texture = new ResourceLocation("dungeons_and_arcanus", "textures/block/" + name + ".png");
    }

    @Override
    public ResourceLocation getModelResource(PC_BaseChestBlockEntity entity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(PC_BaseChestBlockEntity entity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(PC_BaseChestBlockEntity entity) {
        return this.animation;
    }

    @Override
    public void setCustomAnimations(PC_BaseChestBlockEntity entity, long instanceId,
                                    AnimationState<PC_BaseChestBlockEntity> state) {
        super.setCustomAnimations(entity, instanceId, state);

        CoreGeoBone lid = this.getAnimationProcessor().getBone("Lid");
        if (lid != null) {
            float tickDelta = state.getPartialTick();
            float angle = entity.prevLidAngle + (entity.lidAngle - entity.prevLidAngle) * tickDelta;
            float f = 1.0F - angle;
            f = 1.0F - f * f * f;
            lid.setRotX((float) Math.toRadians(90.0F * f));
        }
    }
}
