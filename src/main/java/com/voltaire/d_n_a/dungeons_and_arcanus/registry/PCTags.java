package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.cloudwarp.probablychests.ProbablyChests;

public class PCTags {
   public static final TagKey<EntityType<?>> MIMIC_MOB_EXTENSION;

   static {
      MIMIC_MOB_EXTENSION = TagKey.create(Registries.ENTITY_TYPE, ProbablyChests.id("mimic_mob_extension"));
   }
}
