package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.ModBlockEntitys;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.Spinning_Wheel_BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class Spinning_Wheel extends BaseEntityBlock {
    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(3, 8, 1, 13, 9, 16),
            Block.box(4, 0, 2, 6, 7.6, 4),
            Block.box(4, 0, 12, 6, 3.5, 14),
            Block.box(10, 0, 2, 12, 7.6, 4),
            Block.box(10, 0, 12, 12, 3.5, 14),
            Block.box(6, 4.9, 12, 7, 13.9, 14),
            Block.box(9, 4.9, 12, 10, 13.9, 14),
            Block.box(7, 6.9, 9.8, 9, 14.9, 17.8),
            Block.box(7, 7, 4, 8, 15, 5),
            Block.box(6, 11, 3, 9, 12, 6),
            Block.box(6.5, 12, 3.5, 8.5, 14, 5.5)
    );

    // Pre-compute the rotated versions
    private static final VoxelShape SHAPE_EAST  = rotateShape(Direction.NORTH, Direction.EAST,  SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST  = rotateShape(Direction.NORTH, Direction.WEST,  SHAPE_NORTH);

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public Spinning_Wheel(Properties pProperties) {
        super(pProperties);
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof Spinning_Wheel_BlockEntity) {
                ((Spinning_Wheel_BlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST  -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST  -> SHAPE_WEST;
            default    -> SHAPE_NORTH;   // NORTH
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);   // reuse the same logic
    }

    // Helper method to rotate a shape around the Y axis
    private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, Shapes.empty() };

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX));
            });
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }



    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof Spinning_Wheel_BlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (Spinning_Wheel_BlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new Spinning_Wheel_BlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()){
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntitys.SPINNING_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1,pPos, pState1));
    }
}
