package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PC_BaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCProperties;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCSounds;
import com.voltaire.d_n_a.dungeons_and_arcanus.util.MimicCreationUtils;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCChestState;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCConfig;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCLockedState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Objects;

public class PC_ChestBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<PCChestState> CHEST_STATE = PCProperties.PC_CHEST_STATE;
    public static final EnumProperty<PCLockedState> LOCKED_STATE = PCProperties.PC_LOCKED_STATE;

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.5, 15.0, 13.0, 14.5);

    private final PCChestTypes type;

    public PC_ChestBlock(Properties properties, PCChestTypes type) {
        super(properties);
        Objects.requireNonNull(type);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(CHEST_STATE, PCChestState.CLOSED)
                .setValue(LOCKED_STATE, PCLockedState.UNLOCKED));
        this.type = type;
    }

    // ---------- Utility ----------

    public static boolean isChestBlocked(LevelAccessor level, BlockPos pos) {
        return hasBlockOnTop(level, pos);
    }

    public static boolean isDry(BlockState state) {
        return !state.getValue(WATERLOGGED);
    }

    private static boolean hasBlockOnTop(BlockGetter level, BlockPos pos) {
        BlockPos above = pos.above();
        return level.getBlockState(above).isSolidRender(level, above);
    }

    // ---------- Breaking / Collision / Hardness ----------

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
            if (chest != null && !chest.isLocked) {
                MimicCreationUtils.tryMakeHostileMimic(level, pos, state, player, this.type);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
            if (chest != null && !chest.isLocked) {
                MimicCreationUtils.tryMakeHostileMimic(level, pos, state, player, this.type);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext) {
            Entity entity = entityContext.getEntity();
            if (entity != null && !(entity instanceof Player)) {
                return Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);
            }
        }
        return Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (state.getValue(LOCKED_STATE) == PCLockedState.LOCKED) {
            return 0.0F; // completely unbreakable when locked
        }
        float hardness = 2.0F;
        int i = player.hasCorrectToolForDrops(state) ? 30 : 100;
        return player.getDigSpeed(state, pos) / hardness / (float) i;
    }

    // ---------- Lock / Unlock system ----------

    public boolean unlockBlock(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
        if (chest == null) return false;

        if (Dungeons_and_arcanus.loadedConfig.chestSettings.enableLockedChestOwners
                && chest.owner != null
                && !player.getUUID().equals(chest.owner)) {
            PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.APPLY_LOCK2, 1.0F);
            return false;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (chest.hasGoldLock && stack.is(ModItems.GOLD_KEY.get())) {
            chest.isLocked = false;
            PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.LOCK_UNLOCK, 1.3F);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.GOLD_LOCK.get()));
                }
                chest.hasGoldLock = false;
            }
            return true;
        }
        if (chest.hasVoidLock && stack.is(ModItems.VOID_KEY.get())) {
            chest.isLocked = false;
            PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.LOCK_UNLOCK, 1.3F);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.VOID_LOCK.get()));
                }
                chest.hasVoidLock = false;
            }
            return true;
        }
        if (chest.hasIronLock && stack.is(ModItems.IRON_KEY.get())) {
            chest.isLocked = false;
            PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.LOCK_UNLOCK, 1.3F);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.IRON_LOCK.get()));
                }
                chest.hasIronLock = false;
            }
            return true;
        }

        PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.APPLY_LOCK2, 1.0F);
        return false;
    }

    public boolean lockBlock(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
        if (chest == null) return false;

        if (Dungeons_and_arcanus.loadedConfig.chestSettings.enableLockedChestOwners
                && chest.owner != null
                && !player.getUUID().equals(chest.owner)) {
            return false;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (chest.hasGoldLock && stack.is(ModItems.GOLD_KEY.get())) {
            chest.isLocked = true;
            PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.LOCK_UNLOCK, 0.6F);
            return true;
        }
        if (chest.hasVoidLock && stack.is(ModItems.VOID_KEY.get())) {
            chest.isLocked = true;
            PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.LOCK_UNLOCK, 0.6F);
            return true;
        }
        if (chest.hasIronLock && stack.is(ModItems.IRON_KEY.get())) {
            chest.isLocked = true;
            PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.LOCK_UNLOCK, 0.6F);
            return true;
        }
        return false;
    }

    public boolean addLockToBlock(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!Dungeons_and_arcanus.loadedConfig.chestSettings.allowChestLocking) {
            return false;
        }

        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
        if (chest == null) return false;

        ItemStack stack = player.getItemInHand(hand);

        if (!chest.hasGoldLock && !chest.hasVoidLock && !chest.hasIronLock) {
            if (stack.is(ModItems.GOLD_LOCK.get()) && chest.type() == PCChestTypes.GOLD) {
                chest.isLocked = true;
                chest.hasGoldLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) stack.shrink(1);
                PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.APPLY_LOCK1, 0.6F);
                return true;
            }
            if (stack.is(ModItems.VOID_LOCK.get()) && chest.type() == PCChestTypes.SHADOW) {
                chest.isLocked = true;
                chest.hasVoidLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) stack.shrink(1);
                PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.APPLY_LOCK1, 0.6F);
                return true;
            }
            if (stack.is(ModItems.IRON_LOCK.get())) {
                chest.isLocked = true;
                chest.hasIronLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) stack.shrink(1);
                PC_BaseChestBlockEntity.playSound(level, pos, state, PCSounds.APPLY_LOCK1, 0.6F);
                return true;
            }
        }
        return false;
    }

    public void lockBlockState(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LOCKED_STATE, PCLockedState.LOCKED), 3);
    }

    public void unlockBlockState(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LOCKED_STATE, PCLockedState.UNLOCKED), 3);
    }

    // ---------- Main interaction ----------

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
        if (chest == null) return InteractionResult.PASS;

        PCConfig config = Dungeons_and_arcanus.loadedConfig;
        ItemStack stack = player.getItemInHand(hand);

        // 1. Locked → try unlock
        if (chest.isLocked) {
            if (unlockBlock(state, level, pos, player, hand, hit)) {
                unlockBlockState(state, level, pos);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.FAIL;
        }

        // 2. Try add lock
        if (addLockToBlock(state, level, pos, player, hand, hit)) {
            lockBlockState(state, level, pos);
            return InteractionResult.CONSUME;
        }

        // 3. Try re-lock with key
        if (lockBlock(state, level, pos, player, hand, hit)) {
            lockBlockState(state, level, pos);
            return InteractionResult.CONSUME;
        }

        // 4. Pet mimic key
        if (stack.is(ModItems.PET_MIMIC_KEY.get())
                && config.mimicSettings.allowPetMimics
                && !player.isShiftKeyDown()
                && MimicCreationUtils.tryMakePetMimic(level, pos, state, player, this.type)) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.CONSUME;
        }

        // 5. Normal mimic key
        if (stack.is(ModItems.MIMIC_KEY.get())
                && !player.isShiftKeyDown()
                && !MimicCreationUtils.isSecretMimic(chest, level, pos, this.type)
                && level.getDifficulty() != Difficulty.PEACEFUL) {
            chest.isMimic = true;
            chest.isNatural = false;
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.CONSUME;
        }

        // 6. Secret mimic → wake up
        if (MimicCreationUtils.isSecretMimic(chest, level, pos, this.type)) {
            MimicCreationUtils.tryMakeHostileMimic(level, pos, state, player, this.type);
            return InteractionResult.SUCCESS;
        }

        // 7. Open GUI
        if (player instanceof ServerPlayer serverPlayer && chest.canOpen(player)) {
            player.openMenu(chest);
            player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
        }

        return InteractionResult.CONSUME;
    }

    // ---------- Container / drops / placement ----------

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
            BlockEntity be = level.getBlockEntity(pos);

            if (chest != null) {
                if (!MimicCreationUtils.isSecretMimic(chest, level, pos, this.type)) {
                    if (be instanceof PC_BaseChestBlockEntity inventory) {
                        Containers.dropContents(level, pos, inventory);
                        level.updateNeighbourForOutputSignal(pos, this);
                    }
                } else {
                    MimicCreationUtils.tryMakeHostileMimic(level, pos, state, null, this.type);
                }

                // Drop the lock item if present
                if (chest.hasVoidLock) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.VOID_LOCK.get()));
                } else if (chest.hasGoldLock) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.GOLD_LOCK.get()));
                } else if (chest.hasIronLock) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.IRON_LOCK.get()));
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PC_BaseChestBlockEntity chest) {
                chest.setCustomName(stack.getHoverName());
            }
        }
    }

    // ---------- Ticking / Animation ----------

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PC_BaseChestBlockEntity chest) {
            PCChestState current = chest.getChestState();
            if (current == PCChestState.OPEN) {
                chest.setChestState(PCChestState.OPENED);
            } else if (current == PCChestState.CLOSE) {
                chest.setChestState(PCChestState.CLOSED);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, this.getExpectedEntityType(), PC_BaseChestBlockEntity::tick);
    }

    // ---------- Shape / Render / Placement / Waterlogging ----------

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CHEST_STATE, LOCKED_STATE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false; // or true if you want mobs to path through
    }

    // ---------- Block Entity ----------

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.type.makeEntity(pos, state);
    }

    public BlockEntityType<? extends PC_BaseChestBlockEntity> getExpectedEntityType() {
        return this.type.getBlockEntityType();
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }
}
