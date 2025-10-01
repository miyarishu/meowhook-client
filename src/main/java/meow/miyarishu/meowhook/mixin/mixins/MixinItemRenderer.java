package meow.miyarishu.meowhook.mixin.mixins;

import meow.miyarishu.meowhook.features.modules.visual.NoSway;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Shadow
    @Final
    public Minecraft mc;
    private boolean injection = true;

    @Inject(method={"rotateArm"}, at={@At(value="HEAD")}, cancellable=true)
    public void noSwayHook(float partialTicks, CallbackInfo ci) {
        if (NoSway.getInstance().isEnabled()) {
            ci.cancel();
        }
    }

    @Shadow
    public abstract void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Shadow
    protected abstract void renderArmFirstPerson(float var1, float var2, EnumHandSide var3);

}

