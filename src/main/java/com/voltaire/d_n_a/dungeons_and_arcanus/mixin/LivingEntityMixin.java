package com.voltaire.d_n_a.dungeons_and_arcanus.mixin;

import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCItems;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin {
   @Inject(
      method = {"method_16077"},
      at = {@At("TAIL")}
   )
   private void probablychests$dropMimicItems(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
      LivingEntity entity = (LivingEntity)this;
      if (!entity.level().isClientSide()) {
         Level var6 = entity.level();
         if (var6 instanceof ServerLevel) {
            ServerLevel world = (ServerLevel)var6;
            if (source.getEntity() instanceof Player) {
               if (entity.getType().is(PCTags.MIMIC_MOB_EXTENSION)) {
                  if (entity.getRandom().nextFloat() < 0.175F) {
                     entity.spawnAtLocation(new ItemStack(PCItems.MIMIC_CORE));
                  }

                  if (entity.getRandom().nextFloat() < 0.175F) {
                     entity.spawnAtLocation(new ItemStack(PCItems.MIMIC_KEY_FRAGMENT));
                  }

               }
            }
         }
      }
   }
}
