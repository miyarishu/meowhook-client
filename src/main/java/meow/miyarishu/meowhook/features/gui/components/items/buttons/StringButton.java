package meow.miyarishu.meowhook.features.gui.components.items.buttons;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.util.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.features.gui.MeowHookGui;
import meow.miyarishu.meowhook.features.modules.client.ClickGui;
import meow.miyarishu.meowhook.features.modules.client.Colors;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;

public class StringButton
        extends Button {
    private final Setting setting;
    public boolean isListening;
    private CurrentString currentString = new CurrentString("");

    public StringButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(Colors.class).hoverAlpha.getValue()) : MeowHook.colorManager.getColorWithAlpha(MeowHook.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        if (this.isListening) {
            MeowHook.textManager.drawStringWithShadow(this.currentString.getString() + MeowHook.textManager.getIdleSign(), this.x + 2.3f, this.y - 1.7f - (float) MeowHookGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            MeowHook.textManager.drawStringWithShadow((ClickGui.getInstance().lowerCase.getValue() ? this.setting.getName().toLowerCase() : this.setting.getName()) + " " + ChatFormatting.GRAY + this.setting.getValue(), this.x + 2.3f, this.y - 1.7f - (float) MeowHookGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
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
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            switch (keyCode) {
                case 1: {
                    return;
                }
                case 28: {
                    this.enterString();
                }
                case 14: {
                    this.setString(StringButton.removeLastChar(this.currentString.getString()));
                }
            }
            if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                this.setString(this.currentString.getString() + typedChar);
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        } else {
            this.setting.setValue(this.currentString.getString());
        }
        this.setString("");
        this.onMouseClick();
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static class CurrentString {
        private final String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}

