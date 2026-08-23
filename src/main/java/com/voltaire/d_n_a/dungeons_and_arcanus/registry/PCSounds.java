package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class PCSounds {
   public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Dungeons_and_arcanus.MOD_ID);

   public static final RegistryObject<SoundEvent> BELL_HIT_1 = registerSound("bell_hit1");
   public static final RegistryObject<SoundEvent> BELL_HIT_2 = registerSound("bell_hit2");
   public static final RegistryObject<SoundEvent> BELL_HIT_4 = registerSound("bell_hit4");
   public static final RegistryObject<SoundEvent> CLOSE_2 = registerSound("close2");
   public static final RegistryObject<SoundEvent> MIMIC_BITE = registerSound("mimic_bite");
   public static final RegistryObject<SoundEvent> APPLY_LOCK1 = registerSound("apply_lock1");
   public static final RegistryObject<SoundEvent> APPLY_LOCK2 = registerSound("apply_lock2");
   public static final RegistryObject<SoundEvent> LOCK_UNLOCK = registerSound("lock_unlock");

   private static RegistryObject<SoundEvent> registerSound(String name) {
      return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Dungeons_and_arcanus.MOD_ID, name)));
   }
}
