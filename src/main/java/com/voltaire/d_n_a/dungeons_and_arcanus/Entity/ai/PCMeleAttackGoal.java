package com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tamable_Pet_With_Inv;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class PCMeleeAttackGoal extends Goal {

    protected final PathfinderMob mob;
    private final double speed;
    private final boolean pauseWhenMobIdle;
    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int updateCountdownTicks;
    private int cooldown;
    private final int attackIntervalTicks = 20;
    private long lastUpdateTime;
    private static final long MAX_ATTACK_TIME = 20L;
    protected final Tamable_Pet_With_Inv mimic;

    public PCMeleeAttackGoal(PathfinderMob mob, double speed, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.mimic = (Tamable_Pet_With_Inv) mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.mimic.isPassenger()) {
            return false;
        }

        long time = this.mob.level().getGameTime();
        if (time - this.lastUpdateTime < 20L) {
            return false;
        }
        this.lastUpdateTime = time;

        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        }
        if (this.mimic.getIsAbandoned() && this.mimic instanceof ChestMimicPet) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }
        if (this.mimic.getOwner() == target && this.mimic instanceof ChestMimicPet) {
            return false;
        }

        this.path = this.mob.getNavigation().createPath(target, 0);
        if (this.path != null) {
            return true;
        }
        return this.getSquaredMaxAttackDistance(target)
                >= this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mimic.isPassenger()) {
            return false;
        }

        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }
        if (this.mimic.getIsAbandoned() && this.mimic instanceof ChestMimicPet) {
            return false;
        }
        if (this.mimic.getOwner() == target && this.mimic instanceof ChestMimicPet) {
            return false;
        }
        if (!this.mob.isWithinRestriction(target.blockPosition())) {
            return false;
        }
        return !(target instanceof Player player)
                || (!player.isSpectator() && !player.isCreative());
    }

    @Override
    public void tick() {
        if (this.mimic.isPassenger()) {
            this.mob.getNavigation().stop();
            return;
        }

        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }

        this.mimic.getLookControl().setLookAt(target, 10.0F, 10.0F);

        MimicMoveControl moveControl = this.mimic.getMimicMoveControl();
        if (moveControl != null) {
            moveControl.look(this.mimic.getYRot(), true);
        }

        double distSq = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);

        if ((this.pauseWhenMobIdle || this.mob.getSensing().hasLineOfSight(target))
                && this.updateCountdownTicks <= 0
                && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D
                || target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D
                || this.mob.getRandom().nextFloat() < 0.05F)) {

            this.targetX = target.getX();
            this.targetY = target.getY();
            this.targetZ = target.getZ();
            this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);

            if (distSq > 1024.0D) {
                this.updateCountdownTicks += 10;
            } else if (distSq > 256.0D) {
                this.updateCountdownTicks += 5;
            }

            if (!this.mob.getNavigation().moveTo(target, this.speed)) {
                this.updateCountdownTicks += 15;
            }

            this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
        }

        this.cooldown = Math.max(this.cooldown - 1, 0);
        this.attack(target, distSq);
    }

    @Override
    public void start() {
        this.mob.setAggressive(true);
        this.updateCountdownTicks = 0;
        this.cooldown = 0;
    }

    @Override
    public void stop() {
        LivingEntity target = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.mob.setTarget(null);
        }
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    protected void attack(LivingEntity target, double squaredDistance) {
        double reach = this.getSquaredMaxAttackDistance(target);
        if (squaredDistance <= reach && this.cooldown <= 0) {
            this.resetCooldown();
            this.mob.doHurtTarget(target);
        }
    }

    protected void resetCooldown() {
        this.cooldown = this.adjustedTickDelay(20);
    }

    protected boolean isCooledDown() {
        return this.cooldown <= 0;
    }

    protected int getCooldown() {
        return this.cooldown;
    }

    protected int getMaxCooldown() {
        return this.adjustedTickDelay(20);
    }

    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + entity.getBbWidth();
    }
}
