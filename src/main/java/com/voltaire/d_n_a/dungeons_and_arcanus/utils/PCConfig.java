package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

@Config(
   name = "dungeons_and_arcanus"
)
public class PCConfig implements ConfigData {
   @CollapsibleObject
   public MimicSettings mimicSettings = new MimicSettings();
   @CollapsibleObject
   public WorldGen worldGen = new WorldGen();
   @CollapsibleObject
   public ChestSettings chestSettings = new ChestSettings();

   public static class MimicSettings {
      @Tooltip
      public MimicDifficulty mimicDifficulty;
      @Tooltip
      public boolean spawnNaturalMimics;
      @Tooltip
      public float naturalMimicSpawnRate;
      @Tooltip
      public boolean allowPetMimics;
      @Tooltip
      public boolean doPetMimicLimit;
      @Tooltip
      public int petMimicLimit;
      @Tooltip
      public int abandonedMimicTimer;
      @Tooltip
      public boolean allowPetMimicLocking;

      public MimicSettings() {
         this.mimicDifficulty = MimicDifficulty.MEDIUM;
         this.spawnNaturalMimics = true;
         this.naturalMimicSpawnRate = 0.5F;
         this.allowPetMimics = true;
         this.doPetMimicLimit = false;
         this.petMimicLimit = 2;
         this.abandonedMimicTimer = 5;
         this.allowPetMimicLocking = true;
      }
   }

   public static class WorldGen {
      @Tooltip
      public float potSpawnChance = 0.4F;
      @Tooltip
      public float chestSpawnChance = 0.6F;
      @Tooltip
      public float secretMimicChance = 0.2F;
   }

   public static class ChestSettings {
      @Tooltip
      public boolean allowChestLocking = true;
      @Tooltip
      public boolean enableLockedChestOwners = true;
   }
}
