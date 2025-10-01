package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;

public class NoSway
        extends Module {
    private static NoSway INSTANCE = new NoSway( );
    public NoSway() {
        super("NoItemSway", "Disables arm rotations", Category.VISUAL, true, false, false);
        this.setInstance ( );
    }

    public static NoSway getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new NoSway();
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }
}

