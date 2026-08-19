package com.voltaire.d_n_a.dungeons_and_arcanus.client.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PC_BaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.entity.model.ChestBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


public class PCChestRenderer extends GeoBlockRenderer<PC_BaseChestBlockEntity> {

    public PCChestRenderer(String texture) {
        super(new ChestBlockModel(texture));
        this.withScale(1.0F);
    }

    @Override
    public RenderType getRenderType(PC_BaseChestBlockEntity animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable), false);
    }
}
