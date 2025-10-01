package meow.miyarishu.meowhook.features.gui;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.Feature;
import meow.miyarishu.meowhook.features.gui.components.Component;
import meow.miyarishu.meowhook.features.gui.components.items.Item;
import meow.miyarishu.meowhook.features.gui.components.items.buttons.ModuleButton;
import meow.miyarishu.meowhook.features.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MeowHookGui
        extends GuiScreen {
    private static MeowHookGui meowHookGui;
    private static MeowHookGui INSTANCE;

    static {
        INSTANCE = new MeowHookGui();
    }

    private final ArrayList<Component> components = new ArrayList();

    public MeowHookGui() {
        this.setInstance();
        this.load();
    }

    public static MeowHookGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MeowHookGui();
        }
        return INSTANCE;
    }

    public static MeowHookGui getClickGui() {
        return MeowHookGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -115;

        Random random = new Random(); {
        }

        MeowHook.moduleManager.getCategories().stream().sorted(Comparator.comparingInt(a -> a.getName().length())).forEach(category -> {

        });

        for (final Module.Category category : MeowHook.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 119, 4, true) {

                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    MeowHook.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    private MeowHookGui ClickGuiMod;

    public void updateModule(Module module) {
        for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton) item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        // if (HUD.getInstance().drawBackground.getValue()) { this.drawDefaultBackground(); }
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));

        final ScaledResolution res = new ScaledResolution(mc);
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() { return false; }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -3;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
}

