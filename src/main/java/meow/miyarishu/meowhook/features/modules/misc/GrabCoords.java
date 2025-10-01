package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.modules.Module;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class GrabCoords
        extends Module {
    public GrabCoords() {
        super("GrabCoords", "Copies your coords.", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        int posX = (int) mc.player.posX;
        int posY = (int) mc.player.posY;
        int posZ = (int) mc.player.posZ;

        String coords = "X: " + posX + " Y: " + posY + " Z: " + posZ;

        StringSelection stringSelection = new StringSelection(coords);

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        toggle();
    }
}

