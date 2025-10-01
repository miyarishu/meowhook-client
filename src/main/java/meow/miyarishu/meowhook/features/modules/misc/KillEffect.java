package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.event.events.DeathEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KillEffect extends Module {

    public KillEffect() {
        super("FancyKill", "Makes a lightning bolt when someone dies", Category.MISC, true, false, false);
    }

    Setting<Boolean> lightning = register(new Setting("Lightning", true));
    Setting<Boolean> sound = register(new Setting("Sound", true, v -> lightning.getValue()));
    int ticks;
    boolean done;

    @Override
    public void onTick() {
        ticks++;
        if (ticks > 20) {
            ticks = 0;
            done = false;
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent e) {
        if (fullNullCheck() || isDisabled()) {
            return;
        }
        if (lightning.getValue() && !done) {
            EntityLightningBolt bolt = new EntityLightningBolt(mc.world, e.player.posX, e.player.posY, e.player.posZ, false);
            bolt.setLocationAndAngles(e.player.posX, e.player.posY, e.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
            mc.world.spawnEntity(bolt);
            if (sound.getValue()) {
                mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 1.0f, 1.0f);
                mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_IMPACT, 1.0f, 1.0f);
            }
            done = true;
        }
    }
}