package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Util;
import meow.miyarishu.meowhook.event.events.PerspectiveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AspectRatio
        extends Module {
    public Setting<Double> aspect = this.register(new Setting<Double>("Stretch", Util.mc.displayWidth / Util.mc.displayHeight +0.0, 0.0, 3.0));

    @Override
    public String getDisplayInfo() {
        return this.aspect.getValue().toString();
    }


    public AspectRatio() {
        super("AspectRatio", "Stretched res like fortnite (p100)", Category.VISUAL, true, false, false);

    }

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent event){
        event.setAspect(aspect.getValue().floatValue());
    }
}