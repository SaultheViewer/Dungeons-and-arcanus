package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ChestMimicPet;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ChestMimicPetModel extends GeoModel<ChestMimicPet> {
    private static final ResourceLocation MODEL_IDENTIFIER = new ResourceLocation("d_n_a", "geo/pc_chest_mimic.json");
    private static final ResourceLocation ANIMATION_IDENTIFIER = new ResourceLocation("d_n_a", "animations/pc_chest_mimic.animation.json");
    private final ResourceLocation TEXTURE_IDENTIFIER;

    public ChestMimicPetModel(String texture) {
        this.TEXTURE_IDENTIFIER = new ResourceLocation("d_n_a", "textures/entity/" + texture + ".png");
    }

    public ResourceLocation getTextureResource(ChestMimicPet entity) {
        return this.TEXTURE_IDENTIFIER;
    }

    public ResourceLocation getModelResource(ChestMimicPet entity) {
        return MODEL_IDENTIFIER;
    }

    public ResourceLocation getAnimationResource(ChestMimicPet entity) {
        return ANIMATION_IDENTIFIER;
    }
}
