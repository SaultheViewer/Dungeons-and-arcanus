package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public enum PC_PotTypes {

        LUSH,
        NORMAL,
        ROCKY,
        NETHER;

        public BlockBehaviour.Properties setting() {
            return switch (this) {
                case LUSH, NORMAL -> BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_BROWN)
                        .strength(1.0F, 1.0F)
                        .sound(SoundType.DECORATED_POT)
                        .instabreak()
                        .noOcclusion();

                case  NETHER, ROCKY -> BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_BLACK )
                        .strength(1.0F, 1.0F)
                        .sound(SoundType.STONE)
                        .instabreak()
                        .noOcclusion();

            };
        }
    }

