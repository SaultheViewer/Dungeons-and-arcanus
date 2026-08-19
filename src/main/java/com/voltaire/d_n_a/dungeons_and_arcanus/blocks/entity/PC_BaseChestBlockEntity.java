package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity;


import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class PC_BaseChestBlockEntity extends RandomizableContainerBlockEntity implements GeoAnimatable {

    public static final EnumProperty<PCChestState> CHEST_STATE = PCProperties.PC_CHEST_STATE;

    private static final String CONTROLLER_NAME = "chestController";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private PCChestState lastState = null;
    private String currentAnim = "";

    public boolean isMimic = false;
    public boolean isNatural = false;
    public boolean hasBeenInteractedWith = false;
    public boolean hasMadeMimic = false;

    public float lidAngle = 0.0F;
    public float prevLidAngle = 0.0F;

    private AnimPhase animPhase;

    public boolean hasGoldLock;
    public boolean hasVoidLock;
    public boolean hasIronLock;
    public boolean isLocked;
    public UUID owner;

    private final ContainerOpenersCounter openersCounter;

    PCChestTypes type;

    private NonNullList<ItemStack> inventory;

    public PC_BaseChestBlockEntity(PCChestTypes type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.animPhase = AnimPhase.CLOSED;
        this.hasGoldLock = false;
        this.hasVoidLock = false;
        this.hasIronLock = false;
        this.isLocked = false;
        this.owner = null;

        this.openersCounter = new ContainerOpenersCounter() {
            @Override
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                PC_BaseChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
            }

            @Override
            protected void onClose(Level level, BlockPos pos, BlockState state) {
                PC_BaseChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
            }

            @Override
            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
                PC_BaseChestBlockEntity.this.onInvOpenOrClose(level, pos, state, oldCount, newCount);
            }

            @Override
            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof PCChestScreenHandler) {
                    Container inv = ((PCChestScreenHandler) player.containerMenu).getInventory();
                    return inv == PC_BaseChestBlockEntity.this;
                }
                return false;
            }
        };

        this.inventory = NonNullList.withSize(54, ItemStack.EMPTY);
        this.type = type;
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
    }

    public static int getPlayersLookingInChestCount(BlockGetter level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            var blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PC_BaseChestBlockEntity chest) {
                return chest.openersCounter.getOpenerCount();
            }
        }
        return 0;
    }

    public void tick() {
        if (this.level != null) {
            this.prevLidAngle = this.lidAngle;
            if (!this.level.isClientSide) {
                this.openersCounter.recheckOpeners(this.level, this.worldPosition, this.getBlockState());
            }

            if (this.getChestState() == PCChestState.OPENED) {
                this.lidAngle = Math.min(1.0F, this.lidAngle + 0.1F);
            } else {
                this.lidAngle = Math.max(0.0F, this.lidAngle - 0.1F);
            }
        }
    }

    public static void copyInventory(PC_BaseChestBlockEntity from, PC_BaseChestBlockEntity to) {
        NonNullList<ItemStack> fromItems = from.getItems();
        from.setItems(to.getItems());
        to.setItems(fromItems);
    }

    public static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        level.playSound(null, x, y, z, soundEvent, SoundSource.BLOCKS, 0.5F,
                level.random.nextFloat() * 0.1F + 0.9F);
    }

    public static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent soundEvent, float pitchRange) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        level.playSound(null, x, y, z, soundEvent, SoundSource.BLOCKS, 0.5F,
                level.random.nextFloat() * 0.1F + pitchRange);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.inventory);
        }

        this.isMimic = tag.getBoolean("isMimic");
        this.hasGoldLock = tag.getBoolean("hasGoldLock");
        this.hasVoidLock = tag.getBoolean("hasVoidLock");
        this.hasIronLock = tag.getBoolean("hasIronLock");
        this.isLocked = tag.getBoolean("isLocked");
        this.isNatural = tag.getBoolean("isNatural");
        this.hasBeenInteractedWith = tag.getBoolean("hasBeenOpened");
        this.hasMadeMimic = tag.getBoolean("hasMadeMimic");

        if (tag.hasUUID("pc_owner")) {
            this.owner = tag.getUUID("pc_owner");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.inventory);
        }

        tag.putBoolean("isMimic", this.isMimic);
        tag.putBoolean("hasGoldLock", this.hasGoldLock);
        tag.putBoolean("hasVoidLock", this.hasVoidLock);
        tag.putBoolean("hasIronLock", this.hasIronLock);
        tag.putBoolean("isLocked", this.isLocked);
        tag.putBoolean("isNatural", this.isNatural);
        tag.putBoolean("hasBeenOpened", this.hasBeenInteractedWith);
        tag.putBoolean("hasMadeMimic", this.hasMadeMimic);

        if (this.owner != null) {
            tag.putUUID("pc_owner", this.owner);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.inventory = items;
    }

    public void onScheduledTick() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void onInvOpenOrClose(Level level, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        Block block = state.getBlock();
        level.blockEvent(pos, block, 1, newViewerCount);

        if (oldViewerCount != newViewerCount) {
            if (newViewerCount > 0) {
                level.setBlock(pos, state.setValue(CHEST_STATE, PCChestState.OPENED), 3);
            } else {
                level.setBlock(pos, state.setValue(CHEST_STATE, PCChestState.CLOSED), 3);
            }
            this.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            return true;
        }
        return super.triggerEvent(id, type);
    }

    public PCChestState getChestState() {
        return this.getBlockState().getValue(CHEST_STATE);
    }

    public void setChestState(PCChestState state) {
        this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(CHEST_STATE, state), 3);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        if (!this.hasBeenInteractedWith && player.isSpectator()) {
            return null;
        }
        if (this.canOpen(player)) {
            this.unpackLootTable(playerInventory.player);
            return PCChestScreenHandler.createScreenHandler(syncId, playerInventory, this);
        }
        return null;
    }

    private RawAnimation animLoop(String suffix) {
        String typeName = this.type.name().toLowerCase();
        String animName = typeName + "_" + suffix;
        System.out.println("[dungeons_and_arcanus] Playing animation: " + animName);
        return RawAnimation.begin().then(animName, Animation.LoopType.LOOP);
    }

    private RawAnimation animHold(String suffix) {
        String typeName = this.type.name().toLowerCase();
        String animName = typeName + "_" + suffix;
        System.out.println("[dungeons_and_arcanus] Playing animation: " + animName);
        return RawAnimation.begin().then(animName, Animation.LoopType.HOLD_ON_LAST_FRAME);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        // empty in original
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public double getTick(Object o) {
        return this.level != null ? (double) this.level.getGameTime() : 0.0D;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return PCChestScreenHandler.createScreenHandler(syncId, inventory, this);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public int getContainerSize() {
        return 54;
    }

    public PCChestTypes type() {
        return this.type;
    }

    private enum AnimPhase {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING
    }
}
