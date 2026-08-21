package com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MimicEscapeDangerGoal extends Goal {

    public static final int WATER_CHECK_RANGE = 1;

    protected final PathfinderMob mob;
    protected final double speed;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected boolean active;

    public MimicEscapeDangerGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.isInDanger()) {
            return false;
        }

        if (this.mob.isOnFire()) {
            BlockPos waterPos = this.locateClosestWater(this.mob.level(), this.mob, 5);
            if (waterPos != null) {
                this.targetX = waterPos.getX();
                this.targetY = waterPos.getY();
                this.targetZ = waterPos.getZ();
                return true;
            }
        }

        return this.findTarget();
    }

    protected boolean isInDanger() {
        return this.mob.getLastHurtByMob() != null
                || this.mob.isFreezing()
                || this.mob.isOnFire();
    }

    protected boolean findTarget() {
        Vec3 pos = DefaultRandomPos.getPos(this.mob, 5, 4);
        if (pos == null) {
            return false;
        }
        this.targetX = pos.x;
        this.targetY = pos.y;
        this.targetZ = pos.z;
        return true;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speed);
        this.active = true;
    }

    @Override
    public void stop() {
        this.active = false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Nullable
    protected BlockPos locateClosestWater(BlockGetter level, Entity entity, int range) {
        BlockPos blockPos = entity.blockPosition();
        if (!level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty()) {
            return null;
        }
        return BlockPos.findClosestMatch(entity.blockPosition(), range, 1,
                pos -> level.getFluidState(pos).is(FluidTags.WATER)).orElse(null);
    }
}
