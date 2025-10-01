package meow.miyarishu.meowhook.mixin.mixins;

import meow.miyarishu.meowhook.features.modules.misc.ToolTips;
import meow.miyarishu.meowhook.features.modules.client.Background;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {GuiScreen.class})
public class MixinGuiScreen
        extends Gui {

    @Inject(method = "drawWorldBackground", at = @At("HEAD"), cancellable = true)
    public void drawWorldBackground(int tint, CallbackInfo info) {
        if (Minecraft.getMinecraft().player != null && Background.getInstance().clean.getValue() && Background.getInstance().isEnabled()) {
            info.cancel();
        }
    }
    @Inject(method = {"renderToolTip"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderToolTipHook(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ToolTips.getInstance().isOn() && stack.getItem() instanceof ItemShulkerBox) {
            ToolTips.getInstance().renderShulkerToolTip(stack, x, y, null);
            info.cancel();
        }
    }
}

