package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PCVoxelShapes {
   public static final VoxelShape POT_VOXELSHAPE;

   static {
      POT_VOXELSHAPE = Shapes.join(Block.box((double)3.0F, (double)12.0F, (double)3.0F, (double)13.0F, (double)16.0F, (double)13.0F), Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)12.0F, (double)15.0F), BooleanOp.OR);
   }
}
