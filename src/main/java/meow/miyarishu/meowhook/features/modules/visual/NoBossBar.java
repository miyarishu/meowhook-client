package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;

public class NoBossBar
        extends Module {
    private static NoBossBar INSTANCE = new NoBossBar( );
    public NoBossBar() {
        super("NoBossBar", "Disables boss bar", Category.VISUAL, true, false, false);
        this.setInstance ( );
    }

    public static NoBossBar getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new NoBossBar();
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }
}

