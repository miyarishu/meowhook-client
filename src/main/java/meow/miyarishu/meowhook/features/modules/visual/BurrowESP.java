package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.modules.client.Colors;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.ColorUtil;
import meow.miyarishu.meowhook.util.RenderUtil;
import meow.miyarishu.meowhook.util.RenderUtill;
import meow.miyarishu.meowhook.util.Util;
import meow.miyarishu.meowhook.event.events.Render3DEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BurrowESP extends Module {

    private static BurrowESP INSTANCE = new BurrowESP();

    public Setting<Integer> range = register(new Setting("Range", 20, 5, 50));
    public Setting<Boolean> self = register(new Setting("Self", true));
    public Setting<Boolean> rainbow = register(new Setting("Rainbow", false));
    public Setting<Integer> red = register(new Setting("Red", 0, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> green = register(new Setting("Green", 255, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> blue = register(new Setting("Blue", 0, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> alpha = register(new Setting("Alpha", 0, 0, 255));
    public Setting<Integer> outlineAlpha = register(new Setting("OL-Alpha", 0, 0, 255));

    private final List<BlockPos> posList = new ArrayList<>();

    private RenderUtill renderUtill = new RenderUtill();

    public BurrowESP(){
        super("BurrowESP", "BURROWESP", Category.VISUAL, true, false, false);
        this.setInstance();
    }

    public static BurrowESP getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new BurrowESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onTick(){
        posList.clear();
        for (EntityPlayer player : Util.mc.world.playerEntities){
            BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY+0.2), Math.floor(player.posZ));
            if((Util.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || Util.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && blockPos.distanceSq(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ) <= this.range.getValue()){

                if (!(blockPos.distanceSq(Util.mc.player.posX, Util.mc.player.posY, Util.mc.player.posZ) <= 1.5) || this.self.getValue()) {
                    posList.add(blockPos);
                }


            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event){
        for (BlockPos blockPos : posList){
            RenderUtil.drawBoxESP(blockPos, rainbow.getValue() ? ColorUtil.rainbow(Colors.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()), 1.5F, true, true, alpha.getValue());
        }
    }
}