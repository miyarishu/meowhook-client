package meow.miyarishu.meowhook.features.gui.components.items.buttons;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.util.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.features.gui.MeowHookGui;
import meow.miyarishu.meowhook.features.gui.components.Component;
import meow.miyarishu.meowhook.features.modules.client.ClickGui;
import meow.miyarishu.meowhook.features.modules.client.Colors;
import meow.miyarishu.meowhook.features.setting.Setting;
import org.lwjgl.input.Mouse;

public class Slider
        extends Button {
    private final Number min;
    private final Number max;
    private final int difference;
    public Setting setting;

    public Slider(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number) setting.getMin();
        this.max = (Number) setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.dragSetting(mouseX, mouseY);
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);
        RenderUtil.drawRect(this.x, this.y, ((Number) this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float) this.width + 7.4f) * this.partialMultiplier(), this.y + (float) this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(Colors.class).hoverAlpha.getValue()) : MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue()));
        MeowHook.textManager.drawStringWithShadow((ClickGui.getInstance().lowerCase.getValue() ? this.getName().toLowerCase() : this.getName()) + " " + ChatFormatting.GRAY + (this.setting.getValue() instanceof Float ? this.setting.getValue() : Double.valueOf(((Number) this.setting.getValue()).doubleValue())), this.x + 2.3f, this.y - 1.7f - (float) MeowHookGui.getClickGui().getTextOffset(), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : MeowHookGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() + 8.0f && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void dragSetting(int mouseX, int mouseY) {
        if (this.isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public int getHeight() {
        return 9;
    }

    private void setSettingFromX(int mouseX) {
        float percent = ((float) mouseX - this.x) / ((float) this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            double result = (Double) this.setting.getMin() + (double) ((float) this.difference * percent);
            this.setting.setValue((double) Math.round(10.0 * result) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            float result = ((Float) this.setting.getMin()).floatValue() + (float) this.difference * percent;
            this.setting.setValue(Float.valueOf((float) Math.round(10.0f * result) / 10.0f));
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer) this.setting.getMin() + (int) ((float) this.difference * percent));
        }
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number) this.setting.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}

