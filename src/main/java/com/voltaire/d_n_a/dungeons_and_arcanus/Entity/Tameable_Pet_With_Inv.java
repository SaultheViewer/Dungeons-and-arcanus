package com.voltaire.d_n_a.dungeons_and_arcanus.Entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai.MimicMoveControl;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.interfaces.PlayerEntityAccess;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCSounds;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCMimicScreenHandler;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.MimicCreationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

public abstract class Tameable_Pet_With_Inv extends TamableAnimal implements OwnableEntity, ContainerListener, NeutralMob {
    public SimpleContainer inventory = new SimpleContainer(54);
    public boolean interacting;
    PCChestTypes type;
    @Nullable
    private UUID angryAt;
    private static final EntityDataAccessor<Integer> MIMIC_STATE = SynchedEntityData.defineId(Tameable_Pet_With_Inv.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(Tameable_Pet_With_Inv.class, EntityDataSerializers.INT);
    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Boolean> IS_ABANDONED = SynchedEntityData.defineId(Tameable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MIMIC_HAS_LOCK = SynchedEntityData.defineId(Tameable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_MIMIC_LOCKED = SynchedEntityData.defineId(
            Tameable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN
    );
    private static final EntityDataAccessor<Boolean> IS_OPEN_STATE = SynchedEntityData.defineId(Tameable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN);
    public boolean isFollowingOwner = false;
    public static final int IS_SLEEPING = 0;
    public static final int IS_IN_AIR = 1;
    public static final int IS_IDLE = 2;
    public static final int IS_JUMPING = 3;
    public static final int IS_BITING = 4;
    public static final int IS_LANDING = 5;
    public int viewerCount = 0;
    public int closeAnimationTimer;
    public int openAnimationTimer;
    public int biteAnimationTimer;
    public int isAbandonedTimer;
    public int biteDamageAmount = 2;
    private static final double moveSpeed = 1.0;

    public Tameable_Pet_With_Inv(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
        this.type = PCChestTypes.NORMAL;
        this.noCulling = true;
        this.inventory.addListener(this);
        this.isAbandonedTimer = 1200;
    }

    public void setType(PCChestTypes type) {
        this.type = type;
    }

    public static Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    public LivingEntity getOwner() {
        if (this.level() == null) {
            return null;
        }

        UUID ownerUuid = this.getOwnerUUID();
        return ownerUuid == null ? null : this.level().getPlayerByUUID(ownerUuid);
    }

    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() != null && this.getOwner() instanceof ServerPlayer) {
            ((PlayerEntityAccess)this.getOwner()).removePetMimicFromOwnedList(this.getUUID());
        }
    }

    public void tick() {
        super.tick();
        if (this.getOwner() != null && !this.level().isClientSide() && this.isTame() && this.getIsAbandoned()) {
            if (this.isAbandonedTimer > 0) {
                this.isAbandonedTimer--;
            } else {
                this.setTame(false);
                MimicCreationUtils.convertPetMimicToHostile(this.level(), this.type, this);
            }
        }
    }

    public boolean isFood(ItemStack stack) {
        return false;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.level().isClientSide()) {
            return InteractionResult.CONSUME;
        }

        if (this.isTame() && !this.getIsAbandoned()) {
            if (player.isShiftKeyDown() && this.isOwnedBy(player)) {
                boolean newState = !this.isOrderedToSit();
                this.setOrderedToSit(newState);
                this.updateSitting(player);
                String name = this.hasCustomName() ? this.getCustomName().getString() : this.getName().getString();
                if (player instanceof ServerPlayer serverPlayer) {
                    if (newState) {
                        serverPlayer.displayClientMessage(Component.translatable("entity.d_n_a.is_staying", new Object[]{name}), true);
                    } else {
                        serverPlayer.displayClientMessage(Component.translatable("entity.d_n_a.is_following", new Object[]{name}), true);
                    }
                }

                this.playSound(this.isOrderedToSit() ? this.getSitSound() : this.getStandSound(), this.getSoundVolume(), 0.9F + this.getPitchOffset(0.1F));
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            } else if (itemStack.getItem().isEdible() && itemStack.getItem().getFoodProperties().isMeat()) {
                if (this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    int healAmount = itemStack.getItem().getFoodProperties().getNutrition();
                    boolean willBeFull = this.getHealth() + healAmount >= this.getMaxHealth();
                    this.heal(healAmount);
                    ServerLevel world = (ServerLevel)this.level();
                    double offsetX = (this.random.nextDouble() - 0.5) * 1.2;
                    double offsetY = this.random.nextDouble() * 1.0;
                    double offsetZ = (this.random.nextDouble() - 0.5) * 1.2;
                    world.sendParticles(ParticleTypes.HEART, this.getX() + offsetX, this.getY() + 0.2 + offsetY, this.getZ() + offsetZ, 1, 0.0, 0.0, 0.0, 0.0);
                    if (willBeFull) {
                        this.playSound(SoundEvents.PLAYER_BURP, this.getSoundVolume(), 1.0F);
                    } else {
                        this.playSound(SoundEvents.GENERIC_EAT, this.getSoundVolume(), 1.0F);
                    }

                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                if (this.getIsMimicLocked()) {
                    if (this.getOwner() == player) {
                        this.openGui(player);
                    } else {
                        this.bite(player);
                        this.biteAnimationTimer = 6;
                        this.setMimicState(4);
                    }
                } else {
                    this.openGui(player);
                }

                return InteractionResult.SUCCESS;
            }
        } else if (itemStack.is(ModItems.MIMIC_CORE.get())) {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            if (this instanceof ChestMimicPet) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.updateSitting(player);
                this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + this.getPitchOffset(0.1F));
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                MimicCreationUtils.convertHostileMimicToPet(this.level(), this, player);
                this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + this.getPitchOffset(0.1F));
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public void bite(LivingEntity target) {
        if (this.isAlive() && target.hurt(this.damageSources().mobAttack(this), this.biteDamageAmount)) {
            this.playSound(PCSounds.MIMIC_BITE.get(), this.getSoundVolume(), 1.5F + this.getPitchOffset(0.2F));
            this.doEnchantDamageEffects(this, target);
        }
    }

    @Nullable
    public MimicMoveControl getMimicMoveControl() {
        return this.getMoveControl() instanceof MimicMoveControl mimicMoveControl ? mimicMoveControl : null;
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    protected void playHurtSound(DamageSource source) {
    }

    public boolean fireImmune() {
        return true;
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source)
                || source.is(DamageTypes.FALL)
                || source.is(DamageTypes.STALAGMITE)
                || source.is(DamageTypes.IN_FIRE)
                || source.is(DamageTypes.ON_FIRE)
                || source.is(DamageTypes.LAVA)
                || source.is(DamageTypes.HOT_FLOOR)
                || source.is(DamageTypes.DROWN)
                || source.is(DamageTypes.IN_WALL)
                || source.is(DamageTypes.CACTUS);
    }

    public float getPitchOffset(float range) {
        return (this.random.nextFloat() - this.random.nextFloat()) * range;
    }

    public void updateSitting(Player player) {
    }

    public float getSoundVolume() {
        return 0.6F;
    }

    public void containerChanged(Container container) {
    }

    public EntityDataAccessor<Integer> getMimicStateVariable() {
        return MIMIC_STATE;
    }

    public void openGui(Player player) {
        if (player.level() != null && !this.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            if (this.viewerCount == 0) {
                this.openAnimationTimer = 12;
                this.setIsOpenState(true);
                this.playSound(this.getOpenSound(), this.getSoundVolume(), 0.8F + this.getPitchOffset(0.1F));
                this.playSound(PCSounds.CLOSE_2.get(), this.getSoundVolume(), 1.5F + this.getPitchOffset(0.1F));
            }

            this.viewerCount++;
            net.minecraftforge.network.NetworkHooks.openScreen(
                    serverPlayer,
                    new Tameable_Pet_With_Inv.MimicScreenHandlerFactory(),
                    buf -> {
                        buf.writeInt(this.inventory.getContainerSize());
                        buf.writeVarInt(this.getId());
                    }
            );
        }
    }

    public void closeGui(Player player) {
        if (player.level() != null && !this.level().isClientSide()) {
            this.viewerCount--;
            if (this.viewerCount == 0) {
                this.closeAnimationTimer = 12;
                this.setIsOpenState(false);
                this.playSound(this.getCloseSound(), this.getSoundVolume(), 0.8F + this.getPitchOffset(0.1F));
                this.playSound(PCSounds.CLOSE_2.get(), this.getSoundVolume(), 1.0F + this.getPitchOffset(0.1F));
            }

            if (this.viewerCount < 0) {
                System.out.println("this should not happen but i added a check just in case. Viewer count of pet mimic is less than 0");
                this.viewerCount = 0;
            }
        }
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer) {
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                this.spawnAtLocation(itemstack);
            }
        }
    }

    public int getMaxFallDistance() {
        return 20;
    }

    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        MobEffectInstance statusEffectInstance = this.getEffect(MobEffects.JUMP);
        float f = statusEffectInstance == null ? 0.0F : statusEffectInstance.getAmplifier() + 1;
        return Mth.ceil((fallDistance - 20.0F - f) * damageMultiplier);
    }

    public float getJumpSoundPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F;
    }

    public SoundEvent getJumpSound() {
        return SoundEvents.CHEST_OPEN;
    }

    protected SoundEvent getSitSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    protected SoundEvent getStandSound() {
        return SoundEvents.CHEST_OPEN;
    }

    protected SoundEvent getCloseSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    protected SoundEvent getOpenSound() {
        return SoundEvents.CHEST_OPEN;
    }

    protected SoundEvent getHurtSound() {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR;
    }

    protected SoundEvent getLandingSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    static void playSound(Level world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        double d = pos.getX() + 0.5;
        double e = pos.getY() + 0.5;
        double f = pos.getZ() + 0.5;
        world.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.625F * dimensions.height;
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("state", this.getMimicState());
        ListTag listnbt = new ListTag();

        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack itemstack = this.inventory.getItem(i);
            CompoundTag compoundnbt = new CompoundTag();
            compoundnbt.putByte("Slot", (byte)i);
            itemstack.save(compoundnbt);
            listnbt.add(compoundnbt);
        }

        if (this.type != null) {
            compound.putString("ChestType", this.type.name());
        }

        compound.put("Inventory", listnbt);
        compound.putInt("mimic_state", this.getMimicState());
        compound.putBoolean("is_abandoned", this.getIsAbandoned());
        compound.putBoolean("mimic_has_lock", this.getMimicHasLock());
        compound.putBoolean("is_mimic_locked", this.getIsMimicLocked());
        this.addPersistentAngerSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setMimicState(compound.getInt("state"));
        ListTag listnbt = compound.getList("Inventory", 10);

        for (int i = 0; i < listnbt.size(); i++) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            this.inventory.setItem(j, ItemStack.of(compoundnbt));
        }

        if (compound.contains("ChestType")) {
            this.type = PCChestTypes.valueOf(compound.getString("ChestType"));
        }

        this.readPersistentAngerSaveData(this.level(), compound);
        this.setMimicState(compound.getInt("mimic_state"));
        this.setIsAbandoned(compound.getBoolean("is_abandoned"));
        this.setMimicHasLock(compound.getBoolean("mimic_has_lock"));
        this.setIsMimicLocked(compound.getBoolean("is_mimic_locked"));
    }

    public PCChestTypes getChestType() {
        return this.type;
    }

    public void setMimicState(int state) {
        this.entityData.set(MIMIC_STATE, state);
    }

    public int getMimicState() {
        return (Integer)this.entityData.get(MIMIC_STATE);
    }

    public void setIsOpenState(boolean state) {
        this.entityData.set(IS_OPEN_STATE, state);
    }

    public boolean getIsOpenState() {
        return (Boolean)this.entityData.get(IS_OPEN_STATE);
    }

    public void setIsAbandoned(boolean state) {
        this.entityData.set(IS_ABANDONED, state);
    }

    public boolean getIsAbandoned() {
        return (Boolean)this.entityData.get(IS_ABANDONED);
    }

    public void setMimicHasLock(boolean state) {
        this.entityData.set(MIMIC_HAS_LOCK, state);
    }

    public boolean getMimicHasLock() {
        return (Boolean)this.entityData.get(MIMIC_HAS_LOCK);
    }

    public void setIsMimicLocked(boolean state) {
        this.entityData.set(IS_MIMIC_LOCKED, state);
    }

    public boolean getIsMimicLocked() {
        return (Boolean)this.entityData.get(IS_MIMIC_LOCKED);
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGER_TIME, 0);
        this.entityData.define(IS_ABANDONED, false);
        this.entityData.define(MIMIC_HAS_LOCK, false);
        this.entityData.define(IS_MIMIC_LOCKED, false);
        this.entityData.define(IS_OPEN_STATE, false);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean canFreeze() {
        return false;
    }

    public boolean areInventoriesDifferent(Container other) {
        return this.inventory != other;
    }

    public int getRemainingPersistentAngerTime() {
        return (Integer)this.entityData.get(ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.entityData.set(ANGER_TIME, remainingPersistentAngerTime);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.angryAt = persistentAngerTarget;
    }

    public int getTicksUntilNextJump() {
        return this.random.nextInt(40) + 5;
    }

    static class FollowOwnerGoal extends Goal {
        private final Tameable_Pet_With_Inv mimic;
        private final LevelReader world;
        private final PathNavigation navigation;
        private final float maxDistance;
        private final float minDistance;
        private final boolean leavesAllowed;
        private LivingEntity owner;
        private int updateCountdownTicks;
        private float oldWaterPathfindingPenalty;

        public FollowOwnerGoal(Tameable_Pet_With_Inv mimic, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
            this.mimic = mimic;
            this.world = mimic.level();
            this.navigation = mimic.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.leavesAllowed = leavesAllowed;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
            if (!(mimic.getNavigation() instanceof GroundPathNavigation) && !(mimic.getNavigation() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            if (this.mimic.isPassenger()) {
                return false;
            }

            LivingEntity livingEntity = this.mimic.getOwner();
            if (livingEntity == null) {
                return false;
            }

            if (livingEntity.isSpectator()) {
                return false;
            }

            if (this.mimic.isOrderedToSit()) {
                return false;
            }

            if (this.mimic.distanceToSqr(livingEntity) < this.minDistance * this.minDistance) {
                return false;
            }

            if (this.mimic.getIsAbandoned()) {
                return false;
            }

            this.owner = livingEntity;
            return true;
        }

        public boolean canContinueToUse() {
            if (this.mimic.isPassenger()) {
                return false;
            } else if (this.navigation.isDone()) {
                return false;
            } else if (this.mimic.isOrderedToSit()) {
                return false;
            } else {
                return this.mimic.getIsAbandoned() ? false : !(this.mimic.distanceToSqr(this.owner) <= this.maxDistance * this.maxDistance);
            }
        }

        public void tick() {
            if (this.mimic.isPassenger()) {
                this.navigation.stop();
            } else {
                if (this.owner != null) {
                    this.mimic.lookAt(this.owner, 40.0F, 40.0F);
                }

                MimicMoveControl moveControl = this.mimic.getMimicMoveControl();
                if (moveControl != null) {
                    moveControl.look(this.mimic.getYRot(), true);
                }

                this.mimic.getLookControl().setLookAt(this.owner, 40.0F, this.mimic.getMaxHeadXRot());
                if (--this.updateCountdownTicks <= 0) {
                    this.updateCountdownTicks = this.adjustedTickDelay(10);
                    if (this.mimic.distanceToSqr(this.owner) >= 184.0) {
                        this.tryTeleport();
                    } else if (moveControl != null) {
                        moveControl.move(1.0);
                    }
                }
            }
        }

        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.mimic.getPathfindingMalus(BlockPathTypes.WATER);
            this.mimic.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
            this.mimic.setTarget(this.owner);
            this.mimic.isFollowingOwner = true;
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.mimic.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterPathfindingPenalty);
            this.mimic.isFollowingOwner = false;
        }

        private void tryTeleport() {
            BlockPos blockPos = this.owner.blockPosition();

            for (int i = 0; i < 10; i++) {
                int j = this.getRandomInt(-3, 3);
                int k = this.getRandomInt(-1, 1);
                int l = this.getRandomInt(-3, 3);
                boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
                if (bl) {
                    return;
                }
            }
        }

        private boolean tryTeleportTo(int x, int y, int z) {
            if (Math.abs(x - this.owner.getX()) < 2.0 && Math.abs(z - this.owner.getZ()) < 2.0) {
                return false;
            }

            if (!this.canTeleportTo(new BlockPos(x, y, z))) {
                return false;
            }

            this.mimic.moveTo(x + 0.5, y, z + 0.5, this.mimic.getYRot(), this.mimic.getXRot());
            this.navigation.stop();
            return true;
        }

        private boolean canTeleportTo(BlockPos pos) {
            BlockPathTypes pathNodeType = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, pos.mutable());
            if (pathNodeType != BlockPathTypes.WALKABLE) {
                return false;
            }

            BlockState blockState = this.world.getBlockState(pos.below());
            BlockPos blockPos = pos.subtract(this.mimic.blockPosition());
            return this.world.noCollision(this.mimic, this.mimic.getBoundingBox().move(blockPos));
        }

        private int getRandomInt(int min, int max) {
            return this.mimic.getRandom().nextInt(max - min + 1) + min;
        }
    }

    static class IdleGoal extends Goal {
        private final Tameable_Pet_With_Inv mimic;

        public IdleGoal(Tameable_Pet_With_Inv mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
        }

        public boolean canUse() {
            return !this.mimic.isPassenger();
        }

        public void tick() {
        }
    }

    private class MimicScreenHandlerFactory implements MenuProvider {
        private Tameable_Pet_With_Inv mimic() {
            return Tameable_Pet_With_Inv.this;
        }

        public Component getDisplayName() {
            return this.mimic().getDisplayName();
        }

        public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
            SimpleContainer mimicInv = this.mimic().inventory;
            PCMimicScreenHandler screenHandler = PCMimicScreenHandler.createScreenHandler(syncId, inv, mimicInv);
            screenHandler.setMimicEntity(this.mimic());
            return screenHandler;
        }
    }

    static class SleepGoal extends Goal {
        private final Tameable_Pet_With_Inv mimic;

        public SleepGoal(Tameable_Pet_With_Inv mimic) {
            this.mimic = mimic;
        }

        public boolean canUse() {
            return !this.mimic.isPassenger() && this.mimic.getMimicState() == 0;
        }

        public boolean canContinueToUse() {
            return !this.mimic.isPassenger() && this.mimic.getMimicState() == 0;
        }

        public void tick() {
            if (!this.mimic.isPassenger()) {
                this.lockToBlock(10.0F, 10.0F);
                MimicMoveControl moveControl = this.mimic.getMimicMoveControl();
                if (moveControl != null) {
                    moveControl.look(this.mimic.getYRot(), true);
                }
            }
        }

        public void lockToBlock(float maxYawChange, float maxPitchChange) {
            float r = Math.round(this.mimic.getYHeadRot() / 90.0F) * 90.0F;
            double x = this.mimic.getBlockX() - this.mimic.getX();
            double z = this.mimic.getBlockZ() - this.mimic.getZ();
            this.mimic.setYRot(this.changeAngle(this.mimic.getYRot(), r, maxYawChange));
        }

        private float changeAngle(float from, float to, float max) {
            float f = Mth.wrapDegrees(to - from);
            if (f > max) {
                f = max;
            }

            if (f < -max) {
                f = -max;
            }

            return from + f;
        }
    }

    static class SwimmingGoal extends Goal {
        private final Tameable_Pet_With_Inv mimic;

        public SwimmingGoal(Tameable_Pet_With_Inv mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            mimic.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return !this.mimic.isPassenger() && (this.mimic.isInWater() || this.mimic.isInLava()) && this.mimic.getMoveControl() instanceof MimicMoveControl;
        }

        public void tick() {
            if (!this.mimic.isPassenger()) {
                if (this.mimic.getRandom().nextFloat() < 0.8F) {
                    this.mimic.getJumpControl().jump();
                }

                MimicMoveControl moveControl = this.mimic.getMimicMoveControl();
                if (moveControl != null) {
                    moveControl.move(4.2);
                }
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
