package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.block.PCChestBlock;
import org.cloudwarp.probablychests.block.PCChestTypes;
import org.cloudwarp.probablychests.block.entity.PCBaseChestBlockEntity;
import org.cloudwarp.probablychests.entity.PCChestMimic;
import org.cloudwarp.probablychests.entity.PCChestMimicPet;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.interfaces.PlayerEntityAccess;
import org.cloudwarp.probablychests.registry.PCStatistics;

public class PCMimicCreationUtils {
   public static boolean createHostileMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
      PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
      if (chest != null) {
         createMimicEntity(false, pos, state, world, chest, player, type);
         return true;
      } else {
         return false;
      }
   }

   public static void tryMakeHostileMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
      if (canCreateHostileMimic(world, pos, state, player, type)) {
         createHostileMimic(world, pos, state, player, type);
      }

   }

   public static boolean canCreateHostileMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
      PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
      return world.getDifficulty() != Difficulty.PEACEFUL && isSecretMimic(chest, world, pos, type);
   }

   public static void createMimicEntity(boolean isPetMimic, BlockPos pos, BlockState state, Level world, PCBaseChestBlockEntity chest, Player player, PCChestTypes type) {
      if (!chest.hasMadeMimic) {
         chest.hasMadeMimic = true;
         PCTameablePetWithInventory mimic;
         if (isPetMimic) {
            mimic = new PCChestMimicPet(type.getPetMimicType(), world);
            mimic.tame(player);
            mimic.setTarget((LivingEntity)null);
            mimic.setOrderedToSit(false);
            mimic.updateSitting(player);
            ((PlayerEntityAccess)player).addPetMimicToOwnedList(mimic.getUUID());
            if (player != null && player instanceof ServerPlayer) {
               ServerPlayer serverPlayer = (ServerPlayer)player;
               CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, mimic);
            }
         } else {
            mimic = new PCChestMimic(type.getMimicType(), world);
            if (player != null) {
               player.awardStat(PCStatistics.MIMIC_ENCOUNTERS, 1);
               if (player instanceof ServerPlayer) {
                  ServerPlayer serverPlayer = (ServerPlayer)player;
                  CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, mimic);
               }
            }
         }

         mimic.setType(type);
         mimic.setPosRaw((double)pos.getX() + (double)0.5F, (double)pos.getY() + 0.1, (double)pos.getZ() + (double)0.5F);
         mimic.setYRot(((Direction)state.getValue(PCChestBlock.FACING)).toYRot());
         mimic.yHeadRot = mimic.getYRot();
         mimic.yBodyRot = mimic.getYRot();

         for(int i = 0; i < type.size; ++i) {
            mimic.inventory.setItem(i, chest.getItem(i));
            chest.setItem(i, ItemStack.EMPTY);
         }

         world.addFreshEntity(mimic);
         if (isPetMimic) {
            mimic.setOrderedToSit(false);
            mimic.updateSitting(player);
            mimic.level().broadcastEntityEvent(mimic, (byte)7);
         }

         boolean waterlogged = (Boolean)state.getValue(PCChestBlock.WATERLOGGED);
         world.setBlockAndUpdate(pos, waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
      }
   }

   public static void convertPetMimicToHostile(Level world, PCChestTypes type, PCTameablePetWithInventory other) {
      PCTameablePetWithInventory mimic = new PCChestMimic(type.getMimicType(), world);
      mimic.setType(type);
      mimic.copyPosition(other);

      for(int i = 0; i < type.size; ++i) {
         mimic.inventory.setItem(i, other.inventory.getItem(i));
         other.inventory.setItem(i, ItemStack.EMPTY);
      }

      if (other.hasCustomName()) {
         mimic.setCustomName(other.getCustomName());
         mimic.setCustomNameVisible(other.isCustomNameVisible());
      }

      other.discard();
      world.addFreshEntity(mimic);
   }

   public static void convertHostileMimicToPet(Level world, PCTameablePetWithInventory other, Player player) {
      PCChestTypes type = other.getChestType();
      PCTameablePetWithInventory mimic = new PCChestMimicPet(type.getPetMimicType(), world);
      mimic.tame(player);
      mimic.setTame(true);
      mimic.setTarget((LivingEntity)null);
      mimic.setOrderedToSit(false);
      mimic.updateSitting(player);
      mimic.setType(type);
      mimic.copyPosition(other);

      for(int i = 0; i < type.size; ++i) {
         mimic.inventory.setItem(i, other.inventory.getItem(i));
         other.inventory.setItem(i, ItemStack.EMPTY);
      }

      if (other.hasCustomName()) {
         mimic.setCustomName(other.getCustomName());
         mimic.setCustomNameVisible(other.isCustomNameVisible());
      }

      other.discard();
      world.addFreshEntity(mimic);
      ((PlayerEntityAccess)player).addPetMimicToOwnedList(mimic.getUUID());
      mimic.moveTo(mimic.getX(), mimic.getY(), mimic.getZ(), mimic.getYRot(), mimic.getXRot());
      mimic.level().broadcastEntityEvent(mimic, (byte)7);
   }

   public static boolean isSecretMimic(PCBaseChestBlockEntity chest, Level world, BlockPos pos, PCChestTypes type) {
      if (world.getDifficulty() == Difficulty.PEACEFUL) {
         if (!chest.hasBeenInteractedWith && chest.isNatural) {
            chest.hasBeenInteractedWith = true;
            RandomizableContainerBlockEntity.setLootTable(world, world.getRandom(), pos, type.getLootTable());
         }

         return false;
      } else {
         PCConfig config = ProbablyChests.loadedConfig;
         if (!chest.hasBeenInteractedWith && chest.isNatural) {
            chest.hasBeenInteractedWith = true;
            float mimicRandom = world.getRandom().nextFloat();
            chest.isMimic = mimicRandom < config.worldGen.secretMimicChance;
            if (!chest.isMimic) {
               RandomizableContainerBlockEntity.setLootTable(world, world.getRandom(), pos, type.getLootTable());
            }
         }

         return chest.isMimic;
      }
   }

   public static boolean canCreatePetMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
      PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
      return !((PlayerEntityAccess)player).checkForMimicLimit() && !isSecretMimic(chest, world, pos, type);
   }

   public static boolean tryMakePetMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
      return canCreatePetMimic(world, pos, state, player, type) ? createPetMimic(world, pos, state, player, type) : false;
   }

   public static boolean createPetMimic(Level world, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
      PCBaseChestBlockEntity chest = getChestBlockFromWorld(world, pos);
      if (chest != null) {
         createMimicEntity(true, pos, state, world, chest, player, type);
         return true;
      } else {
         return false;
      }
   }

   public static PCBaseChestBlockEntity getChestBlockFromWorld(Level world, BlockPos pos) {
      PCBaseChestBlockEntity chest = null;
      if (world.getBlockEntity(pos) instanceof PCBaseChestBlockEntity) {
         chest = (PCBaseChestBlockEntity)world.getBlockEntity(pos);
      }

      return chest;
   }
}
