package meow.miyarishu.meowhook.features.modules.client;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.client.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;

public class Background extends Module
{
    public static Background INSTANCE = new Background();

    public Setting<Boolean> clean = register(new Setting<Boolean>("Clean", true));

    public Background() {
        super("Background", "Modifies the background", Category.CLIENT, true, false, false);
        setInstance();
    }

    public static Background getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Background();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}