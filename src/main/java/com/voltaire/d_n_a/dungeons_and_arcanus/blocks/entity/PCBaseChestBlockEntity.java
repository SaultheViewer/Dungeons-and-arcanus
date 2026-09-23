package com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCProperties;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCChestScreenHandler;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCChestState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.Animation.LoopType;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class PCBaseChestBlockEntity extends RandomizableContainerBlockEntity implements GeoAnimatable {
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
    private AnimPhase animPhase = AnimPhase.CLOSED;
    public boolean hasGoldLock = false;
    public boolean hasVoidLock = false;
    public boolean hasIronLock = false;
    public boolean isLocked = false;
    public UUID owner = null;
    private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            PCBaseChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
        }

        protected void onClose(Level level, BlockPos pos, BlockState state) {
            PCBaseChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
            PCBaseChestBlockEntity.this.onInvOpenOrClose(level, pos, state, count, openCount);
        }

        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof PCChestScreenHandler) {
                Container inventory = ((PCChestScreenHandler)player.containerMenu).getInventory();
                return inventory == PCBaseChestBlockEntity.this;
            } else {
                return false;
            }
        }
    };
    PCChestTypes type;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(54, ItemStack.EMPTY);

    public PCBaseChestBlockEntity(PCChestTypes type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.type = type;
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
    }

    public static int getPlayersLookingInChestCount(BlockGetter world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        BlockEntity blockEntity;
        return blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(pos)) instanceof PCBaseChestBlockEntity
                ? ((PCBaseChestBlockEntity)blockEntity).stateManager.getOpenerCount()
                : 0;
    }

    public void tick() {
        if (this.level != null) {
            this.prevLidAngle = this.lidAngle;
            if (!this.level.isClientSide) {
                this.stateManager.recheckOpeners(this.level, this.worldPosition, this.getBlockState());
            }

            if (this.getChestState() == PCChestState.OPENED) {
                this.lidAngle = Math.min(1.0F, this.lidAngle + 0.1F);
            } else {
                this.lidAngle = Math.max(0.0F, this.lidAngle - 0.1F);
            }
        }
    }

    public static void copyInventory(PCBaseChestBlockEntity from, PCBaseChestBlockEntity to) {
        NonNullList<ItemStack> defaultedList = from.getItems();
        from.setItems(to.getItems());
        to.setItems(defaultedList);
    }

    public static void playSound(Level world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        double d = pos.getX() + 0.5;
        double e = pos.getY() + 0.5;
        double f = pos.getZ() + 0.5;
        world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    public static void playSound(Level world, BlockPos pos, BlockState state, SoundEvent soundEvent, float pitchRange) {
        double d = pos.getX() + 0.5;
        double e = pos.getY() + 0.5;
        double f = pos.getZ() + 0.5;
        world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + pitchRange);
    }

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
        if (tag.contains("pc_owner")) {
            this.owner = tag.getUUID("pc_owner");
        }
    }

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

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.stateManager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.stateManager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    protected void setItems(NonNullList<ItemStack> itemStacks) {
        this.inventory = itemStacks;
    }

    public void onScheduledTick() {
        if (!this.remove) {
            this.stateManager.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void onInvOpenOrClose(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        Block block = state.getBlock();
        world.blockEvent(pos, block, 1, newViewerCount);
        if (oldViewerCount != newViewerCount) {
            if (newViewerCount > 0) {
                world.setBlock(pos, (BlockState)state.setValue(CHEST_STATE, PCChestState.OPENED), 3);
            } else {
                world.setBlock(pos, (BlockState)state.setValue(CHEST_STATE, PCChestState.CLOSED), 3);
            }

            this.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public boolean triggerEvent(int id, int type) {
        return id == 1 ? true : super.triggerEvent(id, type);
    }

    public PCChestState getChestState() {
        return (PCChestState)this.getBlockState().getValue(CHEST_STATE);
    }

    public void setChestState(PCChestState state) {
        this.getLevel().setBlockAndUpdate(this.getBlockPos(), (BlockState)this.getBlockState().setValue(CHEST_STATE, state));
    }

    @Nullable
    public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        if (!this.hasBeenInteractedWith && player.isSpectator()) {
            return null;
        } else if (this.canOpen(player)) {
            this.unpackLootTable(inventory.player);
            return PCChestScreenHandler.createScreenHandler(syncId, inventory, this);
        } else {
            return null;
        }
    }

    private RawAnimation animLoop(String suffix) {
        String typeName = this.type.name().toLowerCase();
        String animName = typeName + "_" + suffix;
        System.out.println("[Dungeons_and_arcanus] Playing animation: " + animName);
        return RawAnimation.begin().then(animName, LoopType.LOOP);
    }

    private RawAnimation animHold(String suffix) {
        String typeName = this.type.name().toLowerCase();
        String animName = typeName + "_" + suffix;
        System.out.println("[Dungeons_and_arcanus] Playing animation: " + animName);
        return RawAnimation.begin().then(animName, LoopType.HOLD_ON_LAST_FRAME);
    }

    public void registerControllers(ControllerRegistrar registrar) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public double getTick(Object o) {
        return this.level != null ? this.level.getGameTime() : 0.0;
    }

    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return PCChestScreenHandler.createScreenHandler(containerId, inventory, this);
    }

    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

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
        CLOSING;
    }
}
