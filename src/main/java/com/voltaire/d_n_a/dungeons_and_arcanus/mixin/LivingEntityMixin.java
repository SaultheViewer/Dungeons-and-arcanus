package com.voltaire.d_n_a.dungeons_and_arcanus.mixin;


import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
   @Inject(method = "dropCustomDeathLoot", at = @At("TAIL"))
   private void probablychests$dropMimicItems(DamageSource source, int looting, boolean causedByPlayer, CallbackInfo ci) {
      LivingEntity entity = (LivingEntity)(Object)this;
      if (!entity.level().isClientSide()) {
         if (entity.level() instanceof ServerLevel world) {
            if (source.getEntity() instanceof Player) {
               if (entity.getType().is(PCTags.MIMIC_MOB_EXTENSION)) {
                  if (entity.getRandom().nextFloat() < 0.175F) {
                     entity.spawnAtLocation(new ItemStack(ModItems.MIMIC_CORE.get()));
                  }

                  if (entity.getRandom().nextFloat() < 0.175F) {
                     entity.spawnAtLocation(new ItemStack(ModItems.MIMIC_KEY_FRAGMENT.get()));
                  }
               }
            }
         }
      }
   }
}
