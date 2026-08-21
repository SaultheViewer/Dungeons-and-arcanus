package com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCSounds;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.MimicDifficulty;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
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

import java.util.UUID;

public class PCChestMimic extends Tameable_Pet_With_Inv implements GeoAnimatable, Enemy {
   public static final RawAnimation IDLE;
   public static final RawAnimation JUMP;
   public static final RawAnimation CLOSE;
   public static final RawAnimation SLEEPING;
   public static final RawAnimation FLYING;
   public static final RawAnimation LOW_WAG;
   public static final RawAnimation FLYING_WAG;
   public static final RawAnimation IDLE_WAG;
   public static final RawAnimation NO_WAG;
   private static final String MIMIC_CONTROLLER = "mimicController";
   private static final String TONGUE_CONTROLLER = "tongueController";
   private static double moveSpeed;
   private static int maxHealth;
   private static int maxDamage;
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
      return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, (double)12.0F).add(Attributes.ATTACK_KNOCKBACK, (double)2.0F).add(Attributes.ATTACK_DAMAGE, (double)mimicDifficulty.getDamage()).add(Attributes.MOVEMENT_SPEED, (double)1.0F).add(Attributes.MAX_HEALTH, (double)50.0F).add(Attributes.KNOCKBACK_RESISTANCE, (double)0.5F);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(7, new Tameable_Pet_With_Inv.IdleGoal(this));
      this.goalSelector.addGoal(5, new PCMeleAttackGoal(this, (double)1.0F, true));
      this.goalSelector.addGoal(6, new Tameable_Pet_With_Inv.SleepGoal(this));
      this.goalSelector.addGoal(1, new Tameable_Pet_With_Inv.SwimmingGoal(this));
      this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (livingEntity) -> Math.abs(livingEntity.getY() - this.getY()) <= (double)4.0F));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Villager.class, 10, true, false, (livingEntity) -> Math.abs(livingEntity.getY() - this.getY()) <= (double)4.0F));
   }

   private <E extends GeoAnimatable> PlayState chestMovement(AnimationState<E> state) {
      AnimationController<E> controller = state.getController();
      int mimicState = this.getMimicState();
      controller.setAnimationSpeed((double)1.0F);
      RawAnimation var10000;
      switch (mimicState) {
         case 0:
            var10000 = SLEEPING;
            break;
         case 1:
            var10000 = FLYING;
            break;
         case 2:
            var10000 = IDLE;
            break;
         case 3:
            controller.setAnimationSpeed((double)2.0F);
            var10000 = JUMP;
            break;
         case 4:
         default:
            var10000 = SLEEPING;
            break;
         case 5:
            var10000 = CLOSE;
      }

      RawAnimation animation = var10000;
      if (controller.getCurrentRawAnimation() != animation) {
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
         case 0:
            animation = NO_WAG;
            break;
         case 1:
            animation = FLYING_WAG;
            break;
         case 2:
            controller.setAnimationSpeed((double)1.5F);
            animation = IDLE_WAG;
            break;
         case 3:
            controller.setAnimationSpeed((double)2.0F);
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
      return (double)this.tickCount;
   }

   protected void jumpFromGround() {
      Vec3 vec3d = this.getDeltaMovement();
      LivingEntity target = this.getTarget();
      double jumpStrength;
      if (target == null) {
         jumpStrength = (double)1.0F;
      } else {
         double yDiff = target.getY() - this.getY();
         yDiff = Math.min(yDiff, (double)10.0F);
         if (yDiff <= (double)0.0F) {
            jumpStrength = (double)1.0F;
         } else {
            jumpStrength = yDiff / (double)3.5F + (double)1.0F;
         }

         jumpStrength = Math.min(jumpStrength, (double)3.0F);
      }

      this.setDeltaMovement(vec3d.x, (double)this.getJumpPower() * jumpStrength, vec3d.z);
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
      boolean bl = target.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
      if (bl) {
         this.playSound(PCSounds.MIMIC_BITE, this.getSoundVolume(), 1.5F + this.getPitchOffset(0.2F));
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

   public void tick() {
      super.tick();
      if (!this.level().isClientSide()) {
         if (this.jumpEndTimer >= 0) {
            --this.jumpEndTimer;
         }

         if (this.spawnWaitTimer > 0) {
            --this.spawnWaitTimer;
         } else if (this.onGround()) {
            if (this.onGroundLastTick) {
               if (this.getMimicState() != 0 && !this.isAttemptingToSleep) {
                  this.timeUntilSleep = 150;
                  this.isAttemptingToSleep = true;
                  this.setMimicState(2);
               }

               if (this.isAttemptingToSleep) {
                  --this.timeUntilSleep;
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

   protected boolean shouldDespawnInPeaceful() {
      return true;
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("wasOnGround", this.onGroundLastTick);
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("Owner")) {
         this.setOwnerUUID((UUID)null);
         this.setTame(false);
      }

      this.onGroundLastTick = compound.getBoolean("wasOnGround");
   }

   protected void defineSynchedData() {
      this.entityData.define(this.getMimicStateVariable(), 0);
      super.defineSynchedData();
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
      } else {
         DimensionType dimensionType = world.dimensionType();
         int i = dimensionType.monsterSpawnBlockLightLimit();
         if (i < 15 && world.getBrightness(LightLayer.BLOCK, pos) > i) {
            return false;
         } else {
            PCConfig config = Dungeons_and_arcanus.loadedConfig;
            int j = world.getLevel().isThundering() ? world.getMaxLocalRawBrightness(pos, 10) : world.getMaxLocalRawBrightness(pos);
            return (float)j <= (float)dimensionType.monsterSpawnLightTest().sample(random) * config.mimicSettings.naturalMimicSpawnRate;
         }
      }
   }

   public static boolean canSpawn(EntityType<PCChestMimic> pcChestMimicEntityType, ServerLevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random) {
      return isSpawnDark(serverWorldAccess, blockPos, random);
   }

   static {
      IDLE = RawAnimation.begin().then("idle", LoopType.LOOP);
      JUMP = RawAnimation.begin().then("jump", LoopType.PLAY_ONCE).then("flying", LoopType.LOOP);
      CLOSE = RawAnimation.begin().then("land", LoopType.PLAY_ONCE).then("idle", LoopType.LOOP);
      SLEEPING = RawAnimation.begin().then("sleeping", LoopType.LOOP);
      FLYING = RawAnimation.begin().then("flying", LoopType.LOOP);
      LOW_WAG = RawAnimation.begin().then("lowWag", LoopType.LOOP);
      FLYING_WAG = RawAnimation.begin().then("flyingWag", LoopType.LOOP);
      IDLE_WAG = RawAnimation.begin().then("idleWag", LoopType.LOOP);
      NO_WAG = RawAnimation.begin().then("noWag", LoopType.LOOP);
      moveSpeed = (double)1.5F;
      maxHealth = 50;
      maxDamage = 5;
   }
}
