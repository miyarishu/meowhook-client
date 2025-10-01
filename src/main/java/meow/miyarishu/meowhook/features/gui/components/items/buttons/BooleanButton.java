package meow.miyarishu.meowhook.features.gui.components.items.buttons;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.util.RenderUtil;
import meow.miyarishu.meowhook.features.gui.MeowHookGui;
import meow.miyarishu.meowhook.features.modules.client.ClickGui;
import meow.miyarishu.meowhook.features.modules.client.Colors;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class BooleanButton
        extends Button {
    private final Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(Colors.class).hoverAlpha.getValue()) : MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        MeowHook.textManager.drawStringWithShadow((ClickGui.getInstance().lowerCase.getValue() ? this.getName().toLowerCase() : this.getName()), this.x + 2.3f, this.y - 1.7f - (float) MeowHookGui.getClickGui().getTextOffset(), -1);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (ClickGui.getInstance().moduleButtonSound.getValue()) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
        }
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public void toggle() {
        this.setting.setValue((Boolean) this.setting.getValue() == false);
    }

    @Override
    public boolean getState() {
        return (Boolean) this.setting.getValue();
    }
}

