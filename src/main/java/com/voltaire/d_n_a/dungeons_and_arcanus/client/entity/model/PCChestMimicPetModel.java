package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PCChestMimicPetModel extends GeoModel<ChestMimicPet> {

    private static final ResourceLocation MODEL_IDENTIFIER =
            new ResourceLocation("dungeons_and_arcanus", "geo/pc_chest_mimic.json");
    private static final ResourceLocation ANIMATION_IDENTIFIER =
            new ResourceLocation("dungeons_and_arcanus", "animations/pc_chest_mimic.animation.json");

    private final ResourceLocation textureIdentifier;

    public PCChestMimicPetModel(String texture) {
        this.textureIdentifier = new ResourceLocation("dungeons_and_arcanus", "textures/entity/" + texture + ".png");
    }

    @Override
    public ResourceLocation getModelResource(ChestMimicPet entity) {
        return MODEL_IDENTIFIER;
    }

    @Override
    public ResourceLocation getTextureResource(ChestMimicPet entity) {
        return this.textureIdentifier;
    }

    @Override
    public ResourceLocation getAnimationResource(ChestMimicPet entity) {
        return ANIMATION_IDENTIFIER;
    }
}
