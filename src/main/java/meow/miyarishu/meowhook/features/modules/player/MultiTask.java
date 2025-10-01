package meow.miyarishu.meowhook.features.modules.player;

import meow.miyarishu.meowhook.features.modules.Module;


public class MultiTask
        extends Module {

    private static MultiTask INSTANCE = new MultiTask();

    public MultiTask() {
        super("MultiTask", "yeah", Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static MultiTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MultiTask();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}