package meow.miyarishu.meowhook.features.gui.components.items.buttons;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.gui.components.items.Item;
import meow.miyarishu.meowhook.util.RenderUtil;
import meow.miyarishu.meowhook.util.Util;
import meow.miyarishu.meowhook.features.gui.MeowHookGui;
import meow.miyarishu.meowhook.features.gui.components.Component;
import meow.miyarishu.meowhook.features.modules.client.ClickGui;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class Button
        extends Item {
    private boolean state;

    public Button(String name) {
        super(name);
        this.height = 10;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(Colors.class).hoverAlpha.getValue()) : MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).buttonAlpha.getValue()) : MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).buttonDisabledAlpha.getValue()) : MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())));
        MeowHook.textManager.drawStringWithShadow((ClickGui.getInstance().lowerCase.getValue() ? this.getName().toLowerCase() : this.getName()), this.x + 2.3f, this.y - 2.0f - (float) MeowHookGui.getClickGui().getTextOffset(), (this.getState() ? -1 : -1));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        if (ClickGui.getInstance().moduleButtonSound.getValue()) {
            Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 9;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : MeowHookGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }
}

