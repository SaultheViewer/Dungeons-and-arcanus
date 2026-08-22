package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCChestState;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCLockedState;
import net.minecraft.world.level.block.state.properties.EnumProperty;


public class PCProperties {
   public static final EnumProperty<PCChestState> PC_CHEST_STATE = EnumProperty.create("pc_chest_state", PCChestState.class);
   public static final EnumProperty<PCLockedState> PC_LOCKED_STATE = EnumProperty.create("pc_locked_state", PCLockedState.class);
}
