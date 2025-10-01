package meow.miyarishu.meowhook.features.modules.combat;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.mixin.mixins.accessors.ICPacketPlayer;
import meow.miyarishu.meowhook.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;


public class ShulkerNuker
        extends Module {

    private final Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 0, 8));
    private final Setting<Boolean> autoSwap = this.register(new Setting<Boolean>("AutoSwap", true));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> disableInContainer = this.register(new Setting<Boolean>("Disable In Container", true));

    private float yaw, pitch;

    public ShulkerNuker() { super ("ShulkerNuker", "Anti regear", Category.COMBAT, true , false , false); }

    @Override
    public void onDisable() {
        if (rotate.getValue()) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
        }
    }

    @Override
    public void onUpdate() {

        if (mc.currentScreen instanceof GuiShulkerBox && this.disableInContainer.getValue()) {
            return;
        }

        if (getBlock() != null) {
            int oldSlot = mc.player.inventory.currentItem;

            if (autoSwap.getValue() && InventoryUtil.findItemInHotbar(Items.DIAMOND_PICKAXE) != -1) {
                mc.player.inventory.currentItem = InventoryUtil.findItemInHotbar(Items.DIAMOND_PICKAXE);
            }

            if (rotate.getValue()) {

                Vec3d vec = new Vec3d(getBlock().getPos().getX() + .5,
                        getBlock().getPos().getY() - 1,
                        getBlock().getPos().getZ() + .5);

                float[] rotations = MeowHook.rotationManager.getAngle(vec);

                yaw = rotations[0];
                pitch = rotations[1];
            }

            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, getBlock().getPos(), EnumFacing.SOUTH));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, getBlock().getPos(), EnumFacing.SOUTH));

            mc.player.inventory.currentItem = oldSlot;
        }

        if (rotate.getValue() && getBlock() == null) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (rotate.getValue()) {
            Packet packet = event.getPacket();

            if (packet instanceof CPacketPlayer) {
                ((ICPacketPlayer) packet).setYaw(yaw);
                ((ICPacketPlayer) packet).setPitch(pitch);
            }
        }
    }

    private TileEntity getBlock() {
        TileEntity out = null;

        for (TileEntity entity : mc.world.loadedTileEntityList) {

            if (entity instanceof TileEntityShulkerBox) {

                if (entity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= (range.getValue() * range.getValue())) {
                    out = entity;
                }
            }
        }
        return out;
    }
}