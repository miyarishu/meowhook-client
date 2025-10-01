package meow.miyarishu.meowhook.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.event.events.MoveEvent;
import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.event.events.PushEvent;
import meow.miyarishu.meowhook.event.events.UpdateWalkingPlayerEvent;
import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.EntityUtil;
import meow.miyarishu.meowhook.util.MathUtil;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacketFly
        extends Module {

    public static PacketFly INSTANCE;

    private final Setting<Boolean> debug = this.register(new Setting<>("Debug", false));
    private final Setting<modes> mode = this.register(new Setting<>("Mode", modes.FAST));
    private final Setting<Integer> factorAmount = this.register(new Setting<>("Factor", 3, 1, 15));
    private final Setting<types> type = this.register(new Setting<>("Type", types.DIFFER));
    private final Setting<Boolean> limit = this.register(new Setting<>("Limit", true));
    private final Setting<Boolean> frequency = this.register(new Setting<>("Frequency", true));
    private final Setting<Boolean> fluctuate = this.register(new Setting<>("Fluctuate", false));
    private final Setting<Integer> frequencyAmount = this.register(new Setting<>("Frequency Amount", 10, 1, 10, v -> frequency.getValue()));
    private final Setting<Integer> limitAmount = this.register(new Setting<>("Limit Amount", 10, 1, 10, v -> limit.getValue() && !fluctuate.getValue()));
    private final Setting<Boolean> factorize = this.register(new Setting<>("Factorize", true));
    private final Setting<Integer> fluctuateMin = this.register(new Setting<>("Factor Min", 1, 1, 15, v -> factorize.getValue()));
    private final Setting<Integer> fluctuateMax = this.register(new Setting<>("Factor Max", 4, 1, 15, v -> factorize.getValue()));
    private final Setting<Float> additional = this.register(new Setting<>("Inheritance", 0.0f, -0.5f, 1f));
    private final List<CPacketPlayer> packets = new ArrayList<>();
    private int teleportID = 0;
    private int clock = 0;
    private int flightCounter = 0;
    private float factorAmt = 0;
    private float limitAmt = 0;
    private int discrim = 0;
    private boolean countingUp = true;

    public PacketFly() {
        super("PacketFly", "Uses packets to allow you to phase and fly", Module.Category.MOVEMENT, true, false, false);
    }

    public static PacketFly getInstance() {
        if (INSTANCE == null) INSTANCE = new PacketFly();
        return INSTANCE;
    }

    @Override
    public String getDisplayInfo() {

        if (this.mode.getValue() == modes.FACTOR) {
            return this.mode.getValue() + ", F: " + this.factorAmt + ", " + (limit.getValue() ? " L: " + limitAmt + ", " : "") + discrim + ", " + (checkHitBoxes() ? ChatFormatting.GREEN + "Phase" : ChatFormatting.RED + "Non-Phase");
        }

        return this.mode.getValue() + (limit.getValue() ? ", L: " + limitAmt + ", " : ", ") + discrim + ", " + (checkHitBoxes() ? ChatFormatting.GREEN + " Phase" : ChatFormatting.DARK_RED + " Non-Phase");

    }

    private double[] getBounds(double motionX, double motionY, double motionZ) {
        switch (type.getValue()) {
            case UP:
                return new double[]{Util.mc.player.posX + motionX, Util.mc.player.posY + motionY + 1337.69, Util.mc.player.posZ + motionZ};
            case DOWN:
                return new double[]{Util.mc.player.posX + motionX, Util.mc.player.posY + motionY - 42069, Util.mc.player.posZ + motionZ};
            case PRESERVE:
                return new double[]{Util.mc.player.posX + motionX + new Random().nextInt((int) (3.0E7 + 3.0E7)) - 3.0E7, Util.mc.player.posY + motionY, Util.mc.player.posZ + motionZ + new Random().nextInt((int) (3.0E7 + 3.0E7)) - 3.0E7};
            case LIMITJITTER:
                return new double[]{Util.mc.player.posX + motionX + new Random().nextInt(50 + 50) - 50, Util.mc.player.posY + motionY + new Random().nextInt(420 + 420) - 420, Util.mc.player.posZ + motionZ + new Random().nextInt(50 + 50) - 50};
            case DIFFER:
                switch (discrim) {
                    case 0:
                        return new double[]{Util.mc.player.posX + motionX, Util.mc.player.posY + motionY - 256, Util.mc.player.posZ + motionZ};
                    case 1:
                        return new double[]{Util.mc.player.posX + motionX, Util.mc.player.posY + motionY + 256, Util.mc.player.posZ + motionZ};
                    case 2:
                        return new double[]{Util.mc.player.posX + motionX + 256, Util.mc.player.posY + motionY, Util.mc.player.posZ + motionZ - 256};
                    case 3:
                        return new double[]{Util.mc.player.posX + motionX, Util.mc.player.posY + motionY - 42069, Util.mc.player.posZ + motionZ};
                }
            case BOUNDED:
                return new double[]{Util.mc.player.posX + motionX, 256, Util.mc.player.posZ + motionZ};
            case SPECIAL:
                return new double[]{-8043809, 1337.69, -203912};
            case CONCEAL:
                return new double[]{Util.mc.player.posX + motionX + new Random().nextInt(1000000 + 1000000) - 1000000, Util.mc.player.posY + motionY + 256, Util.mc.player.posZ + motionZ + new Random().nextInt(1000000 + 1000000) - 1000000};
        }
        return new double[]{Util.mc.player.posX + motionX, Util.mc.player.posY + motionY + 1337.69, Util.mc.player.posZ + motionZ};
    }

    private float getFactorAmount() {
        if (mode.getValue() == modes.FACTOR) {
            if (factorize.getValue()) {
                return new Random().nextInt(fluctuateMax.getValue() - fluctuateMin.getValue() + 1) + fluctuateMin.getValue();
            }
            return factorAmount.getValue();
        }

        return 1f;
    }

    private float getLimitAmount() {
        if (fluctuate.getValue()) {
            return new Random().nextInt(10 - 1 + 1) + 1;
        }
        return limitAmount.getValue();
    }

    @Override
    public void onDisable() {
        Util.mc.player.noClip = false;
        Util.mc.player.collidedHorizontally = false;
        Util.mc.player.collidedVertically = false;

        double[] bounds = getBounds(0, 0, 0);
        final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0],
                bounds[1], bounds[2], Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);
        packets.add(outOfBoundsPosition);
        Util.mc.player.connection.sendPacket(outOfBoundsPosition);
    }

    @Override
    public void onEnable() {
        if (Util.mc.player != null) {
            teleportID = 0;
            clock = 0;
            factorAmt = 0;
            limitAmt = 0;

            double[] bounds = getBounds(0, 0, 0);
            final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0],
                    bounds[1], bounds[2], Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);
            packets.add(outOfBoundsPosition);
            Util.mc.player.connection.sendPacket(outOfBoundsPosition);
        }
    }

    private boolean checkHitBoxes() {
        return !Util.mc.world.getCollisionBoxes(Util.mc.player, Util.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        event.setCanceled(true);
    }

    private boolean resetCounter(int counter) {
        if (++this.flightCounter >= counter) {

            if (debug.getValue()) Command.sendMessage(counter + " Exceeded");

            this.flightCounter = 0;
            return true;
        }

        return false;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }

        if (Util.mc.player == null || Util.mc.world == null) {
            this.disable();
            return;
        }

        if (Util.mc.currentScreen instanceof GuiDownloadTerrain) {
            Util.mc.currentScreen = null;
            return;
        }

        Util.mc.player.noClip = false;

        Util.mc.player.motionX = 0;
        Util.mc.player.motionY = 0;
        Util.mc.player.motionZ = 0;

        Util.mc.player.setVelocity(0, 0, 0);

        //Prepare movement

        double[] strafing;
        double factorizes;

        if (checkHitBoxes()) {
            if (Util.mc.player.movementInput.jump || Util.mc.player.movementInput.sneak) {
                factorizes = 0.032;
            } else {
                factorizes = 0.062 + (additional.getValue() / 10);
            }
        } else {
            if (Util.mc.player.movementInput.jump || Util.mc.player.movementInput.sneak) {
                factorizes = this.resetCounter(20) ? -0.001 : 0.032;
            } else {
                factorizes = this.resetCounter(20) ? 0.016 : 0.031 + (additional.getValue() / 10);
            }
        }

        strafing = MathUtil.directionSpeed(factorizes);

        if (checkHitBoxes()) {
            Util.mc.player.noClip = true;
        }

        float speed = (float) ((!Util.mc.player.movementInput.jump || (!checkHitBoxes() && EntityUtil.isMoving())) ? (Util.mc.player.movementInput.sneak ? -0.062 :
                (checkHitBoxes() ? 0.0 : (!this.resetCounter(4) ? 0.0 : -0.01))) : (checkHitBoxes() ? 0.062 : (this.resetCounter(20) ? -0.032 : 0.062)));

        if (speed == 0 && strafing[0] == 0 && strafing[1] == 0) return;

        factorAmt = getFactorAmount();
        limitAmt = getLimitAmount();

        if (discrim >= 3) {
            countingUp = false;
        } else if (discrim <= 0) {
            countingUp = true;
        }

        if (countingUp) discrim++;
        else discrim--;

        this.clock = 0;

        int multiplier = 1;

        while (multiplier <= (mode.getValue() == modes.FACTOR ? factorAmt : 1)) {

            double motionX = strafing[0] * multiplier;
            double motionY = speed * multiplier;
            double motionZ = strafing[1] * multiplier;

            if (limit.getValue()) {
                if (clock++ > limitAmt) {

                    final CPacketPlayer.PositionRotation newPosition =
                            new CPacketPlayer.PositionRotation(
                                    Util.mc.player.posX + motionX,
                                    Util.mc.player.posY + motionY,
                                    Util.mc.player.posZ + motionZ, Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);
                    packets.add(newPosition);
                    Util.mc.player.connection.sendPacket(newPosition);

                    if (frequency.getValue()) {
                        for (int f = 0; f < frequencyAmount.getValue(); f++) {
                            double[] bounds = getBounds(motionX, motionY, motionZ);

                            final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0],
                                    bounds[1], bounds[2], Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);

                            packets.add(outOfBoundsPosition);
                            Util.mc.player.connection.sendPacket(outOfBoundsPosition);
                        }
                    } else {
                        double[] bounds = getBounds(motionX, motionY, motionZ);

                        final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0],
                                bounds[1], bounds[2], Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);

                        packets.add(outOfBoundsPosition);
                        Util.mc.player.connection.sendPacket(outOfBoundsPosition);
                    }

                    multiplier++;
                }
            } else {

                final CPacketPlayer.PositionRotation newPosition =
                        new CPacketPlayer.PositionRotation(
                                Util.mc.player.posX + motionX,
                                Util.mc.player.posY + motionY,
                                Util.mc.player.posZ + motionZ, Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);

                packets.add(newPosition);
                Util.mc.player.connection.sendPacket(newPosition);

                if (frequency.getValue()) {
                    for (int f = 0; f < frequencyAmount.getValue(); f++) {
                        double[] bounds = getBounds(motionX, motionY, motionZ);

                        final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0],
                                bounds[1], bounds[2], Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);

                        packets.add(outOfBoundsPosition);
                        Util.mc.player.connection.sendPacket(outOfBoundsPosition);
                    }
                } else {
                    double[] bounds = getBounds(motionX, motionY, motionZ);

                    final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0],
                            bounds[1], bounds[2], Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, Util.mc.player.onGround);

                    packets.add(outOfBoundsPosition);
                    Util.mc.player.connection.sendPacket(outOfBoundsPosition);
                }

                multiplier++;
            }

        }

    }

    @SubscribeEvent
    public void onPushOutOfBlocks(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.Rotation) {
            event.setCanceled(true);
        }

        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packetPlayer = event.getPacket();

            if (packets.contains(packetPlayer)) {
                if (debug.getValue())
                    Command.sendMessage("Sending: " + packetPlayer.x + " " + packetPlayer.y + " " + packetPlayer.z);

                packets.remove(packetPlayer);
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (Util.mc.player == null || Util.mc.world == null) return;

        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook pak = event.getPacket();
            if (debug.getValue())
                Command.sendMessage("LagBack Detected: " + pak.getX() + " " + pak.getY() + " " + pak.getZ() + " L" + limitAmt + " F" + factorAmt + " D" + discrim);
            this.teleportID = pak.getTeleportId();
            //mc.player.setPosition(pak.getX(), pak.getY(), pak.getZ());

            Util.mc.player.setPosition(pak.getX(), pak.getY(), pak.getZ());
            Util.mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportID++));

            event.setCanceled(true);
        }

        if (event.getPacket() instanceof SPacketEntityVelocity) {
            //Stop all velocity
            SPacketEntityVelocity packet = event.getPacket();
            if (packet.entityID == Util.mc.player.entityId) {
                packet.motionX = 0;
                packet.motionY = 0;
                packet.motionZ = 0;
            }
        }
    }

    private enum modes {
        FAST, FACTOR
    }

    private enum types {
        UP, DOWN, PRESERVE, LIMITJITTER, DIFFER, BOUNDED, SPECIAL, CONCEAL
    }

}

