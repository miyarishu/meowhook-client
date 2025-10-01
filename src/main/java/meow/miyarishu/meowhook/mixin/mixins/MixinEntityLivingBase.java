package meow.miyarishu.meowhook.mixin.mixins;

import meow.miyarishu.meowhook.features.modules.movement.ElytraFlight;
import meow.miyarishu.meowhook.features.modules.visual.SlowHand;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityLivingBase.class})
public abstract class MixinEntityLivingBase
extends Entity {
    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(method={"getArmSwingAnimationEnd"}, at={@At(value="HEAD")}, cancellable=true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        if (SlowHand.getInstance().isEnabled()) {
            info.setReturnValue(SlowHand.getInstance().speed.getValue());
        }
    }

    @Inject(method = { "isElytraFlying" }, at = { @At("HEAD") }, cancellable = true)
    private void isElytraFlyingHook(final CallbackInfoReturnable<Boolean> info) {
        if (Util.mc.player != null && Util.mc.player.equals((Object)this) && ElytraFlight.getInstance().isOn() && ElytraFlight.getInstance().mode.getValue() == ElytraFlight.Mode.BETTER) {
            info.setReturnValue(false);
        }
    }
}

