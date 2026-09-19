package com.voltaire.d_n_a.dungeons_and_arcanus.mixin;

import com.mojang.authlib.GameProfile;
import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.interfaces.PlayerEntityAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements PlayerEntityAccess {
   HashSet<UUID> petMimicList = new HashSet<>();
   HashSet<UUID> mimicKeepList = new HashSet<>();

   public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
      super(world, pos, yaw, gameProfile);
   }

   @Override
   public void addPetMimicToOwnedList(UUID mimic) {
      this.petMimicList.add(mimic);
   }

   @Override
   public boolean checkForMimicLimit() {
      Iterator<UUID> i = this.petMimicList.iterator();

      while (i.hasNext()) {
         UUID mimic = i.next();
         Tameable_Pet_With_Inv entity = (Tameable_Pet_With_Inv)((ServerLevel)this.level()).getEntity(mimic);
         if (entity == null || entity.isRemoved()) {
            i.remove();
         }
      }

      return Dungeons_and_arcanus.loadedConfig.mimicSettings.doPetMimicLimit
              && this.getNumberOfPetMimics() >= Dungeons_and_arcanus.loadedConfig.mimicSettings.petMimicLimit;
   }

   @Override
   public void removePetMimicFromOwnedList(UUID mimic) {
      this.petMimicList.remove(mimic);
   }

   @Override
   public int getNumberOfPetMimics() {
      return this.petMimicList.size();
   }

   @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
   public void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
      ListTag listnbt = new ListTag();

      for (UUID mimic : this.petMimicList) {
         CompoundTag compoundnbt = new CompoundTag();
         compoundnbt.putUUID("uuid", mimic);
         listnbt.add(compoundnbt);
      }

      nbt.put("pet_mimics", listnbt);
   }

   @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
   public void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
      ListTag listnbt = nbt.getList("pet_mimics", 10);

      for (int i = 0; i < listnbt.size(); i++) {
         CompoundTag compoundnbt = listnbt.getCompound(i);
         this.addPetMimicToOwnedList(compoundnbt.getUUID("uuid"));
      }
   }

   @Override
   public void addMimicToKeepList(UUID mimic) {
      this.mimicKeepList.add(mimic);
   }

   @Override
   public void removeMimicFromKeepList(UUID mimic) {
      this.mimicKeepList.remove(mimic);
   }

   @Override
   public boolean isMimicInKeepList(UUID mimic) {
      return this.mimicKeepList.contains(mimic);
   }

   @Override
   public int abandonMimics() {
      int removed = 0;
      Iterator<UUID> i = this.petMimicList.iterator();

      while (i.hasNext()) {
         UUID mimic = i.next();
         if (!this.isMimicInKeepList(mimic)) {
            Tameable_Pet_With_Inv entity = (Tameable_Pet_With_Inv)((ServerLevel)this.level()).getEntity(mimic);
            if (entity != null && !entity.isRemoved()) {
               entity.setIsAbandoned(true);
            }

            i.remove();
            removed++;
         }
      }

      return removed;
   }
}
