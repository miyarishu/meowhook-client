package meow.miyarishu.meowhook.util;

import meow.miyarishu.meowhook.event.events.MoveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

import java.util.Objects;

public class SpeedUtil implements Util {
    public static double[] instant(Entity entity, MovementInput movementInput, double speed) {
        float moveForward = movementInput.moveForward;
        float moveStrafe = movementInput.moveStrafe;
        float rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }
    public static double getSpeed() {
        return SpeedUtil.getSpeed(false);
    }

    public static double getSpeed(boolean slowness) {
        int amplifier;
        double defaultSpeed = 0.2873;
        if (SpeedUtil.mc.player.isPotionActive(MobEffects.SPEED)) {
            amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (slowness && SpeedUtil.mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();
            defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return defaultSpeed;
    }
    public static boolean anyMovementKeys() {
        return SpeedUtil.mc.player.movementInput.forwardKeyDown || SpeedUtil.mc.player.movementInput.backKeyDown || SpeedUtil.mc.player.movementInput.leftKeyDown || SpeedUtil.mc.player.movementInput.rightKeyDown || SpeedUtil.mc.player.movementInput.jump || SpeedUtil.mc.player.movementInput.sneak;
    }

}

