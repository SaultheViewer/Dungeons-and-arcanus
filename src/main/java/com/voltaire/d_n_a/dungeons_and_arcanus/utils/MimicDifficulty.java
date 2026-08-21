package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

public enum MimicDifficulty {
   EASY,
   MEDIUM,
   HARD,
   INSANE;

   public static MimicDifficulty fromInt(int i) {
      MimicDifficulty var10000;
      switch (i) {
         case 0 -> var10000 = EASY;
         case 1 -> var10000 = MEDIUM;
         case 2 -> var10000 = HARD;
         case 3 -> var10000 = INSANE;
         default -> var10000 = MEDIUM;
      }

      return var10000;
   }

   public int toInt() {
      byte var10000;
      switch (this.ordinal()) {
         case 0 -> var10000 = 0;
         case 1 -> var10000 = 1;
         case 2 -> var10000 = 2;
         case 3 -> var10000 = 3;
         default -> var10000 = 1;
      }

      return var10000;
   }

   public int getHealth() {
      byte var10000;
      switch (this.ordinal()) {
         case 0 -> var10000 = 15;
         case 1 -> var10000 = 30;
         case 2 -> var10000 = 50;
         case 3 -> var10000 = 100;
         default -> var10000 = 30;
      }

      return var10000;
   }

   public int getDamage() {
      byte var10000;
      switch (this.ordinal()) {
         case 0 -> var10000 = 3;
         case 1 -> var10000 = 5;
         case 2 -> var10000 = 9;
         case 3 -> var10000 = 12;
         default -> var10000 = 5;
      }

      return var10000;
   }

   public double getSpeed() {
      double var10000;
      switch (this.ordinal()) {
         case 0 -> var10000 = 1.3;
         case 1 -> var10000 = (double)1.5F;
         case 2 -> var10000 = 1.9;
         case 3 -> var10000 = (double)3.0F;
         default -> var10000 = 0.8;
      }

      return var10000;
   }

   // $FF: synthetic method
   private static MimicDifficulty[] $values() {
      return new MimicDifficulty[]{EASY, MEDIUM, HARD, INSANE};
   }
}
