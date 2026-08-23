package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class AzureChestBlockEntity extends PC_BaseChestBlockEntity {
   public AzureChestBlockEntity(BlockPos pos, BlockState state) {
      super(PCChestTypes.AZURE, pos, state);
   }
}
