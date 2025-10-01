package meow.miyarishu.meowhook.features.modules.combat;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.util.EntityUtil;
import meow.miyarishu.meowhook.util.Timer;
import meow.miyarishu.meowhook.util.InventoryUtil;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Blocker extends Module {

    private final Setting<Boolean> extend = this.register(new Setting<>("Extend", true));
    private final Setting<Boolean> face = this.register(new Setting<>("Face", true));
    private final Setting<Boolean> diagonal = this.register(new Setting<>("Diagonal", true));
    private final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    private final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", false));

    private final Map<BlockPos, Long> renderBlocks = new ConcurrentHashMap<>();
    private final Timer renderTimer = new Timer();

    public Blocker() {
        super("Blocker", "Attempts to extend your surround when it's being broken.", Category.COMBAT, true, false, true);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.isEnabled()) {
            if (event.getPacket() instanceof SPacketBlockBreakAnim && EntityUtil.isInHole(mc.player)) {
                SPacketBlockBreakAnim packet = event.getPacket();
                BlockPos pos = packet.getPosition();

                if (mc.world.getBlockState(pos).getBlock() == (Blocks.BEDROCK) || mc.world.getBlockState(pos).getBlock() == (Blocks.AIR))
                    return;


                BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                BlockPos placePos = null;
                BlockPos placePos2 = null;
                BlockPos placePos3 = null;

                if (diagonal.getValue()) {
                    if (pos.equals(playerPos.north()))
                        placePos3 = (playerPos.north().west());

                    if (pos.equals(playerPos.west()))
                        placePos3 = (playerPos.north().east());

                    if (pos.equals(playerPos.east()))
                        placePos3 = (playerPos.south().east());

                    if (pos.equals(playerPos.south()))
                        placePos3 = (playerPos.south().west());
                }

                if (extend.getValue()) {
                    if (pos.equals(playerPos.north()))
                        placePos = (playerPos.north().north());

                    if (pos.equals(playerPos.east()))
                        placePos = (playerPos.east().east());

                    if (pos.equals(playerPos.west()))
                        placePos = (playerPos.west().west());

                    if (pos.equals(playerPos.south()))
                        placePos = (playerPos.south().south());
                }

                if (face.getValue()) {
                    if (pos.equals(playerPos.north()))
                        placePos2 = (playerPos.north().add(0, 1, 0));

                    if (pos.equals(playerPos.east()))
                        placePos2 = (playerPos.east().add(0, 1, 0));

                    if (pos.equals(playerPos.west()))
                        placePos2 = (playerPos.west().add(0, 1, 0));

                    if (pos.equals(playerPos.south()))
                        placePos2 = (playerPos.south().add(0, 1, 0));
                }

                if (placePos != null) {
                    placeBlock(placePos);
                }
                if (placePos2 != null) {
                    placeBlock(placePos2);
                }
                if (placePos3 != null) {
                    placeBlock(placePos3);
                }
            }
        }

        if (event.getPacket() instanceof SPacketBlockChange) {
            if (renderBlocks.containsKey(((SPacketBlockChange) event.getPacket()).getBlockPosition())) {
                renderTimer.reset();

                if (((SPacketBlockChange) event.getPacket()).getBlockState().getBlock() != Blocks.AIR && renderTimer.passedMs(400)) {
                    renderBlocks.remove(((SPacketBlockChange) event.getPacket()).getBlockPosition());
                }
            }
        }
    }

    private void placeBlock(BlockPos pos) {
        if (this.isEnabled()) {
            if (!mc.world.isAirBlock(pos)) return;

            int oldSlot = mc.player.inventory.currentItem;

            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);

            if (obbySlot == -1 && eChestSlot == 1) return;

            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityEnderCrystal) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                }
            }

            mc.player.inventory.currentItem = obbySlot == -1 ? eChestSlot : obbySlot;

            mc.playerController.updateController();
            renderBlocks.put(pos, System.currentTimeMillis());

            MeowHook.interactionManager.placeBlock(pos, rotate.getValue(), packet.getValue(), true);

            if (mc.player.inventory.currentItem != oldSlot) {
                mc.player.inventory.currentItem = oldSlot;
                mc.playerController.updateController();
            }

            mc.player.inventory.currentItem = oldSlot;
        }
    }
}