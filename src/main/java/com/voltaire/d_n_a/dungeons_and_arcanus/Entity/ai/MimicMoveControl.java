package com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ai;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tamable_Pet_With_Inv;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class MimicMoveControl extends MoveControl {

    private final Tamable_Pet_With_Inv mimic;
    private float targetYaw;
    private int ticksUntilJump;
    private boolean jumpOften;

    public MimicMoveControl(Tamable_Pet_With_Inv mimic) {
        super(mimic);
        this.mimic = mimic;
        this.targetYaw = 180.0F * mimic.getYRot() / (float) Math.PI;
    }

    public void look(float targetYaw, boolean jumpOften) {
        this.targetYaw = targetYaw;
        this.jumpOften = jumpOften;
    }

    public void move(double speed) {
        this.speedModifier = speed;
        this.operation = Operation.MOVE_TO;
    }

    @Override
    public void tick() {
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.targetYaw, 90.0F));
        this.mob.yRotO = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();

        if (this.operation != Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
        } else {
            this.operation = Operation.WAIT;

            if (this.mob.onGround()) {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

                if (this.ticksUntilJump-- <= 0) {
                    this.ticksUntilJump = this.mimic.getTicksUntilNextJump();
                    if (this.jumpOften) {
                        this.ticksUntilJump /= 3;
                    }

                    this.mimic.getJumpControl().jump();
                    this.mimic.playSound(this.mimic.getJumpSound(), this.mimic.getSoundVolume(), this.mimic.getJumpSoundPitch());
                } else {
                    this.mimic.xxa = 0.0F;
                    this.mimic.zza = 0.0F;
                    this.mob.setSpeed(0.0F);
                }
            } else {
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }
    }
}
