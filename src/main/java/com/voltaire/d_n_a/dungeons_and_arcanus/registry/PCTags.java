package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class PCTags {
   public static final TagKey<EntityType<?>> MIMIC_MOB_EXTENSION;

   static {
      MIMIC_MOB_EXTENSION = TagKey.create(Registries.ENTITY_TYPE, Dungeons_and_arcanus.id("mimic_mob_extension"));
   }
}
