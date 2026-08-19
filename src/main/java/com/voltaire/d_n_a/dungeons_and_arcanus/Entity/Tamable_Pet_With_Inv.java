package com.voltaire.d_n_a.dungeons_and_arcanus.Entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai.MimicMoveControl;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import com.voltaire.d_n_a.dungeons_and_arcanus.util.MimicCreationUtils;
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
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public abstract class Tamable_Pet_With_Inv extends TamableAnimal implements Container, net.minecraft.world.entity.NeutralMob, MenuProvider {

    public final net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(54);
    public boolean interacting;
    public PCChestTypes type = PCChestTypes.NORMAL;

    private @Nullable UUID angryAt;

    // Data parameters
    private static final EntityDataAccessor<Integer> MIMIC_STATE =
            SynchedEntityData.defineId(Tamable_Pet_With_Inv.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANGER_TIME =
            SynchedEntityData.defineId(Tamable_Pet_With_Inv.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_ABANDONED =
            SynchedEntityData.defineId(Tamable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MIMIC_HAS_LOCK =
            SynchedEntityData.defineId(Tamable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_MIMIC_LOCKED =
            SynchedEntityData.defineId(Tamable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_OPEN_STATE =
            SynchedEntityData.defineId(Tamable_Pet_With_Inv.class, EntityDataSerializers.BOOLEAN);

    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);

    public boolean isFollowingOwner = false;

    // Animation / state constants
    public static final int IS_SLEEPING = 0;
    public static final int IS_IN_AIR   = 1;
    public static final int IS_IDLE     = 2;
    public static final int IS_JUMPING  = 3;
    public static final int IS_BITING   = 4;
    public static final int IS_LANDING  = 5;

    public int viewerCount = 0;
    public int closeAnimationTimer;
    public int openAnimationTimer;
    public int biteAnimationTimer;
    public int isAbandonedTimer = 1200;          // 60 seconds
    public int biteDamageAmount = 2;

    private static final double MOVE_SPEED = 1.0;

    public Tamable_Pet_With_Inv(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();          // equivalent of field_5985 = true
        this.inventory.addListener(this);       // so container changes are noticed
    }

    public void setType(PCChestTypes type) {
        this.type = type;
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 16.0)   // was 10 in original? adjusted to sensible
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ARMOR, 2.0);
    }

    // ---------- Owner / Taming helpers ----------

    @Nullable
    @Override
    public LivingEntity getOwner() {
        UUID uuid = this.getOwnerUUID();
        return uuid == null ? null : this.level().getPlayerByUUID(uuid);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() instanceof ServerPlayer serverPlayer) {
            ((PlayerEntityAccess) serverPlayer).removePetMimicFromOwnedList(this.getUUID());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isTame() && this.getIsAbandoned()) {
            if (this.isAbandonedTimer > 0) {
                --this.isAbandonedTimer;
            } else {
                this.setTame(false);
                MimicCreationUtils.convertPetMimicToHostile(this.level(), this.type, this);
            }
        }
    }

    // ---------- Interaction ----------

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // Already tamed and not abandoned
        if (this.isTame() && !this.getIsAbandoned()) {

            // Shift + owner → toggle sit
            if (player.isShiftKeyDown() && this.isOwnedBy(player)) {
                boolean newSit = !this.isOrderedToSit();
                this.setOrderedToSit(newSit);
                this.updateSitting(player);

                String name = this.hasCustomName()
                        ? this.getCustomName().getString()
                        : this.getName().getString();

                if (player instanceof ServerPlayer serverPlayer) {
                    if (newSit) {
                        serverPlayer.displayClientMessage(
                                Component.translatable("entity.probablychests.is_staying", name), true);
                    } else {
                        serverPlayer.displayClientMessage(
                                Component.translatable("entity.probablychests.is_following", name), true);
                    }
                }

                this.playSound(this.isOrderedToSit() ? this.getSitSound() : this.getStandSound(),
                        this.getSoundVolume(), 0.9F + this.getPitchOffset(0.1F));
                this.jumping = false;
                this.getNavigation().stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            }

            // Food → heal
            FoodProperties food = stack.getFoodProperties(this);
            if (food != null && food.isMeat()) {          // original checked isFood + isMeat
                if (this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    int heal = food.getNutrition();
                    boolean willBeFull = this.getHealth() + heal >= this.getMaxHealth();
                    this.heal(heal);

                    // Heart particles
                    if (this.level() instanceof ServerLevel server) {
                        double ox = (this.random.nextDouble() - 0.5) * 1.2;
                        double oy = this.random.nextDouble();
                        double oz = (this.random.nextDouble() - 0.5) * 1.2;
                        server.sendParticles(ParticleTypes.HEART,
                                this.getX() + ox, this.getY() + 0.2 + oy, this.getZ() + oz,
                                1, 0, 0, 0, 0);
                    }

                    this.playSound(willBeFull ? SoundEvents.PLAYER_LEVELUP : SoundEvents.GENERIC_EAT,
                            this.getSoundVolume(), 1.0F);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }

            // Locked mimic → only owner can open, others get bitten
            if (this.getIsMimicLocked()) {
                if (this.getOwner() == player) {
                    this.openGui(player);
                } else {
                    this.bite(player);
                    this.biteAnimationTimer = 6;
                    this.setMimicState(IS_BITING);
                }
            } else {
                this.openGui(player);
            }
            return InteractionResult.SUCCESS;
        }

        // Not tamed yet → try Mimic Core
        if (stack.is(ModItems.MIMIC_CORE.get())) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (this instanceof ChestMimicPet) {
                // Already a pet variant – just tame & sit
                this.tame(player);
                this.getNavigation().stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.updateSitting(player);
                this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + this.getPitchOffset(0.1F));
                this.level().broadcastEntityEvent(this, (byte) 7);   // hearts
            } else {
                // Hostile → convert to pet
                MimicCreationUtils.convertHostileMimicToPet(this.level(), this, player);
                this.playSound(this.getSitSound(), this.getSoundVolume(), 0.9F + this.getPitchOffset(0.1F));
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    public void bite(LivingEntity target) {
        if (this.isAlive() && target.hurt(this.damageSources().mobAttack(this), this.biteDamageAmount)) {
            this.playSound(PCSounds.MIMIC_BITE.get(), this.getSoundVolume(), 1.5F + this.getPitchOffset(0.2F));
            this.doEnchantDamageEffects(this, target);
        }
    }

    @Nullable
    public MimicMoveControl getMimicMoveControl() {
        MoveControl control = this.getMoveControl();
        return control instanceof MimicMoveControl m ? m : null;
    }

    // ---------- Damage / Fall / Immunity ----------

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;   // no fall damage
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        // empty – original did nothing extra
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source)
                || source.is(net.minecraft.tags.DamageTypeTags.IS_FALL)
                || source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)
                || source.is(net.minecraft.tags.DamageTypeTags.IS_DROWNING)
                || source.is(net.minecraft.tags.DamageTypeTags.IS_FREEZING)
                || source.is(net.minecraft.tags.DamageTypeTags.WITCH_RESISTANT_TO)
                || source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)
                || source.is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE)
                || source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR)
                || source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY);
        // Original listed many specific damage types – this is the modern tag equivalent
    }

    public float getPitchOffset(float range) {
        return (this.random.nextFloat() - this.random.nextFloat()) * range;
    }

    public void updateSitting(Player player) {
        // empty in original – override in subclasses if needed
    }

    @Override
    public float getSoundVolume() {
        return 0.6F;
    }

    // ---------- GUI / Inventory ----------

    public void openGui(Player player) {
        if (this.level().isClientSide) return;

        if (this.viewerCount == 0) {
            this.openAnimationTimer = 12;
            this.setIsOpenState(true);
            this.playSound(this.getOpenSound(), this.getSoundVolume(), 0.8F + this.getPitchOffset(0.1F));
            this.playSound(PCSounds.CLOSE_2.get(), this.getSoundVolume(), 1.5F + this.getPitchOffset(0.1F));
        }
        ++this.viewerCount;

        // Forge way – open the menu
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, this, buf -> {
                buf.writeInt(this.inventory.getContainerSize());
                buf.writeInt(this.getId());
            });
        }
    }

    public void closeGui(Player player) {
        if (this.level().isClientSide) return;

        --this.viewerCount;
        if (this.viewerCount == 0) {
            this.closeAnimationTimer = 12;
            this.setIsOpenState(false);
            this.playSound(this.getCloseSound(), this.getSoundVolume(), 0.8F + this.getPitchOffset(0.1F));
            this.playSound(PCSounds.CLOSE_2.get(), this.getSoundVolume(), 1.0F + this.getPitchOffset(0.1F));
        }
        if (this.viewerCount < 0) {
            this.viewerCount = 0;
        }
    }

    // MenuProvider implementation
    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        PCMimicScreenHandler handler = PCMimicScreenHandler.createScreenHandler(id, playerInv, this.inventory);
        handler.setMimicEntity(this);
        return handler;
    }

    // Container implementation (SimpleContainer already does most of the work)
    @Override
    public int getContainerSize() {
        return this.inventory.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.inventory.removeItem(slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.inventory.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.setItem(slot, stack);
    }

    @Override
    public void setChanged() {
        // nothing special
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isAlive() && player.distanceToSqr(this) < 64.0;
    }

    @Override
    public void clearContent() {
        this.inventory.clearContent();
    }

    // ---------- Loot / Death ----------

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack stack = this.inventory.getItem(i);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
            }
        }
        super.dropAllDeathLoot(source);
    }

    @Override
    public int getMaxFallDistance() {
        return 20;   // original method_5850
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        MobEffectInstance jumpBoost = this.getEffect(MobEffects.JUMP);
        float f = jumpBoost == null ? 0.0F : (float) (jumpBoost.getAmplifier() + 1);
        return Mth.ceil((fallDistance - 20.0F - f) * damageMultiplier);
    }

    // ---------- Sounds ----------

    public float getJumpSoundPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F;
    }

    public SoundEvent getJumpSound() {
        return SoundEvents.SLIME_JUMP;          // original used a generic jump
    }

    protected SoundEvent getSitSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    protected SoundEvent getStandSound() {
        return SoundEvents.SLIME_JUMP;
    }

    protected SoundEvent getCloseSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    protected SoundEvent getOpenSound() {
        return SoundEvents.CHEST_OPEN;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHEST_CLOSE;         // original used a generic
    }

    protected SoundEvent getLandingSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    // ---------- NBT ----------

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("state", this.getMimicState());
        tag.putInt("mimic_state", this.getMimicState());

        ListTag list = new ListTag();
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack stack = this.inventory.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(itemTag);
                list.add(itemTag);
            }
        }
        tag.put("Inventory", list);

        if (this.type != null) {
            tag.putString("ChestType", this.type.name());
        }

        tag.putBoolean("is_abandoned", this.getIsAbandoned());
        tag.putBoolean("mimic_has_lock", this.getMimicHasLock());
        tag.putBoolean("is_mimic_locked", this.getIsMimicLocked());

        this.addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setMimicState(tag.getInt("state"));
        this.setMimicState(tag.getInt("mimic_state"));

        ListTag list = tag.getList("Inventory", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag itemTag = list.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot < this.inventory.getContainerSize()) {
                this.inventory.setItem(slot, ItemStack.of(itemTag));
            }
        }

        if (tag.contains("ChestType")) {
            try {
                this.type = PCChestTypes.valueOf(tag.getString("ChestType"));
            } catch (IllegalArgumentException ignored) {}
        }

        this.readPersistentAngerSaveData(this.level(), tag);
        this.setIsAbandoned(tag.getBoolean("is_abandoned"));
        this.setMimicHasLock(tag.getBoolean("mimic_has_lock"));
        this.setIsMimicLocked(tag.getBoolean("is_mimic_locked"));
    }

    // ---------- Data Accessors ----------

    public void setMimicState(int state) {
        this.entityData.set(MIMIC_STATE, state);
    }

    public int getMimicState() {
        return this.entityData.get(MIMIC_STATE);
    }

    public void setIsOpenState(boolean state) {
        this.entityData.set(IS_OPEN_STATE, state);
    }

    public boolean getIsOpenState() {
        return this.entityData.get(IS_OPEN_STATE);
    }

    public void setIsAbandoned(boolean state) {
        this.entityData.set(IS_ABANDONED, state);
    }

    public boolean getIsAbandoned() {
        return this.entityData.get(IS_ABANDONED);
    }

    public void setMimicHasLock(boolean state) {
        this.entityData.set(MIMIC_HAS_LOCK, state);
    }

    public boolean getMimicHasLock() {
        return this.entityData.get(MIMIC_HAS_LOCK);
    }

    public void setIsMimicLocked(boolean state) {
        this.entityData.set(IS_MIMIC_LOCKED, state);
    }

    public boolean getIsMimicLocked() {
        return this.entityData.get(IS_MIMIC_LOCKED);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MIMIC_STATE, 0);
        this.entityData.define(ANGER_TIME, 0);
        this.entityData.define(IS_ABANDONED, false);
        this.entityData.define(MIMIC_HAS_LOCK, false);
        this.entityData.define(IS_MIMIC_LOCKED, false);
        this.entityData.define(IS_OPEN_STATE, false);
    }

    // ---------- NeutralMob (anger) ----------

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(ANGER_TIME, time);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angryAt = target;
    }

    public int getTicksUntilNextJump() {
        return this.random.nextInt(40) + 5;
    }

    // ---------- Dimensions / other ----------

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.625F * dimensions.height;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    // ---------- Inner Goals (kept almost identical) ----------

    public static class SwimmingGoal extends Goal {
        private final Tamable_Pet_With_Inv mimic;

        public SwimmingGoal(Tamable_Pet_With_Inv mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.JUMP, Goal.Flag.MOVE));
            mimic.getNavigation().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return !this.mimic.isPassenger()
                    && (this.mimic.isInWater() || this.mimic.isInLava())
                    && this.mimic.getMoveControl() instanceof MimicMoveControl;
        }

        @Override
        public void tick() {
            if (this.mimic.getRandom().nextFloat() < 0.8F) {
                this.mimic.getJumpControl().jump();
            }
            MimicMoveControl control = this.mimic.getMimicMoveControl();
            if (control != null) {
                control.move(4.2);
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    public static class IdleGoal extends Goal {
        private final Tamable_Pet_With_Inv mimic;

        public IdleGoal(Tamable_Pet_With_Inv mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return !this.mimic.isPassenger();
        }

        @Override
        public void tick() {}
    }

    public static class SleepGoal extends Goal {
        private final Tamable_Pet_With_Inv mimic;

        public SleepGoal(Tamable_Pet_With_Inv mimic) {
            this.mimic = mimic;
        }

        @Override
        public boolean canUse() {
            return !this.mimic.isPassenger() && this.mimic.getMimicState() == IS_SLEEPING;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.mimic.isPassenger() && this.mimic.getMimicState() == IS_SLEEPING;
        }

        @Override
        public void tick() {
            this.lockToBlock(10.0F, 10.0F);
            MimicMoveControl control = this.mimic.getMimicMoveControl();
            if (control != null) {
                control.look(this.mimic.getYRot(), true);
            }
        }

        private void lockToBlock(float maxYaw, float maxPitch) {
            float targetYaw = Math.round(this.mimic.getYRot() / 90.0F) * 90.0F;
            this.mimic.setYRot(this.changeAngle(this.mimic.getYRot(), targetYaw, maxYaw));
        }

        private float changeAngle(float from, float to, float max) {
            float f = Mth.wrapDegrees(to - from);
            if (f > max) f = max;
            if (f < -max) f = -max;
            return from + f;
        }
    }

    public static class FollowOwnerGoal extends Goal {
        private final Tamable_Pet_With_Inv mimic;
        private final Level world;
        private final PathNavigation navigation;
        private final float maxDistance;
        private final float minDistance;
        private final boolean leavesAllowed;
        private LivingEntity owner;
        private int updateCountdownTicks;
        private float oldWaterPathfindingPenalty;

        public FollowOwnerGoal(Tamable_Pet_With_Inv mimic, double speed,
                               float minDistance, float maxDistance, boolean leavesAllowed) {
            this.mimic = mimic;
            this.world = mimic.level();
            this.navigation = mimic.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.leavesAllowed = leavesAllowed;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));

            if (!(mimic.getNavigation() instanceof GroundPathNavigation)
                    && !(mimic.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        @Override
        public boolean canUse() {
            if (this.mimic.isPassenger()) return false;
            LivingEntity owner = this.mimic.getOwner();
            if (owner == null || owner.isSpectator() || this.mimic.isOrderedToSit()
                    || this.mimic.distanceToSqr(owner) < (double)(this.minDistance * this.minDistance)
                    || this.mimic.getIsAbandoned()) {
                return false;
            }
            this.owner = owner;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.mimic.isPassenger() || this.navigation.isDone()
                    || this.mimic.isOrderedToSit() || this.mimic.getIsAbandoned()) {
                return false;
            }
            return this.mimic.distanceToSqr(this.owner) > (double)(this.maxDistance * this.maxDistance);
        }

        @Override
        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.mimic.getPathfindingMalus(BlockPathTypes.WATER);
            this.mimic.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
            this.mimic.setTarget(this.owner);
            this.mimic.isFollowingOwner = true;
        }

        @Override
        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.mimic.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterPathfindingPenalty);
            this.mimic.isFollowingOwner = false;
        }

        @Override
        public void tick() {
            if (this.owner != null) {
                this.mimic.getLookControl().setLookAt(this.owner, 40.0F, 40.0F);
            }
            MimicMoveControl control = this.mimic.getMimicMoveControl();
            if (control != null) {
                control.look(this.mimic.getYRot(), true);
            }
            this.mimic.getLookControl().setLookAt(this.owner, 40.0F, (float) this.mimic.getMaxHeadXRot());

            if (--this.updateCountdownTicks <= 0) {
                this.updateCountdownTicks = this.adjustedTickDelay(10);
                if (this.mimic.distanceToSqr(this.owner) >= 184.0) {
                    this.tryTeleport();
                } else if (control != null) {
                    control.move(MOVE_SPEED);
                }
            }
        }

        private void tryTeleport() {
            BlockPos pos = this.owner.blockPosition();
            for (int i = 0; i < 10; i++) {
                int x = this.getRandomInt(-3, 3);
                int y = this.getRandomInt(-1, 1);
                int z = this.getRandomInt(-3, 3);
                if (this.tryTeleportTo(pos.getX() + x, pos.getY() + y, pos.getZ() + z)) {
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
            BlockPathTypes type = PathNavigation.getPathfindingType(this.world, pos.mutable());
            if (type != BlockPathTypes.WALKABLE) return false;
            BlockState below = this.world.getBlockState(pos.below());
            BlockPos delta = pos.subtract(this.mimic.blockPosition());
            return this.world.noCollision(this.mimic, this.mimic.getBoundingBox().move(delta));
        }

        private int getRandomInt(int min, int max) {
            return this.mimic.getRandom().nextInt(max - min + 1) + min;
        }
    }
}
