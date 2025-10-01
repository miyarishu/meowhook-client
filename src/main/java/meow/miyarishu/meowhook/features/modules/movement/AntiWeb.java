package meow.miyarishu.meowhook.features.modules.movement;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.EntityUtil;
import meow.miyarishu.meowhook.util.Util;

//made by sjnez

public class AntiWeb extends Module {
    private Setting<Boolean> HoleOnly;
    public Setting<Float> timerSpeed = register(new Setting("Speed", 4.0f, 0.1f, 50.0f));

    public float speed = 1.0f;


    public AntiWeb() {
        super("AntiWeb", "Turns on timer when in a web", Category.MOVEMENT, true, false, false);
        this.HoleOnly = (Setting<Boolean>) this.register(new Setting("HoleOnly", true));
    }

    @Override
    public void onEnable() {
        this.speed = timerSpeed.getValue();
    }


    @Override
    public void onUpdate() {

        if (HoleOnly.getValue()) {
            if (Util.mc.player.isInWeb && EntityUtil.isInHole(Util.mc.player)) {
                AntiWeb.mc.timer.tickLength = 50.0f / ((this.timerSpeed.getValue() == 0.0f) ? 0.1f : this.timerSpeed.getValue());
            } else {
                AntiWeb.mc.timer.tickLength = 50.0f;
            }
            if (Util.mc.player.onGround && EntityUtil.isInHole(Util.mc.player)) {
                AntiWeb.mc.timer.tickLength = 50.0f;
            }
        }
        if (!HoleOnly.getValue()) {
            if (Util.mc.player.isInWeb) {
                AntiWeb.mc.timer.tickLength = 50.0f / ((this.timerSpeed.getValue() == 0.0f) ? 0.1f : this.timerSpeed.getValue());

            } else {
                AntiWeb.mc.timer.tickLength = 50.0f;

            }
            if (Util.mc.player.onGround) {
                AntiWeb.mc.timer.tickLength = 50.0f;

            }
        }
    }
}