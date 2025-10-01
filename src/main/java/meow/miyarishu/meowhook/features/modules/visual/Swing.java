package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;

public class Swing
        extends Module {
    private static Swing INSTANCE = new Swing( );
    public Setting<SwingMode> swing = this.register(new Setting<Object>("Mode", SwingMode.Mainhand));
    public Swing() {
        super("Swing", "Changes how your arm swings", Category.VISUAL, true, false, false);
        this.setInstance ( );
    }

    public static Swing getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new Swing();
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }

    public static enum SwingMode {
        Mainhand,
        Offhand,
        Packet;

    }
}

