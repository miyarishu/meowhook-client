package meow.miyarishu.meowhook.features.modules.movement;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.features.Feature;
import meow.miyarishu.meowhook.util.Util;

public class ReverseStep
        extends Module {
    private static ReverseStep INSTANCE = new ReverseStep();
    private final Setting<Boolean> twoBlocks = this.register(new Setting<Boolean>("2Blocks", Boolean.FALSE));

    public ReverseStep() {
        super("ReverseStep", "ReverseStep.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static ReverseStep getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReverseStep();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public
    void onUpdate ( ) {
        if ( Feature.fullNullCheck ( ) ) {
            return;
        }
        if ( Util.mc.player != null && Util.mc.world != null && Util.mc.player.onGround && ! Util.mc.player.isSneaking ( ) && ! Util.mc.player.isInWater ( ) && ! Util.mc.player.isDead && ! Util.mc.player.isInLava ( ) && ! Util.mc.player.isOnLadder ( ) && ! Util.mc.player.noClip && ! Util.mc.gameSettings.keyBindSneak.isKeyDown ( ) && ! Util.mc.gameSettings.keyBindJump.isKeyDown ( ) ) {
            if ( ReverseStep.mc.player.onGround ) {
                ReverseStep.mc.player.motionY -= 1.0;
            }
        }
    }
}

