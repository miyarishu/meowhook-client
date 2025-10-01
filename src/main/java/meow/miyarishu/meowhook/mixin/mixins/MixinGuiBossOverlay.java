package meow.miyarishu.meowhook.mixin.mixins;

import meow.miyarishu.meowhook.features.modules.visual.NoBossBar;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBossOverlay.class)
public abstract class MixinGuiBossOverlay {

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    public void renderHook(CallbackInfo ci) {
        if (NoBossBar.getInstance().isOn()) {
            ci.cancel();
        }
    }
}
