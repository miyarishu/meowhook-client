package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraft.client.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class CowDupe
        extends Module {

    private final Setting<Integer> Dura = this.register(new Setting<Integer>("Uses", 150, 1, 238));
    public CowDupe() {
        super("CowDupe", "Dupes Cows.", Category.MISC, true, false, false);
    }



    @Override
    public void onEnable() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.inventory.getCurrentItem().getItem().equals(Items.SHEARS)) {
            for (int i = 0; i < this.Dura.getValue().intValue(); ++i) {
                if (mc.pointedEntity != null) {
                    mc.getConnection().sendPacket((Packet)new CPacketUseEntity(mc.pointedEntity, EnumHand.MAIN_HAND));
                }
            }
            Command.sendMessage("Finished shearing targeted entity.");
            this.disable();
        }
        else {
            Command.sendMessage("You need to hold shears to do the glitch.");
            this.disable();
        }
    }
}