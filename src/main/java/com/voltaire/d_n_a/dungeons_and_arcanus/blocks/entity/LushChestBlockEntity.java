package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class LushChestBlockEntity extends PC_BaseChestBlockEntity {
   public LushChestBlockEntity(BlockPos pos, BlockState state) {
      super(PCChestTypes.LUSH, pos, state);
   }
}
