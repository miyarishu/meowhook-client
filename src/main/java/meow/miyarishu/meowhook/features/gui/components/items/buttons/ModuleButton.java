package meow.miyarishu.meowhook.features.gui.components.items.buttons;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.modules.client.ClickGui;
import meow.miyarishu.meowhook.features.gui.MeowHookGui;
import meow.miyarishu.meowhook.features.gui.components.Component;
import meow.miyarishu.meowhook.features.gui.components.items.Item;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Bind;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class ModuleButton
        extends Button {
    private final Module module;
    private List<Item> items = new ArrayList<Item>();
    private boolean subOpen;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
        this.initSettings();
    }


    public void initSettings() {
        ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add(new StringButton(setting));
                }
                if (setting.isNumberSetting() && setting.hasRestriction()) {
                    newItems.add(new Slider(setting));
                    continue;
                }
                if (!setting.isEnumSetting()) continue;
                newItems.add(new EnumButton(setting));
            }
        }
        newItems.add(new BindButton(this.module.getSettingByName("Keybind")));
        this.items = newItems;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (isHovering(mouseX, mouseY) && ClickGui.getInstance().isEnabled() && ClickGui.getInstance().descriptions.getValue()) {
            renderer.drawStringWithShadow(ClickGui.getInstance().lowerCase.getValue() ? module.getDescription().toLowerCase() : module.getDescription(), mouseX + 10, mouseY, -1);
        }

        if (!this.items.isEmpty()) {
            MeowHook.textManager.drawStringWithShadow(this.subOpen ? ClickGui.getInstance().moduleButtonOpen.getValue() : ClickGui.getInstance().moduleButtonClosed.getValue(),this.x + (float) this.width - 2f - this.renderer.getStringWidth(this.subOpen ? ClickGui.getInstance().moduleButtonOpen.getValue() : ClickGui.getInstance().moduleButtonClosed.getValue()), this.y - 2.2f - (float) MeowHookGui.getClickGui().getTextOffset(), -1);
            if (this.subOpen) {
                float height = 1.0f;
                for (Item item : this.items) {
                    meow.miyarishu.meowhook.features.gui.components.Component.counter1[0] = Component.counter1[0] + 1;
                    if (!item.isHidden()) {
                        item.setLocation(this.x + 1.0f, this.y + (height += 10.0f));
                        item.setHeight(10);
                        item.setWidth(this.width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                    }
                    item.update();
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                if (ClickGui.getInstance().moduleButtonSound.getValue()) {
                    Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                }
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    if (item.isHidden()) continue;
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 10;
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                height += item.getHeight() + 1;
            }
            return height + 1;
        }
        return 10;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}

