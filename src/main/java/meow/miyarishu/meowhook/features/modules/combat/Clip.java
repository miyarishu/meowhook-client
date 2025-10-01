package meow.miyarishu.meowhook.features.modules.combat;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketPlayer;

public class Clip
        extends Module {

    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 5, 1, 30));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("Disable", true));
    private final Setting<Integer> updates = this.register(new Setting<Integer>("Updates", 10, 1, 30));
    private final Setting<ClipMode> mode = register(new Setting("Mode", ClipMode.Corner));

    public BlockPos pos;
    int disableTime = 0;


    public Clip() {super ("Clip", "Clips you into a corner", Category.COMBAT, true , false , false); }

    @Override
    public void onUpdate() {
        if (mc.world == null || mc.player == null) {
            return;
        }
        if (!MovementUtil.noMovementKeys()) {
            disable();
            return;
        }
        switch (mode.getValue()) {

            case AutoCenter:
                try
                {
                    Vec3d setCenter = new Vec3d(pos.getX() + 0.5, mc.player.posY, pos.getZ() + 0.5);

                    mc.player.motionX = 0;
                    mc.player.motionZ = 0;

                    mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(setCenter.x, setCenter.y, setCenter.z, true));
                    mc.player.setPosition(setCenter.x, setCenter.y, setCenter.z);
                } catch (Exception e2) {
                    System.out.println(2);
                }
                break;

            case Corner:
                if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().grow(0.01, 0, 0.01)).size() < 2) {
                    mc.player.setPosition(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.301, Math.floor(mc.player.posX) + 0.699), mc.player.posY, roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.301, Math.floor(mc.player.posZ) + 0.699));

                } else if (mc.player.ticksExisted % delay.getValue() == 0) {
                    mc.player.setPosition(mc.player.posX + MathHelper.clamp(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.241, Math.floor(mc.player.posX) + 0.759) - mc.player.posX, -0.03, 0.03), mc.player.posY, mc.player.posZ + MathHelper.clamp(roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.241, Math.floor(mc.player.posZ) + 0.759) - mc.player.posZ, -0.03, 0.03));
                    mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(roundToClosest(mc.player.posX, Math.floor(mc.player.posX) + 0.23, Math.floor(mc.player.posX) + 0.77), mc.player.posY, roundToClosest(mc.player.posZ, Math.floor(mc.player.posZ) + 0.23, Math.floor(mc.player.posZ) + 0.77), true));
                }
                break;

            case FiveB:
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 0.0042123,mc.player.posZ,mc.player.onGround));
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 0.02141,mc.player.posZ,mc.player.onGround));
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX,mc.player.posY - 0.097421,mc.player.posZ,500,500,mc.player.onGround));
                // https://github.com/WMSGaming/Abstract-1.12.2/blob/master/src/main/java/com/wms/abstractclient/module/modules/exploit/CornerClip.java
                break;
        }
        disableTime++;
        if (disable.getValue()) {
            if (disableTime >= updates.getValue()) {
                disable();
            }
        }
    }

    public static double roundToClosest(double num, double low, double high) {
        double d2 = high - num;
        double d1 = num - low;
        return (d2 > d1 ? low : high);
    }

    @Override
    public void onDisable()
    {
        disableTime = 0;
    }

    public enum ClipMode{
        Corner,
        FiveB,
        AutoCenter
    }
}