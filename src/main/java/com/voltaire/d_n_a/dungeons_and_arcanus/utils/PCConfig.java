package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class PCConfig {
   public static final ForgeConfigSpec SPEC;
   public static final SpecHolder SPEC_HOLDER;
   public static final PCConfig loadedConfig = new PCConfig();

   public MimicSettings mimicSettings = new MimicSettings();
   public WorldGen worldGen = new WorldGen();
   public ChestSettings chestSettings = new ChestSettings();

   static {
      Pair<SpecHolder, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(SpecHolder::new);
      SPEC_HOLDER = pair.getLeft();
      SPEC = pair.getRight();
   }

   public static void onConfigLoad(ModConfigEvent event) {
      if (event.getConfig().getSpec() == SPEC) {
         updateLoadedConfig();
      }
   }

   public static void updateLoadedConfig() {
      loadedConfig.chestSettings.allowChestLocking = SPEC_HOLDER.allowChestLocking.get();
      loadedConfig.chestSettings.enableLockedChestOwners = SPEC_HOLDER.enableLockedChestOwners.get();
      loadedConfig.mimicSettings.mimicDifficulty = SPEC_HOLDER.mimicDifficulty.get();
      loadedConfig.mimicSettings.spawnNaturalMimics = SPEC_HOLDER.spawnNaturalMimics.get();
      loadedConfig.mimicSettings.naturalMimicSpawnRate = SPEC_HOLDER.naturalMimicSpawnRate.get().floatValue();
      loadedConfig.mimicSettings.allowPetMimics = SPEC_HOLDER.allowPetMimics.get();
      loadedConfig.mimicSettings.doPetMimicLimit = SPEC_HOLDER.doPetMimicLimit.get();
      loadedConfig.mimicSettings.petMimicLimit = SPEC_HOLDER.petMimicLimit.get();
      loadedConfig.mimicSettings.abandonedMimicTimer = SPEC_HOLDER.abandonedMimicTimer.get();
      loadedConfig.mimicSettings.allowPetMimicLocking = SPEC_HOLDER.allowPetMimicLocking.get();
      loadedConfig.worldGen.potSpawnChance = SPEC_HOLDER.potSpawnChance.get().floatValue();
      loadedConfig.worldGen.chestSpawnChance = SPEC_HOLDER.chestSpawnChance.get().floatValue();
      loadedConfig.worldGen.secretMimicChance = SPEC_HOLDER.secretMimicChance.get().floatValue();
   }

   public static class SpecHolder {
      public final ForgeConfigSpec.BooleanValue allowChestLocking;
      public final ForgeConfigSpec.BooleanValue enableLockedChestOwners;
      public final ForgeConfigSpec.EnumValue<MimicDifficulty> mimicDifficulty;
      public final ForgeConfigSpec.BooleanValue spawnNaturalMimics;
      public final ForgeConfigSpec.DoubleValue naturalMimicSpawnRate;
      public final ForgeConfigSpec.BooleanValue allowPetMimics;
      public final ForgeConfigSpec.BooleanValue doPetMimicLimit;
      public final ForgeConfigSpec.IntValue petMimicLimit;
      public final ForgeConfigSpec.IntValue abandonedMimicTimer;
      public final ForgeConfigSpec.BooleanValue allowPetMimicLocking;
      public final ForgeConfigSpec.DoubleValue potSpawnChance;
      public final ForgeConfigSpec.DoubleValue chestSpawnChance;
      public final ForgeConfigSpec.DoubleValue secretMimicChance;

      public SpecHolder(ForgeConfigSpec.Builder builder) {
         builder.push("chestSettings");
         allowChestLocking = builder.comment("Allow chest locking").define("allowChestLocking", true);
         enableLockedChestOwners = builder.comment("Enable locked chest owners").define("enableLockedChestOwners", true);
         builder.pop();

         builder.push("mimicSettings");
         mimicDifficulty = builder.comment("Mimic difficulty").defineEnum("mimicDifficulty", MimicDifficulty.MEDIUM);
         spawnNaturalMimics = builder.comment("Spawn natural mimics").define("spawnNaturalMimics", true);
         naturalMimicSpawnRate = builder.comment("Natural mimic spawn rate").defineInRange("naturalMimicSpawnRate", 0.5, 0.0, 1.0);
         allowPetMimics = builder.comment("Allow pet mimics").define("allowPetMimics", true);
         doPetMimicLimit = builder.comment("Do pet mimic limit").define("doPetMimicLimit", false);
         petMimicLimit = builder.comment("Pet mimic limit").defineInRange("petMimicLimit", 2, 0, 100);
         abandonedMimicTimer = builder.comment("Abandoned mimic timer in minutes").defineInRange("abandonedMimicTimer", 5, 0, 1440);
         allowPetMimicLocking = builder.comment("Allow pet mimic locking").define("allowPetMimicLocking", true);
         builder.pop();

         builder.push("worldGen");
         potSpawnChance = builder.comment("Pot spawn chance").defineInRange("potSpawnChance", 0.4, 0.0, 1.0);
         chestSpawnChance = builder.comment("Chest spawn chance").defineInRange("chestSpawnChance", 0.6, 0.0, 1.0);
         secretMimicChance = builder.comment("Secret mimic chance").defineInRange("secretMimicChance", 0.2, 0.0, 1.0);
         builder.pop();
      }
   }

   public static class ChestSettings {
      public boolean allowChestLocking = true;
      public boolean enableLockedChestOwners = true;
   }

   public static class MimicSettings {
      public MimicDifficulty mimicDifficulty = MimicDifficulty.MEDIUM;
      public boolean spawnNaturalMimics = true;
      public float naturalMimicSpawnRate = 0.5F;
      public boolean allowPetMimics = true;
      public boolean doPetMimicLimit = false;
      public int petMimicLimit = 2;
      public int abandonedMimicTimer = 5;
      public boolean allowPetMimicLocking = true;
   }

   public static class WorldGen {
      public float potSpawnChance = 0.4F;
      public float chestSpawnChance = 0.6F;
      public float secretMimicChance = 0.2F;
   }
}
