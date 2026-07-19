package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PCVoxelShapes {

    public static final VoxelShape POT_VOXELSHAPE;

    static {
        VoxelShape base = Block.box(3.0, 12.0, 3.0, 13.0, 16.0, 13.0);
        VoxelShape bottom = Block.box(1.0, 0.0, 1.0, 15.0, 12.0, 15.0);

        POT_VOXELSHAPE = Shapes.join(base, bottom, BooleanOp.OR);
    }
}
