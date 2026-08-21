package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.gen;

public class PCWorldGen {
   public static void generatePCWorldGen() {
      UndergroundChestGeneration.generateChest();
      PCPotGeneration.generatePot();
   }
}
