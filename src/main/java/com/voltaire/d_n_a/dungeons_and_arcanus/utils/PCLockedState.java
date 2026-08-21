package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

import net.minecraft.util.StringRepresentable;

public enum PCLockedState implements StringRepresentable {
   LOCKED("locked"),
   UNLOCKED("unlocked");

   private final String name;

   private PCLockedState(String name) {
      this.name = name;
   }

   public String getSerializedName() {
      return this.name;
   }

   // $FF: synthetic method
   private static PCLockedState[] $values() {
      return new PCLockedState[]{LOCKED, UNLOCKED};
   }
}
