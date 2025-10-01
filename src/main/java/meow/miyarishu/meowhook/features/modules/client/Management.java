package meow.miyarishu.meowhook.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;

public class Management
        extends Module {
    public static Management INSTANCE = new Management();
    public Setting<ChatFormatting> mainColor = register(new Setting("Main Color", ChatFormatting.LIGHT_PURPLE));
    public Setting<ChatFormatting> accentColor = register(new Setting("Accent Color", ChatFormatting.DARK_PURPLE));
    public Setting<Integer> trUpdates = register(new Setting("TextRadar Updates", 500, 100, 1000));
    public Management() {
        super("Management", "Manages all sorts of stuff in the client", Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static Management getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Management();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}