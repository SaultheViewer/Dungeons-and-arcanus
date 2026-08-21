package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

import net.minecraft.util.StringRepresentable;

public enum PCChestState implements StringRepresentable {
   OPEN("open"),
   OPENED("opened"),
   CLOSE("close"),
   CLOSED("closed");

   private final String name;

   private PCChestState(String name) {
      this.name = name;
   }

   public String getSerializedName() {
      return this.name;
   }

   // $FF: synthetic method
   private static PCChestState[] $values() {
      return new PCChestState[]{OPEN, OPENED, CLOSE, CLOSED};
   }
}
