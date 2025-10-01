package meow.miyarishu.meowhook.mixin.mixins;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.modules.player.TpsSync;
import meow.miyarishu.meowhook.features.modules.movement.PacketFly;
import com.mojang.authlib.GameProfile;
import meow.miyarishu.meowhook.features.modules.visual.NoBlockRender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityPlayer.class})
public abstract class MixinEntityPlayer
        extends EntityLivingBase {
    public MixinEntityPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn);
    }


    @Inject(method={"getCooldownPeriod"}, at={@At(value="HEAD")}, cancellable=true)
    private void getCooldownPeriodHook(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (TpsSync.getInstance().isOn() && TpsSync.getInstance().attack.getValue().booleanValue()) {
            callbackInfoReturnable.setReturnValue(Float.valueOf((float)(1.0 / ((EntityPlayer)EntityPlayer.class.cast((Object)this)).getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue() * 20.0 * (double) MeowHook.serverManager.getTpsFactor())));
        }
    }

    @Inject(method={"isEntityInsideOpaqueBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void isEntityInsideOpaqueBlockHook(CallbackInfoReturnable<Boolean> info) {
        if (NoBlockRender.getInstance().isOn()) {
            info.cancel();
        }
        if (PacketFly.getInstance().isOn()) {
            info.setReturnValue(false);
        }
    }
}
