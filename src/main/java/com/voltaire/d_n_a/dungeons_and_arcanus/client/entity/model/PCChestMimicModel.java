package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.PCChestMimic;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PCChestMimicModel extends GeoModel<PCChestMimic> {

    private static final ResourceLocation MODEL_IDENTIFIER =
            new ResourceLocation("dungeons_and_arcanus", "geo/pc_chest_mimic.json");
    private static final ResourceLocation ANIMATION_IDENTIFIER =
            new ResourceLocation("dungeons_and_arcanus", "animations/pc_chest_mimic.animation.json");

    private final ResourceLocation textureIdentifier;

    public PCChestMimicModel(String texture) {
        this.textureIdentifier = new ResourceLocation("dungeons_and_arcanus", "textures/entity/" + texture + ".png");
    }

    @Override
    public ResourceLocation getModelResource(PCChestMimic entity) {
        return MODEL_IDENTIFIER;
    }

    @Override
    public ResourceLocation getTextureResource(PCChestMimic entity) {
        return this.textureIdentifier;
    }

    @Override
    public ResourceLocation getAnimationResource(PCChestMimic entity) {
        return ANIMATION_IDENTIFIER;
    }
}
