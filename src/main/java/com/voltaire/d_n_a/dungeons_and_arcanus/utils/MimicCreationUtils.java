package com.voltaire.d_n_a.dungeons_and_arcanus.utils;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ChestMimicPet;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.PCChestMimic;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PC_ChestBlock;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PCBaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.interfaces.PlayerEntityAccess;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCStatistics;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class MimicCreationUtils {
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

    public static void createMimicEntity(
            boolean isPetMimic, BlockPos pos, BlockState state, Level world, PCBaseChestBlockEntity chest, Player player, PCChestTypes type
    ) {
        if (!chest.hasMadeMimic) {
            chest.hasMadeMimic = true;
            Tameable_Pet_With_Inv mimic;
            if (isPetMimic) {
                mimic = new ChestMimicPet(type.getPetMimicType(), world);
                mimic.tame(player);
                mimic.setTarget((LivingEntity)null);
                mimic.setOrderedToSit(false);
                mimic.updateSitting(player);
                ((PlayerEntityAccess)player).addPetMimicToOwnedList(mimic.getUUID());
                if (player != null && player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, mimic);
                }
            } else {
                mimic = new PCChestMimic(type.getMimicType(), world);
                if (player != null) {
                    player.awardStat(Stats.CUSTOM.get(PCStatistics.MIMIC_ENCOUNTERS.get()));
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, mimic);
                    }
                }
            }

            mimic.setType(type);
            mimic.setPosRaw(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
            mimic.setYRot(((Direction)state.getValue(PC_ChestBlock.FACING)).toYRot());
            mimic.yHeadRot = mimic.getYRot();
            mimic.yBodyRot = mimic.getYRot();

            for (int i = 0; i < type.size; i++) {
                mimic.inventory.setItem(i, chest.getItem(i));
                chest.setItem(i, ItemStack.EMPTY);
            }

            world.addFreshEntity(mimic);
            if (isPetMimic) {
                mimic.setOrderedToSit(false);
                mimic.updateSitting(player);
                mimic.level().broadcastEntityEvent(mimic, (byte)7);
            }

            boolean waterlogged = (Boolean)state.getValue(PC_ChestBlock.WATERLOGGED);
            world.setBlockAndUpdate(pos, waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
        }
    }

    public static void convertPetMimicToHostile(Level world, PCChestTypes type, Tameable_Pet_With_Inv other) {
        Tameable_Pet_With_Inv mimic = new PCChestMimic(type.getMimicType(), world);
        mimic.setType(type);
        mimic.copyPosition(other);

        for (int i = 0; i < type.size; i++) {
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

    public static void convertHostileMimicToPet(Level world, Tameable_Pet_With_Inv other, Player player) {
        PCChestTypes type = other.getChestType();
        Tameable_Pet_With_Inv mimic = new ChestMimicPet(type.getPetMimicType(), world);
        mimic.tame(player);
        mimic.setTame(true);
        mimic.setTarget(null);
        mimic.setOrderedToSit(false);
        mimic.updateSitting(player);
        mimic.setType(type);
        mimic.copyPosition(other);

        for (int i = 0; i < type.size; i++) {
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
            PCConfig config = Dungeons_and_arcanus.loadedConfig;
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
