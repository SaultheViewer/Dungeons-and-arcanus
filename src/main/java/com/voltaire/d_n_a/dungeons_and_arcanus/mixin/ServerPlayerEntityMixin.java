package com.voltaire.d_n_a.dungeons_and_arcanus.mixin;

import com.mojang.authlib.GameProfile;
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

@Mixin({ServerPlayer.class})
public abstract class ServerPlayerEntityMixin extends Player implements PlayerEntityAccess {
   HashSet<UUID> petMimicList = new HashSet();
   HashSet<UUID> mimicKeepList = new HashSet();

   public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
      super(world, pos, yaw, gameProfile);
   }

   public void addPetMimicToOwnedList(UUID mimic) {
      this.petMimicList.add(mimic);
   }

   public boolean checkForMimicLimit() {
      Iterator<UUID> i = this.petMimicList.iterator();

      while(i.hasNext()) {
         UUID mimic = (UUID)i.next();
         PCTameablePetWithInventory entity = (PCTameablePetWithInventory)((ServerLevel)this.level()).getEntity(mimic);
         if (entity == null || entity.isRemoved()) {
            i.remove();
         }
      }

      if (ProbablyChests.loadedConfig.mimicSettings.doPetMimicLimit && this.getNumberOfPetMimics() >= ProbablyChests.loadedConfig.mimicSettings.petMimicLimit) {
         return true;
      } else {
         return false;
      }
   }

   public void removePetMimicFromOwnedList(UUID mimic) {
      this.petMimicList.remove(mimic);
   }

   public int getNumberOfPetMimics() {
      return this.petMimicList.size();
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"method_5652"}
   )
   public void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
      ListTag listnbt = new ListTag();

      for(UUID mimic : this.petMimicList) {
         CompoundTag compoundnbt = new CompoundTag();
         compoundnbt.putUUID("uuid", mimic);
         listnbt.add(compoundnbt);
      }

      nbt.put("pet_mimics", listnbt);
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"method_5749"}
   )
   public void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
      ListTag listnbt = nbt.getList("pet_mimics", 10);

      for(int i = 0; i < listnbt.size(); ++i) {
         CompoundTag compoundnbt = listnbt.getCompound(i);
         this.addPetMimicToOwnedList(compoundnbt.getUUID("uuid"));
      }

   }

   public void addMimicToKeepList(UUID mimic) {
      this.mimicKeepList.add(mimic);
   }

   public void removeMimicFromKeepList(UUID mimic) {
      this.mimicKeepList.remove(mimic);
   }

   public boolean isMimicInKeepList(UUID mimic) {
      return this.mimicKeepList.contains(mimic);
   }

   public int abandonMimics() {
      int removed = 0;
      Iterator<UUID> i = this.petMimicList.iterator();

      while(i.hasNext()) {
         UUID mimic = (UUID)i.next();
         if (!this.isMimicInKeepList(mimic)) {
            PCTameablePetWithInventory entity = (PCTameablePetWithInventory)((ServerLevel)this.level()).getEntity(mimic);
            if (entity != null && !entity.isRemoved()) {
               entity.setIsAbandoned(true);
            }

            i.remove();
            ++removed;
         }
      }

      return removed;
   }
}
