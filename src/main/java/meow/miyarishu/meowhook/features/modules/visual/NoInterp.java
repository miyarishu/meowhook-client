package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;

public class NoInterp
        extends Module {
    public NoInterp() {
        super("NoInterp", "Disables limb swinging", Category.VISUAL, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) { return; }
        for (EntityPlayer p : NoInterp.mc.world.playerEntities) {
            if (p != null) {
                p.limbSwing = 0;
                p.limbSwingAmount = 0;
                p.prevLimbSwingAmount = 0;
            }
        }
    }
}

