package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PC_BaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCProperties;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCSounds;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.Objects;

public class PC_ChestBlock extends AbstractChestBlock<PC_BaseChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;
    public static final EnumProperty<PCChestState> CHEST_STATE;
    public static final EnumProperty<PCLockedState> LOCKED_STATE;
    protected static final VoxelShape SHAPE;
    protected static final Map<Direction, VoxelShape> SHAPES;
    private final PCChestTypes type;

    public PC_ChestBlock(BlockBehaviour.Properties settings, PCChestTypes type) {
        super(settings, Objects.requireNonNull(type)::getBlockEntityType);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(WATERLOGGED, false)
                        .setValue(CHEST_STATE, PCChestState.CLOSED)
                        .setValue(LOCKED_STATE, PCLockedState.UNLOCKED)
        );
        this.type = type;
    }

    public static boolean isChestBlocked(LevelAccessor world, BlockPos pos) {
        return hasBlockOnTop(world, pos);
    }

    public static boolean isDry(BlockState state) {
        return !(Boolean)state.getValue(WATERLOGGED);
    }

    private static boolean hasBlockOnTop(BlockGetter world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return world.getBlockState(blockPos).isRedstoneConductor(world, blockPos);
    }

    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            if (!MimicCreationUtils.getChestBlockFromWorld(level, pos).isLocked) {
                MimicCreationUtils.tryMakeHostileMimic(level, pos, state, player, this.type);
            }

        }
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return true;
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide) {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                if (!MimicCreationUtils.getChestBlockFromWorld(level, pos).isLocked) {
                    MimicCreationUtils.tryMakeHostileMimic(level, pos, state, player, this.type);
                }
            }

        }
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext) {
            if (entityContext.getEntity() != null && !(entityContext.getEntity() instanceof Player)) {
                return Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)7.0F, (double)15.0F);
            }
        }

        return Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)15.0F, (double)15.0F);
    }

    public float defaultDestroyTime() {
        return this.stateDefinition.getProperty(PCProperties.PC_LOCKED_STATE.getName()).equals(PCLockedState.LOCKED) ? -1.0F : 2.0F;
    }

    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float f = ((PCLockedState)state.getValue(PCProperties.PC_LOCKED_STATE)).equals(PCLockedState.LOCKED) ? -1.0F : 2.0F;
        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = player.hasCorrectToolForDrops(state) ? 30 : 100;
            return player.getDestroySpeed(state) / f / (float)i;
        }
    }

    public boolean unlockBlock(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(world, pos);
        if (Dungeons_and_arcanus.loadedConfig.chestSettings.enableLockedChestOwners && chest.owner != null && !player.getUUID().equals(chest.owner)) {
            PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK2, 1.0F);
            return false;
        }

        NonNullList<ItemStack> locks = NonNullList.create();
        ItemStack itemStack = player.getItemInHand(hand);
        if (chest.hasGoldLock && itemStack.is(ModItems.GOLD_KEY.get())) {
            chest.isLocked = false;
            PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 1.3F);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    locks.add(new ItemStack(ModItems.GOLD_LOCK.get()));
                    Containers.dropContents(world, pos, locks);
                }

                chest.hasGoldLock = false;
            }

            return true;
        } else if (chest.hasVoidLock && itemStack.is(ModItems.VOID_KEY.get())) {
            chest.isLocked = false;
            PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 1.3F);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    locks.add(new ItemStack(ModItems.VOID_LOCK.get()));
                    Containers.dropContents(world, pos, locks);
                }

                chest.hasVoidLock = false;
            }

            return true;
        } else if (chest.hasIronLock && itemStack.is(ModItems.IRON_KEY.get())) {
            chest.isLocked = false;
            PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 1.3F);
            if (chest.isNatural && !chest.hasBeenInteractedWith) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    locks.add(new ItemStack(ModItems.IRON_LOCK.get()));
                    Containers.dropContents(world, pos, locks);
                }

                chest.hasIronLock = false;
            }

            return true;
        } else {
            PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK2, 1.0F);
            return false;
        }
    }

    public boolean lockBlock(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(world, pos);
        if (Dungeons_and_arcanus.loadedConfig.chestSettings.enableLockedChestOwners && chest.owner != null && !player.getUUID().equals(chest.owner)) {
            return false;
        } else {
            ItemStack itemStack = player.getItemInHand(hand);
            if (chest.hasGoldLock && itemStack.is(ModItems.GOLD_KEY.get())) {
                chest.isLocked = true;
                PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 0.6F);
                return true;
            } else if (chest.hasVoidLock && itemStack.is(ModItems.VOID_KEY.get())) {
                chest.isLocked = true;
                PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 0.6F);
                return true;
            } else if (chest.hasIronLock && itemStack.is(ModItems.IRON_KEY.get())) {
                chest.isLocked = true;
                PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.LOCK_UNLOCK, 0.6F);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean addLockToBlock(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!Dungeons_and_arcanus.loadedConfig.chestSettings.allowChestLocking) {
            return false;
        }

        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(world, pos);
        ItemStack itemStack = player.getItemInHand(hand);
        if (!chest.hasGoldLock && !chest.hasVoidLock && !chest.hasIronLock) {
            if (itemStack.is(ModItems.GOLD_LOCK.get()) && chest.type().equals(PCChestTypes.GOLD)) {
                chest.isLocked = true;
                chest.hasGoldLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK1, 0.6F);
                return true;
            }

            if (itemStack.is(ModItems.VOID_LOCK.get()) && chest.type().equals(PCChestTypes.SHADOW)) {
                chest.isLocked = true;
                chest.hasVoidLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK1, 0.6F);
                return true;
            }

            if (itemStack.is(ModItems.IRON_LOCK.get())) {
                chest.isLocked = true;
                chest.hasIronLock = true;
                chest.owner = player.getUUID();
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                PC_BaseChestBlockEntity.playSound(world, pos, state, PCSounds.APPLY_LOCK1, 0.6F);
                return true;
            }
        }

        return false;
    }

    public void lockBlockState(BlockState state, Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, (BlockState)state.setValue(PCProperties.PC_LOCKED_STATE, PCLockedState.LOCKED));
    }

    public void unlockBlockState(BlockState state, Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, (BlockState)state.setValue(PCProperties.PC_LOCKED_STATE, PCLockedState.UNLOCKED));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
        PCConfig config = Dungeons_and_arcanus.loadedConfig;
        ItemStack itemStack = player.getItemInHand(hand);
        if (chest.isLocked) {
            if (this.unlockBlock(state, level, pos, player, hand, hit)) {
                this.unlockBlockState(state, level, pos);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            if (this.addLockToBlock(state, level, pos, player, hand, hit)) {
                this.lockBlockState(state, level, pos);
                return InteractionResult.CONSUME;
            }

            if (this.lockBlock(state, level, pos, player, hand, hit)) {
                this.lockBlockState(state, level, pos);
                return InteractionResult.CONSUME;
            }

            if (chest != null) {
                if (itemStack.is(ModItems.PET_MIMIC_KEY.get())
                        && config.mimicSettings.allowPetMimics
                        && !player.isShiftKeyDown()
                        && MimicCreationUtils.tryMakePetMimic(level, pos, state, player, this.type)) {
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                    return InteractionResult.CONSUME;
                }

                if (itemStack.is(ModItems.MIMIC_KEY.get())
                        && !player.isShiftKeyDown()
                        && !MimicCreationUtils.isSecretMimic(chest, level, pos, this.type)
                        && level.getDifficulty() != Difficulty.PEACEFUL) {
                    chest.isMimic = true;
                    chest.isNatural = false;
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                    return InteractionResult.CONSUME;
                }

                if (MimicCreationUtils.isSecretMimic(chest, level, pos, this.type)) {
                    MimicCreationUtils.tryMakeHostileMimic(level, pos, state, player, this.type);
                    return InteractionResult.SUCCESS;
                }
            }

            MenuProvider namedScreenHandlerFactory = this.getMenuProvider(state, level, pos);
            if (namedScreenHandlerFactory != null && player instanceof ServerPlayer && chest.canOpen(player)) {
                player.openMenu(namedScreenHandlerFactory);
                player.awardStat(this.getOpenStat());
            }

            return InteractionResult.CONSUME;
        }
    }

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
            if (chest == null) {
                super.onRemove(state, level, pos, newState, movedByPiston);
            } else {
                if (!MimicCreationUtils.isSecretMimic(chest, level, pos, this.type)) {
                    if (blockEntity instanceof Container inventory) {
                        Containers.dropContents(level, pos, inventory);
                        level.updateNeighbourForOutputSignal(pos, this);
                    }
                } else {
                    MimicCreationUtils.tryMakeHostileMimic(level, pos, state, null, this.type);
                }

                NonNullList<ItemStack> locks = NonNullList.create();
                if (chest.hasVoidLock) {
                    locks.add(new ItemStack(ModItems.VOID_LOCK.get()));
                } else if (chest.hasGoldLock) {
                    locks.add(new ItemStack(ModItems.GOLD_LOCK.get()));
                } else if (chest.hasIronLock) {
                    locks.add(new ItemStack(ModItems.IRON_LOCK.get()));
                }

                if (!locks.isEmpty()) {
                    Containers.dropContents(level, pos, locks);
                }

                super.onRemove(state, level, pos, newState, movedByPiston);
            }
        }
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        PC_BaseChestBlockEntity chest = MimicCreationUtils.getChestBlockFromWorld(level, pos);
        if (chest != null && stack.hasCustomHoverName()) {
            chest.setCustomName(stack.getHoverName());
        }

    }

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

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape)SHAPES.get(state.getValue(FACING));
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, this.getExpectedEntityType(), (world1, pos, state1, blockEntity) -> blockEntity.tick());
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.type.makeEntity(pos, state);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, WATERLOGGED, CHEST_STATE, LOCKED_STATE});
        super.createBlockStateDefinition(builder);
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if ((Boolean)state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public BlockEntityType<? extends PC_BaseChestBlockEntity> getExpectedEntityType() {
        return (BlockEntityType)this.blockEntityType.get();
    }

    public static Direction getFacing(BlockState state) {
        return (Direction)state.getValue(FACING);
    }

    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean override) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        CHEST_STATE = PCProperties.PC_CHEST_STATE;
        LOCKED_STATE = PCProperties.PC_LOCKED_STATE;
        SHAPE = Block.box((double)1.0F, (double)0.0F, (double)1.5F, (double)15.0F, (double)13.0F, (double)14.5F);
        SHAPES = VoxelShaper.generateRotations(SHAPE);
    }
}
