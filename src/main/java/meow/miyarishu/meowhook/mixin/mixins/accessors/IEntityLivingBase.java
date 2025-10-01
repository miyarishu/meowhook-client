package meow.miyarishu.meowhook.mixin.mixins.accessors;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={EntityLivingBase.class})
public interface IEntityLivingBase {
    @Invoker(value="getArmSwingAnimationEnd")
    public int getArmSwingAnimationEnd();

    @Accessor(value = "ticksSinceLastSwing")
    int getTicksSinceLastSwing();

    @Accessor(value = "activeItemStackUseCount")
    int getActiveItemStackUseCount();

    @Accessor(value = "ticksSinceLastSwing")
    void setTicksSinceLastSwing(int ticks);

    @Accessor(value = "activeItemStackUseCount")
    void setActiveItemStackUseCount(int count);
}

