package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.modules.Module;

public class NoBlockRender
        extends Module {
    private static NoBlockRender INSTANCE = new NoBlockRender( );
    public NoBlockRender() {
        super("NoBlockRender", "Disables goofy ahh block screen render", Category.VISUAL, true, false, false);
        this.setInstance ( );
    }

    public static NoBlockRender getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new NoBlockRender();
        }
        return INSTANCE;
    }

    private void setInstance ( ) {
        INSTANCE = this;
    }
}

