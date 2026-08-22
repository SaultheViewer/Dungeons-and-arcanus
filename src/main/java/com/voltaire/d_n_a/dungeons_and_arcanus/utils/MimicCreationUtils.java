package com.voltaire.d_n_a.dungeons_and_arcanus.utils;


import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ChestMimicPet;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.PCChestMimic;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PC_ChestBlock;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PC_BaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.interfaces.PlayerEntityAccess;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCStatistics;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MimicCreationUtils {
    public  MimicCreationUtils() {

    }
    public static boolean createHostileMimic(Level level, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
        PC_BaseChestBlockEntity chest = getChestBlockFromWorld(level, pos);
        if (chest != null) {
            createMimicEntity(false, pos, state, level, chest, player, type);
            return true;
        } else {
            return false;
        }
    }

    public static void tryMakeHostileMimic(Level level, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
        if (canCreateHostileMimic(level, pos, state, player, type)) {
            createHostileMimic(level, pos, state, player, type);
        }
    }

    public static boolean canCreateHostileMimic(Level level, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
        PC_BaseChestBlockEntity chest = getChestBlockFromWorld(level, pos);
        return level.getDifficulty() != Difficulty.PEACEFUL && isSecretMimic(chest, level, pos, type);
    }

    public static void createMimicEntity(boolean isPetMimic, BlockPos pos, BlockState state, Level level, PC_BaseChestBlockEntity chest, Player player, PCChestTypes type) {
        if (!chest.hasMadeMimic) {
            chest.hasMadeMimic = true;
            Tameable_Pet_With_Inv mimic;
            if (isPetMimic) {
                mimic = new ChestMimicPet(type.getPetMimicType(), level);
                mimic.setOwnerUUID(player);
                mimic.setTarget(null);
                mimic.setInSittingPose(false);
                mimic.updateSitting(player);
                ((PlayerEntityAccess) player).addPetMimicToOwnedList(mimic.getUUID());
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, mimic);
                }
            } else {
                mimic = new PCChestMimic(type.getMimicType(), level);
                if (player != null) {
                    player.awardStat(PCStatistics.MIMIC_ENCOUNTERS, 1);
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, mimic);
                    }
                }
            }

            mimic.setType(type);
            mimic.setPos(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D);
            mimic.setYRot(state.getValue(PC_ChestBlock.FACING).toYRot());
            mimic.yRotO = mimic.getYRot();
            mimic.yBodyRot = mimic.getYRot();

            for (int i = 0; i < type.size; ++i) {
                mimic.inventory.setItem(i, chest.getItem(i));
                chest.setItem(i, ItemStack.EMPTY);
            }

            level.addFreshEntity(mimic);
            if (isPetMimic) {
                mimic.setSitting(false);
                mimic.updateSitting(player);
                mimic.level().broadcastEntityEvent(mimic, (byte) 7);
            }

            boolean waterlogged = state.getValue(PC_ChestBlock.WATERLOGGED);
            level.setBlock(pos, waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public static void convertPetMimicToHostile(Level level, PCChestTypes type, Tameable_Pet_With_Inv other) {
        Tameable_Pet_With_Inv mimic = new PCChestMimic(type.getMimicType(), level);
        mimic.setType(type);
        mimic.copyPosition(other);

        for (int i = 0; i < type.size; ++i) {
            mimic.inventory.setItem(i, other.inventory.getItem(i));
            other.inventory.setItem(i, ItemStack.EMPTY);
        }

        if (other.hasCustomName()) {
            mimic.setCustomName(other.getCustomName());
            mimic.setCustomNameVisible(other.isCustomNameVisible());
        }

        other.discard();
        level.addFreshEntity(mimic);
    }

    public static void convertHostileMimicToPet(Level level, Tameable_Pet_With_Inv other, Player player) {
        PCChestTypes type = other.getChestType();
        Tameable_Pet_With_Inv mimic = new ChestMimicPet(type.getPetMimicType(), level);
        mimic.setOwner(player);
        mimic.setTamed(true);
        mimic.setTarget(null);
        mimic.setSitting(false);
        mimic.updateSitting(player);
        mimic.setType(type);
        mimic.copyPosition(other);

        for (int i = 0; i < type.size; ++i) {
            mimic.inventory.setItem(i, other.inventory.getItem(i));
            other.inventory.setItem(i, ItemStack.EMPTY);
        }

        if (other.hasCustomName()) {
            mimic.setCustomName(other.getCustomName());
            mimic.setCustomNameVisible(other.isCustomNameVisible());
        }

        other.discard();
        level.addFreshEntity(mimic);
        ((PlayerEntityAccess) player).addPetMimicToOwnedList(mimic.getUUID());
        mimic.moveTo(mimic.getX(), mimic.getY(), mimic.getZ(), mimic.getYRot(), mimic.getXRot());
        mimic.level().broadcastEntityEvent(mimic, (byte) 7);
    }

    public static boolean isSecretMimic(PC_BaseChestBlockEntity chest, Level level, BlockPos pos, PCChestTypes type) {
        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            if (!chest.hasBeenInteractedWith && chest.isNatural) {
                chest.hasBeenInteractedWith = true;
                RandomizableContainerBlockEntity.setLootTable(level, level.getRandom(), pos, type.getLootTable());
            }
            return false;
        } else {
            // Adjust this to match whatever your config class is named/located
            var config = Dungeons_and_arcanus.loadedConfig;
            if (!chest.hasBeenInteractedWith && chest.isNatural) {
                chest.hasBeenInteractedWith = true;
                float mimicRandom = level.getRandom().nextFloat();
                chest.isMimic = mimicRandom < config.worldGen.secretMimicChance;
                if (!chest.isMimic) {
                    RandomizableContainerBlockEntity.setLootTable(level, level.getRandom(), pos, type.getLootTable());
                }
            }
            return chest.isMimic;
        }
    }

    public static boolean canCreatePetMimic(Level level, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
        PC_BaseChestBlockEntity chest = getChestBlockFromWorld(level, pos);
        return !((PlayerEntityAccess) player).checkForMimicLimit() && !isSecretMimic(chest, level, pos, type);
    }

    public static boolean tryMakePetMimic(Level level, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
        return canCreatePetMimic(level, pos, state, player, type) && createPetMimic(level, pos, state, player, type);
    }

    public static boolean createPetMimic(Level level, BlockPos pos, BlockState state, Player player, PCChestTypes type) {
        PC_BaseChestBlockEntity chest = getChestBlockFromWorld(level, pos);
        if (chest != null) {
            createMimicEntity(true, pos, state, level, chest, player, type);
            return true;
        } else {
            return false;
        }
    }

    public static PC_BaseChestBlockEntity getChestBlockFromWorld(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof PC_BaseChestBlockEntity chest) {
            return chest;
        }
        return null;
    }


}
