package meow.miyarishu.meowhook.mixin.mixins;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.modules.visual.GlintModify;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ RenderItem.class })
public class MixinRenderItem {

    @Shadow
    private void renderModel(IBakedModel model, int color, ItemStack stack) {}
    @ModifyArg(method = "renderEffect", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderEffect(int oldValue) {
        return MeowHook.moduleManager.getModuleByName("GlintModify").isEnabled() ? GlintModify.getColor(1, 1).getRGB()
                : oldValue;
    }
}