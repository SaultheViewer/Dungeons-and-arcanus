package com.voltaire.d_n_a.dungeons_and_arcanus.Entity;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai.MimicMoveControl;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai.PCMeleAttackGoal;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCSounds;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.MimicDifficulty;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation.LoopType;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PCChestMimic extends Tameable_Pet_With_Inv implements GeoAnimatable, Enemy {
   public static final RawAnimation IDLE = RawAnimation.begin().then("idle", LoopType.LOOP);
   public static final RawAnimation JUMP = RawAnimation.begin().then("jump", LoopType.PLAY_ONCE).then("flying", LoopType.LOOP);
   public static final RawAnimation CLOSE = RawAnimation.begin().then("land", LoopType.PLAY_ONCE).then("idle", LoopType.LOOP);
   public static final RawAnimation SLEEPING = RawAnimation.begin().then("sleeping", LoopType.LOOP);
   public static final RawAnimation FLYING = RawAnimation.begin().then("flying", LoopType.LOOP);
   public static final RawAnimation LOW_WAG = RawAnimation.begin().then("lowWag", LoopType.LOOP);
   public static final RawAnimation FLYING_WAG = RawAnimation.begin().then("flyingWag", LoopType.LOOP);
   public static final RawAnimation IDLE_WAG = RawAnimation.begin().then("idleWag", LoopType.LOOP);
   public static final RawAnimation NO_WAG = RawAnimation.begin().then("noWag", LoopType.LOOP);
   private static final String MIMIC_CONTROLLER = "mimicController";
   private static final String TONGUE_CONTROLLER = "tongueController";
   private static double moveSpeed = 1.5;
   private static int maxHealth = 50;
   private static int maxDamage = 5;
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private boolean onGroundLastTick;
   private int timeUntilSleep = 0;
   private int jumpEndTimer = 10;
   private int spawnWaitTimer = 10;
   private boolean isAttemptingToSleep = true;

   public PCChestMimic(EntityType<? extends Tameable_Pet_With_Inv> entityType, Level world) {
      super(entityType, world);
      this.noCulling = true;
      this.moveControl = new MimicMoveControl(this);
      this.xpReward = 10;
   }

   public static AttributeSupplier.Builder createMobAttributes() {
      MimicDifficulty mimicDifficulty = Dungeons_and_arcanus.loadedConfig.mimicSettings.mimicDifficulty;
      moveSpeed = mimicDifficulty.getSpeed();
      return LivingEntity.createLivingAttributes()
              .add(Attributes.FOLLOW_RANGE, 12.0)
              .add(Attributes.ATTACK_KNOCKBACK, 2.0)
              .add(Attributes.ATTACK_DAMAGE, mimicDifficulty.getDamage())
              .add(Attributes.MOVEMENT_SPEED, 1.0)
              .add(Attributes.MAX_HEALTH, 50.0)
              .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(7, new Tameable_Pet_With_Inv.IdleGoal(this));
      this.goalSelector.addGoal(5, new PCMeleAttackGoal(this, 1.0, true));
      this.goalSelector.addGoal(6, new Tameable_Pet_With_Inv.SleepGoal(this));
      this.goalSelector.addGoal(1, new Tameable_Pet_With_Inv.SwimmingGoal(this));
      this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.targetSelector
              .addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, livingEntity -> livingEntity != null && Math.abs(livingEntity.getY() - this.getY()) <= 4.0));
      this.targetSelector
              .addGoal(1, new NearestAttackableTargetGoal<>(this, Villager.class, 10, true, false, livingEntity -> livingEntity != null && Math.abs(livingEntity.getY() - this.getY()) <= 4.0));
   }

   private <E extends GeoAnimatable> PlayState chestMovement(AnimationState<E> state) {
      AnimationController<E> controller = state.getController();
      int mimicState = this.getMimicState();
      controller.setAnimationSpeed(1.0);

      RawAnimation animation = switch (mimicState) {
         case 0 -> SLEEPING;
         case 1 -> FLYING;
         case 2 -> IDLE;
         case 3 -> {
            controller.setAnimationSpeed(2.0);
            yield JUMP;
         }
         default -> SLEEPING;
         case 5 -> CLOSE;
      };
      if (controller.getCurrentRawAnimation() != animation) {
         controller.setAnimation(animation);
      }

      return PlayState.CONTINUE;
   }

   private <E extends GeoAnimatable> PlayState tongueMovement(AnimationState<E> state) {
      AnimationController<E> controller = state.getController();
      int mimicState = this.getMimicState();
      controller.setAnimationSpeed(1.0);
      RawAnimation animation = null;
      switch (mimicState) {
         case 0:
            animation = NO_WAG;
            break;
         case 1:
            animation = FLYING_WAG;
            break;
         case 2:
            controller.setAnimationSpeed(1.5);
            animation = IDLE_WAG;
            break;
         case 3:
            controller.setAnimationSpeed(2.0);
            animation = FLYING_WAG;
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
      return this.tickCount;
   }

   protected void jumpFromGround() {
      Vec3 vec3d = this.getDeltaMovement();
      LivingEntity target = this.getTarget();
      double jumpStrength;
      if (target == null) {
         jumpStrength = 1.0;
      } else {
         double yDiff = target.getY() - this.getY();
         yDiff = Math.min(yDiff, 10.0);
         if (yDiff <= 0.0) {
            jumpStrength = 1.0;
         } else {
            jumpStrength = yDiff / 3.5 + 1.0;
         }

         jumpStrength = Math.min(jumpStrength, 3.0);
      }

      this.setDeltaMovement(vec3d.x, this.getJumpPower() * jumpStrength, vec3d.z);
      this.hasImpulse = true;
      if (this.onGround() && this.jumpEndTimer <= 0) {
         this.jumpEndTimer = 10;
         this.setMimicState(3);
      }
   }

   protected boolean canAttack() {
      return this.isEffectiveAi();
   }

   protected float getDamageAmount() {
      return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
   }

   public boolean doHurtTarget(Entity target) {
      boolean bl = target.hurt(this.damageSources().mobAttack(this), (int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
      if (bl) {
         this.playSound(PCSounds.MIMIC_BITE.get(), this.getSoundVolume(), 1.5F + this.getPitchOffset(0.2F));
         this.doEnchantDamageEffects(this, target);
      }

      return bl;
   }

   protected SoundEvent getHurtSound() {
      return null;
   }

   public double squaredDistanceToEntity(LivingEntity entity) {
      Vec3 vector = entity.position();
      double d = this.getX() - vector.x;
      double e = this.getY() - (vector.y + 0.6);
      double f = this.getZ() - vector.z;
      return d * d + e * e + f * f;
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide()) {
         if (this.jumpEndTimer >= 0) {
            this.jumpEndTimer--;
         }

         if (this.spawnWaitTimer > 0) {
            this.spawnWaitTimer--;
         } else if (this.onGround()) {
            if (this.onGroundLastTick) {
               if (this.getMimicState() != 0 && !this.isAttemptingToSleep) {
                  this.timeUntilSleep = 150;
                  this.isAttemptingToSleep = true;
                  this.setMimicState(2);
               }

               if (this.isAttemptingToSleep) {
                  this.timeUntilSleep--;
                  if (this.timeUntilSleep <= 0) {
                     this.timeUntilSleep = 0;
                     this.setMimicState(0);
                  }
               }
            } else {
               this.isAttemptingToSleep = false;
               this.setMimicState(5);
               this.playSound(this.getLandingSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }
         } else {
            this.isAttemptingToSleep = false;
            if (this.getMimicState() != 3) {
               this.setMimicState(1);
            }
         }

         this.onGroundLastTick = this.onGround();
      }
   }

   @Override
   protected boolean shouldDespawnInPeaceful() {
      return true;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("wasOnGround", this.onGroundLastTick);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("Owner")) {
         this.setOwnerUUID(null);
         this.setTame(false);
      }

      this.onGroundLastTick = compound.getBoolean("wasOnGround");
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.setMimicState(0);
   }


   public boolean requiresCustomPersistence() {
      return this.isPassenger();
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return true;
   }

   public static boolean isSpawnDark(ServerLevelAccessor world, BlockPos pos, RandomSource random) {
      if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
         return false;
      }

      DimensionType dimensionType = world.dimensionType();
      int i = dimensionType.monsterSpawnBlockLightLimit();
      if (i < 15 && world.getBrightness(LightLayer.BLOCK, pos) > i) {
         return false;
      }

      PCConfig config = Dungeons_and_arcanus.loadedConfig;
      int j = world.getLevel().isThundering() ? world.getMaxLocalRawBrightness(pos, 10) : world.getMaxLocalRawBrightness(pos);
      return j <= dimensionType.monsterSpawnLightTest().sample(random) * config.mimicSettings.naturalMimicSpawnRate;
   }
   @Override
   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
      return null;
   }



   public static boolean canSpawn(
           EntityType<PCChestMimic> pcChestMimicEntityType, ServerLevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random
   ) {
      return isSpawnDark(serverWorldAccess, blockPos, random);
   }
}
