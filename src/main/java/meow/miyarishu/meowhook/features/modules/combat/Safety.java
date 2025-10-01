package meow.miyarishu.meowhook.features.modules.combat;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.util.EntityUtil;
import com.mojang.realmsclient.gui.ChatFormatting;

public class Safety
        extends Module {


    public Safety() {super ("Safety", "SafetyModule", Module.Category.COMBAT, true , false , false); }

    private int isSafe;

    @Override public void onTick() {
        this.checkFeetPos();
    }

    @Override
    public String getDisplayInfo() {
        switch (this.isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
        }
        return ChatFormatting.GREEN + "Safe";
    }

    private void checkFeetPos() {
        if (!EntityUtil.isSafe(Surround.mc.player, 0, true)) {
            this.isSafe = 0;
        } else if (!EntityUtil.isSafe(Surround.mc.player, -1, false)) {
            this.isSafe = 1;
        } else {
            this.isSafe = 2;
        }
    }
}