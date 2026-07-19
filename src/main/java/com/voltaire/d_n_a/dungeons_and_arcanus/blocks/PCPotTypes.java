package com.voltaire.d_n_a.dungeons_and_arcanus.blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public enum PCPotTypes {
    LUSH(new ResourceLocation("d_n_a", "lush_pot")),
    NORMAL(new ResourceLocation("d_n_a", "normal_pot")),
    ROCKY(new ResourceLocation("d_n_a", "rocky_pot")),
    NETHER(new ResourceLocation("d_n_a", "nether_pot"));

    public final ResourceLocation texture;

    private PCPotTypes(ResourceLocation texture) {this.texture = texture;}

        public Block.Properties settings() {
            switch (this) {
                case LUSH, NORMAL:
                    return Block.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_WHITE)
                            .strength(1.0F, 1.0F)
                            .sound(SoundType.GLASS)
                            .instabreak()
                            .noOcclusion();

                case ROCKY, NETHER:
                    return Block.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_WHITE)
                            .strength(1.0F, 1.0F)
                            .sound(SoundType.WOOD)
                            .instabreak()
                            .noOcclusion();

                default:
                    return Block.Properties.of().mapColor(MapColor.STONE);
            }
        }
    }

