package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.event.events.Render3DEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class NewChunks extends Module {

    private final List<Vec2f> chunkDataList;

    public NewChunks(){
        super("NewChunks", "Shows you chunks that haven't yet been generated", Category.VISUAL, true, false, false);

        this.chunkDataList = new ArrayList<Vec2f>();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChunkData) {
            final SPacketChunkData packet2 = (SPacketChunkData) event.getPacket();
            if (!packet2.isFullChunk()) {
                final Vec2f chunk = new Vec2f((float)(packet2.getChunkX() * 16), (float)(packet2.getChunkZ() * 16));
                if (!this.chunkDataList.contains(chunk)) {
                    this.chunkDataList.add(chunk);
                }
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        try {
            final List<Vec2f> found = new ArrayList<Vec2f>();
            for (final Vec2f chunkData : this.chunkDataList) {
                if (chunkData != null) {
                    if (NewChunks.mc.player.getDistance((double) chunkData.x, NewChunks.mc.player.posY, (double) chunkData.y) > 120) {
                        found.add(chunkData);
                    }

                    final double renderPosX = chunkData.x - NewChunks.mc.getRenderManager().viewerPosX;
                    final double renderPosY = -NewChunks.mc.getRenderManager().viewerPosY;
                    final double renderPosZ = chunkData.y - NewChunks.mc.getRenderManager().viewerPosZ;
                    final AxisAlignedBB bb = new AxisAlignedBB(renderPosX, renderPosY, renderPosZ, renderPosX + 16.0, renderPosY + 1.0, renderPosZ + 16.0);
                    RenderBlock(bb, 0xFFFFFFFF, 1.2);
                }
            }
            this.chunkDataList.removeAll(found);
        }catch(Exception e){

        }
    }

    public static void OpenGl() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GL11.glHint(3154, 4354);
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GL11.glEnable(2848);
        GL11.glEnable(34383);
    }

    public static void ReleaseGl() {
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GL11.glHint(3154, 4352);
        GL11.glDisable(2848);
        GL11.glDisable(34383);
        GlStateManager.shadeModel(7424);
    }

    public static void RenderBlock(final AxisAlignedBB bb, final int c, final Double width) {
        OpenGl();
        GlStateManager.glLineWidth((float)(1.5 * (width + 1.0E-4)));
        final float a = (c >> 24 & 0xFF) / 255.0f;
        final float r = (c >> 16 & 0xFF) / 255.0f;
        final float g = (c >> 8 & 0xFF) / 255.0f;
        final float b = (c >> 0 & 0xFF) / 255.0f;;
        RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ, r, g, b, a);
        ReleaseGl();
    }

}
