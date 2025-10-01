package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.event.events.Render3DEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.EntityUtil;
import meow.miyarishu.meowhook.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CityESP
        extends Module {

    public CityESP() {
        super("CityESP", "deezmaster move u r getting citied", Category.VISUAL, true, false, false);
    }

    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));

    private ICamera camera = new Frustum();
    ArrayList<BlockPos> positions = new ArrayList<BlockPos>();

    private static final BlockPos[] surroundOffset =
            {
                    new BlockPos(0, 0, -1), // north
                    new BlockPos(1, 0, 0), // east
                    new BlockPos(0, 0, 1), // south
                    new BlockPos(-1, 0, 0) // west
            };

    public ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>> GetPlayersReadyToBeCitied()
    {
        ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>> players = new ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>>();

        for (Entity entity : mc.world.playerEntities.stream().filter(entityPlayer -> !MeowHook.friendManager.isFriend(entityPlayer.getName())).collect(Collectors.toList()))
        {
            if(EntityUtil.isBurrow(entity)) continue;
            positions = new ArrayList<BlockPos>();

            for (int i = 0; i < 4; ++i)
            {
                BlockPos o = EntityUtil.GetPositionVectorBlockPos(entity, surroundOffset[i]);

                // ignore if the surrounding block is not obsidian
                if (mc.world.getBlockState(o).getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(o).getBlock() != Blocks.ENDER_CHEST)
                    continue;

                boolean passCheck = false;

                switch (i)
                {
                    case 0:
                        passCheck = canPlaceCrystal(o.north(1).down());
                        break;
                    case 1:
                        passCheck = canPlaceCrystal(o.east(1).down());
                        break;
                    case 2:
                        passCheck = canPlaceCrystal(o.south(1).down());
                        break;
                    case 3:
                        passCheck = canPlaceCrystal(o.west(1).down());
                        break;
                }

                if (passCheck)
                    positions.add(o);
            }

            if (!positions.isEmpty())
                players.add(new Pair<EntityPlayer, ArrayList<BlockPos>>((EntityPlayer)entity, positions));
        }

        return players;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (mc.getRenderManager() == null || mc.getRenderManager().options == null)
            return;

        GetPlayersReadyToBeCitied().forEach(pair ->
        {
            pair.getValue().forEach(o ->
            {
                final AxisAlignedBB bb = new AxisAlignedBB(o.getX() - mc.getRenderManager().viewerPosX, o.getY() - mc.getRenderManager().viewerPosY,
                        o.getZ() - mc.getRenderManager().viewerPosZ, o.getX() + 1 - mc.getRenderManager().viewerPosX, o.getY() + 1 - mc.getRenderManager().viewerPosY,
                        o.getZ() + 1 - mc.getRenderManager().viewerPosZ);

                camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

                if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ,
                        bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ)))
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
                    GL11.glLineWidth(1.5f);

                    drawBox(o, red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());

                    GL11.glDisable(GL11.GL_LINE_SMOOTH);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            });
        });
    }

    private void drawBox(BlockPos blockPos, int r, int g, int b, int a) {
        Color color = new Color(r, g, b, a);

        RenderUtil.drawBoxESP(blockPos, color, false, color, 1.2f, true, true, 120, false);
    }

    public static boolean canPlaceCrystal(final BlockPos pos)
    {
        final Block block = mc.world.getBlockState(pos).getBlock();

        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK)
        {
            final Block floor = mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            final Block ceil = mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();

            if (floor == Blocks.AIR && ceil == Blocks.AIR)
            {
                if (mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty())
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static class Pair<T, S> {
        T key;
        S value;

        public Pair(T key, S value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public S getValue() {
            return value;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public void setValue(S value) {
            this.value = value;
        }

    }

}
