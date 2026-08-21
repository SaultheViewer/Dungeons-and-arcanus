package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCSounds {
   public static final ResourceLocation BELL_HIT_1_ID = ProbablyChests.id("bell_hit1");
   public static final ResourceLocation BELL_HIT_2_ID = ProbablyChests.id("bell_hit2");
   public static final ResourceLocation BELL_HIT_4_ID = ProbablyChests.id("bell_hit4");
   public static final ResourceLocation CLOSE_2_ID = ProbablyChests.id("close2");
   public static final ResourceLocation MIMIC_BITE_ID = ProbablyChests.id("mimic_bite");
   public static final ResourceLocation APPLY_LOCK_1_ID = ProbablyChests.id("apply_lock1");
   public static final ResourceLocation APPLY_LOCK_2_ID = ProbablyChests.id("apply_lock2");
   public static final ResourceLocation LOCK_UNLOCK_ID = ProbablyChests.id("lock_unlock");
   public static SoundEvent BELL_HIT_1;
   public static SoundEvent BELL_HIT_2;
   public static SoundEvent BELL_HIT_4;
   public static SoundEvent CLOSE_2;
   public static SoundEvent MIMIC_BITE;
   public static SoundEvent APPLY_LOCK1;
   public static SoundEvent APPLY_LOCK2;
   public static SoundEvent LOCK_UNLOCK;

   public static void init() {
      Registry.register(BuiltInRegistries.SOUND_EVENT, BELL_HIT_1_ID, BELL_HIT_1);
      Registry.register(BuiltInRegistries.SOUND_EVENT, BELL_HIT_2_ID, BELL_HIT_2);
      Registry.register(BuiltInRegistries.SOUND_EVENT, BELL_HIT_4_ID, BELL_HIT_4);
      Registry.register(BuiltInRegistries.SOUND_EVENT, CLOSE_2_ID, CLOSE_2);
      Registry.register(BuiltInRegistries.SOUND_EVENT, MIMIC_BITE_ID, MIMIC_BITE);
      Registry.register(BuiltInRegistries.SOUND_EVENT, APPLY_LOCK_1_ID, APPLY_LOCK1);
      Registry.register(BuiltInRegistries.SOUND_EVENT, APPLY_LOCK_2_ID, APPLY_LOCK2);
      Registry.register(BuiltInRegistries.SOUND_EVENT, LOCK_UNLOCK_ID, LOCK_UNLOCK);
   }

   static {
      BELL_HIT_1 = SoundEvent.createVariableRangeEvent(BELL_HIT_1_ID);
      BELL_HIT_2 = SoundEvent.createVariableRangeEvent(BELL_HIT_2_ID);
      BELL_HIT_4 = SoundEvent.createVariableRangeEvent(BELL_HIT_4_ID);
      CLOSE_2 = SoundEvent.createVariableRangeEvent(CLOSE_2_ID);
      MIMIC_BITE = SoundEvent.createVariableRangeEvent(MIMIC_BITE_ID);
      APPLY_LOCK1 = SoundEvent.createVariableRangeEvent(APPLY_LOCK_1_ID);
      APPLY_LOCK2 = SoundEvent.createVariableRangeEvent(APPLY_LOCK_2_ID);
      LOCK_UNLOCK = SoundEvent.createVariableRangeEvent(LOCK_UNLOCK_ID);
   }
}
