package meow.miyarishu.meowhook.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;

public class Media
        extends Module {
    public static Media INSTANCE = new Media();

    public final Setting<String> NameString = register(new Setting<Object>("Name", "New Name Here..."));
    public Media() {
        super("Media", "No Alt fInding", Category.CLIENT, true, false, false);
        this.setInstance();
    }

    @Override
    public void onEnable() {
        Command.sendMessage("Name changed to " + NameString.getValue());
    }

    public static Media getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Media();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}