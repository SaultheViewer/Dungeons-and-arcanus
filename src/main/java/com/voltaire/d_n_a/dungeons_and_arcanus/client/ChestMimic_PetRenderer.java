package com.voltaire.d_n_a.dungeons_and_arcanus.client;

import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.PCChestMimicPetModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.model.GeoModel;

public class ChestMimic_PetRenderer extends GeoEntityRenderer<ChestMimicPet> {

    public ChestMimic_PetRenderer(EntityRendererProvider.Context renderManager, String texture) {
        super(renderManager, new PCChestMimicPetModel(texture));
    }
}
