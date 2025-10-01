package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.modules.Module;

public class AutoJewbase
        extends Module {
    public AutoJewbase() {
        super("AutoJewbase", "AUTOJEWBASE.", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        AutoJewbase.mc.player.sendChatMessage("Omg Its johnmcswag Welcome to jewbase 4 We have been basing for a week now and we decided that you could join The coords are -8043809 -203912");
        this.disable();
    }
}