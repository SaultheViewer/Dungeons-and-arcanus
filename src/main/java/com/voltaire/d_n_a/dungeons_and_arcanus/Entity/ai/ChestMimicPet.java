//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai.MimicMoveControl;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai.PCMeleeAttackGoal;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation.LoopType;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class ChestMimicPet extends Tameable_Pet_With_Inv implements GeoAnimatable, OwnableEntity {
    public static final RawAnimation IDLE;
    public static final RawAnimation JUMP;
    public static final RawAnimation CLOSE_SITTING;
    public static final RawAnimation CLOSE_STANDING;
    public static final RawAnimation OPENING;
    public static final RawAnimation OPENED;
    public static final RawAnimation SITTING;
    public static final RawAnimation STANDING;
    public static final RawAnimation FLYING;
    public static final RawAnimation BITING;
    public static final RawAnimation LOW_WAG;
    public static final RawAnimation FLYING_WAG;
    public static final RawAnimation IDLE_WAG;
    public static final RawAnimation NO_WAG;
    private static final String MIMIC_CONTROLLER = "mimicController";
    private static final String TONGUE_CONTROLLER = "tongueController";
    public SimpleContainer inventory = new SimpleContainer(54);
    PCChestTypes type;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean onGroundLastTick;
    private int jumpEndTimer = 10;
    private int spawnWaitTimer = 5;

    public ChestMimicPet(EntityType<? extends Tameable_Pet_With_Inv> entityType, Level world) {
        super(entityType, world);
        this.type = PCChestTypes.NORMAL;
        this.noCulling = true;
        this.inventory.addListener(this);
        this.moveControl = new MimicMoveControl(this);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, (double)12.0F).add(Attributes.ATTACK_KNOCKBACK, (double)2.0F).add(Attributes.ATTACK_DAMAGE, (double)5.0F).add(Attributes.MOVEMENT_SPEED, (double)1.0F).add(Attributes.MAX_HEALTH, (double)50.0F).add(Attributes.KNOCKBACK_RESISTANCE, (double)0.5F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new Tameable_Pet_With_Inv.SwimmingGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(5, new PCMeleeAttackGoal(this, (double)1.0F, true));
        this.goalSelector.addGoal(6, new Tameable_Pet_With_Inv.FollowOwnerGoal(this, (double)1.0F, 5.0F, 2.0F, false));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal(this, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
    }

    public EntityGetter level() {
        return null;
    }

    public boolean isFood(ItemStack stack) {
        return false;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    private <E extends GeoAnimatable> PlayState chestMovement(AnimationState<E> state) {
        AnimationController<E> controller = state.getController();
        int mimicState = this.getMimicState();
        controller.setAnimationSpeed((double)1.0F);
        RawAnimation animation;
        switch (mimicState) {
            case 1:
                controller.setTransitionLength(2);
                animation = FLYING;
                break;
            case 2:
                controller.setTransitionLength(6);
                if (this.getIsOpenState()) {
                    animation = OPENED;
                } else if (this.isInSittingPose()) {
                    animation = SITTING;
                } else {
                    animation = STANDING;
                }
                break;
            case 3:
                controller.setAnimationSpeed((double)2.0F);
                controller.setTransitionLength(6);
                animation = JUMP;
                break;
            case 4:
                controller.setAnimationSpeed((double)1.5F);
                controller.setTransitionLength(2);
                animation = BITING;
                break;
            default:
                animation = STANDING;
        }

        if (controller.getCurrentRawAnimation() != animation) {
            controller.forceAnimationReset();
            controller.setAnimation(animation);
        }

        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState tongueMovement(AnimationState<E> state) {
        AnimationController<E> controller = state.getController();
        int mimicState = this.getMimicState();
        controller.setAnimationSpeed((double)1.0F);
        RawAnimation animation = null;
        switch (mimicState) {
            case 1:
                controller.setTransitionLength(2);
                animation = FLYING_WAG;
                break;
            case 2:
                controller.setTransitionLength(6);
                if (this.getIsOpenState()) {
                    animation = IDLE_WAG;
                } else if (this.isInSittingPose()) {
                    animation = NO_WAG;
                } else {
                    animation = LOW_WAG;
                }
                break;
            case 3:
                controller.setAnimationSpeed((double)2.0F);
                animation = FLYING_WAG;
            case 4:
        }

        if (animation != null && controller.getCurrentRawAnimation() != animation) {
            controller.setAnimation(animation);
        }

        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController[]{new AnimationController(this, "mimicController", 0, this::chestMovement)});
        controllers.add(new AnimationController[]{new AnimationController(this, "tongueController", 0, this::tongueMovement)});
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public double getTick(Object entity) {
        return (double)this.tickCount;
    }

    protected void jumpFromGround() {
        Vec3 vec3d = this.getDeltaMovement();
        LivingEntity target = this.getTarget();
        double jumpStrength;
        if (this.isFollowingOwner) {
            jumpStrength = (double)1.0F;
        } else if (target != null && target.isAlive()) {
            double yDiff = target.getY() - this.getY();
            yDiff = Math.min(yDiff, (double)10.0F);
            if (yDiff <= (double)0.0F) {
                jumpStrength = (double)1.0F;
            } else {
                jumpStrength = yDiff / (double)3.5F + (double)1.0F;
            }

            jumpStrength = Math.min(jumpStrength, (double)3.0F);
        } else {
            jumpStrength = (double)1.0F;
        }

        this.setDeltaMovement(vec3d.x, (double)this.getJumpPower() * jumpStrength, vec3d.z);
        this.hasImpulse = true;
        if (this.onGround() && this.jumpEndTimer <= 0) {
            this.jumpEndTimer = 10;
            this.setMimicState(3);
        }

    }

    private void alertNearbyMimics(LivingEntity target) {
        if (target != null) {
            double range = (double)16.0F;

            for(ChestMimicPet mimic : this.level().getEntitiesOfClass(ChestMimicPet.class, this.getBoundingBox().inflate(range), (e) -> e != this && e.isTame() && e.getOwner() == this.getOwner())) {
                if (!mimic.isOrderedToSit() && mimic.wantsToAttack(target, this.getOwner()) && mimic.getTarget() == null) {
                    mimic.setTarget(target);
                }
            }

        }
    }

    public int getTicksUntilNextJump() {
        return 10;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }

    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.tickCount % 100 == 0) {
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0, true, false));
            }

            LivingEntity owner = this.getOwner();
            if (owner != null && !this.getIsAbandoned()) {
                double distSq = this.distanceToSqr(owner);
                boolean isFar = distSq > (double)144.0F;
                boolean isUnderwater = this.isUnderWater();
                boolean stuckUnderwater = isUnderwater && this.getNavigation().isDone() && this.getDeltaMovement().lengthSqr() < 0.01;
                boolean busy = this.isOrderedToSit() || this.getTarget() != null;
                if (!busy) {
                    if (isFar) {
                        this.tryTeleportNearOwner(owner);
                    } else if (stuckUnderwater && this.tickCount % 40 == 0) {
                        this.tryTeleportNearOwner(owner);
                    }
                }
            }

            if (this.jumpEndTimer >= 0) {
                --this.jumpEndTimer;
            }

            if (this.biteAnimationTimer > 0) {
                --this.biteAnimationTimer;
            }

            if (this.onGround()) {
                if (!this.onGroundLastTick) {
                    this.playSound(this.getLandingSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
                }

                if (this.biteAnimationTimer <= 0) {
                    this.setMimicState(2);
                }
            } else if (this.getMimicState() != 3) {
                if (this.spawnWaitTimer > 0) {
                    --this.spawnWaitTimer;
                } else {
                    this.setMimicState(1);
                }
            }

            this.onGroundLastTick = this.onGround();
            if (this.getIsAbandoned()) {
                this.setMimicState(2);
            }

        }
    }

    private void tryTeleportNearOwner(LivingEntity owner) {
        Vec3 ownerPos = owner.position();
        double offsetX = (double)this.random.nextIntBetweenInclusive(-2, 2);
        double offsetZ = (double)this.random.nextIntBetweenInclusive(-2, 2);
        this.moveTo(ownerPos.x + offsetX, ownerPos.y, ownerPos.z + offsetZ, this.getYRot(), this.getXRot());
        this.getNavigation().stop();
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("wasOnGround", this.onGroundLastTick);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.onGroundLastTick = compound.getBoolean("wasOnGround");
    }

    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    protected void defineSynchedData() {
        this.entityData.define(this.getMimicStateVariable(), 2);
        super.defineSynchedData();
    }

    public boolean doHurtTarget(Entity target) {
        boolean bl = target.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (bl) {
            this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + this.getPitchOffset(0.2F));
            this.doEnchantDamageEffects(this, target);
            if (target instanceof LivingEntity) {
                LivingEntity living = (LivingEntity)target;
                this.alertNearbyMimics(living);
            }
        }

        return bl;
    }

    protected SoundEvent getHurtSound() {
        return null;
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Ghast && !this.getIsAbandoned()) {
            return false;
        } else if (target instanceof ChestMimicPet) {
            ChestMimicPet mimic = (ChestMimicPet)target;
            return !mimic.isTame() || mimic.getOwner() != owner;
        } else if (target instanceof Player && owner instanceof Player && !((Player)owner).canHarmPlayer((Player)target)) {
            return false;
        } else {
            return !(target instanceof TamableAnimal) || !((TamableAnimal)target).isTame();
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity attacker = source.getEntity();
            if (!this.level().isClientSide && source.is(DamageTypes.DROWN) && !this.isOrderedToSit() && this.getOwner() != null) {
                LivingEntity owner = this.getOwner();
                double offsetX = (double)this.random.nextIntBetweenInclusive(-2, 2);
                double offsetZ = (double)this.random.nextIntBetweenInclusive(-2, 2);
                this.moveTo(owner.getX() + offsetX, owner.getY(), owner.getZ() + offsetZ, this.getYRot(), this.getXRot());
                this.getNavigation().stop();
            }

            if (!this.level().isClientSide) {
                if (!this.isOrderedToSit()) {
                    this.setMimicState(2);
                }

                if (attacker instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity)attacker;
                    if (!this.isOrderedToSit() && this.wantsToAttack(living, this.getOwner())) {
                        this.setTarget(living);
                    }

                    for(ChestMimicPet mimic : this.level().getEntitiesOfClass(ChestMimicPet.class, this.getBoundingBox().inflate((double)16.0F), (e) -> e.isTame() && e.getOwner() == this.getOwner())) {
                        if (!mimic.isOrderedToSit() && mimic.wantsToAttack(living, this.getOwner())) {
                            mimic.setTarget(living);
                        }
                    }
                }
            }

            if (attacker != null && !(attacker instanceof Player) && !(attacker instanceof AbstractArrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.hurt(source, amount);
        }
    }

    public boolean canBeLeashed(Player player) {
        return false;
    }

    static {
        IDLE = RawAnimation.begin().then("idle", LoopType.LOOP);
        JUMP = RawAnimation.begin().then("jump", LoopType.PLAY_ONCE).then("flying", LoopType.LOOP);
        CLOSE_SITTING = RawAnimation.begin().then("close", LoopType.PLAY_ONCE).then("sleeping", LoopType.LOOP);
        CLOSE_STANDING = RawAnimation.begin().then("close", LoopType.PLAY_ONCE).then("standing", LoopType.LOOP);
        OPENING = RawAnimation.begin().then("open", LoopType.PLAY_ONCE);
        OPENED = RawAnimation.begin().then("opened", LoopType.LOOP);
        SITTING = RawAnimation.begin().then("sleeping", LoopType.LOOP);
        STANDING = RawAnimation.begin().then("standing", LoopType.LOOP);
        FLYING = RawAnimation.begin().then("flying", LoopType.LOOP);
        BITING = RawAnimation.begin().then("bite", LoopType.PLAY_ONCE);
        LOW_WAG = RawAnimation.begin().then("lowWag", LoopType.LOOP);
        FLYING_WAG = RawAnimation.begin().then("flyingWag", LoopType.LOOP);
        IDLE_WAG = RawAnimation.begin().then("idleWag", LoopType.LOOP);
        NO_WAG = RawAnimation.begin().then("noWag", LoopType.LOOP);
    }

    class PetMimicEscapeDangerGoal extends PCMimicEscapeDangerGoal {
        public PetMimicEscapeDangerGoal(double speed) {
            super(ChestMimicPet.this, speed);
        }

        protected boolean isInDanger() {
            return this.mob.isFreezing() || this.mob.isOnFire();
        }
    }

    static class SitGoal extends Goal {
        private final ChestMimicPet mimic;

        public SitGoal(ChestMimicPet mimic) {
            this.mimic = mimic;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canContinueToUse() {
            return this.mimic.isOrderedToSit();
        }

        public boolean canUse() {
            if (!this.mimic.isTame()) {
                return false;
            } else if (this.mimic.isInWaterOrBubble()) {
                return false;
            } else if (!this.mimic.onGround()) {
                return false;
            } else {
                LivingEntity owner = this.mimic.getOwner();
                if (owner == null) {
                    return true;
                } else {
                    return this.mimic.distanceToSqr(owner) < (double)144.0F && owner.getLastHurtByMob() != null ? false : this.mimic.isOrderedToSit();
                }
            }
        }

        public void start() {
            this.mimic.getNavigation().stop();
            this.mimic.setInSittingPose(true);
        }

        public void stop() {
            this.mimic.setInSittingPose(false);
        }
    }
}
