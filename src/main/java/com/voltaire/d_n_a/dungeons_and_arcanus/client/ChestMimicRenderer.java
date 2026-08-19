package com.voltaire.d_n_a.dungeons_and_arcanus.client;

import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.PCChestMimicModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.model.GeoModel;

public class ChestMimicRenderer extends GeoEntityRenderer<ChestMimic> {

    public ChestMimicRenderer(EntityRendererProvider.Context renderManager, String texture) {
        super (renderManager, new PCChestMimicModel(texture));
    }
}
