package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;

public class SlowHand
        extends Module {
    private static SlowHand INSTANCE = new SlowHand( );
    public Setting<Integer> speed = register(new Setting("Speed", 30, 10, 60));
    public SlowHand() {
        super("SlowHand", "Makes your swinging move slower", Category.VISUAL, true, false, false);
        this.setInstance ( );
    }

    public static SlowHand getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new SlowHand();
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }
}

