package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.init.SoundEvents;

import java.util.HashSet;
import java.util.Set;

public class EntityNotifier
        extends Module {
    public Setting<Boolean> Ghasts = this.register(new Setting<Boolean>("Ghasts", true));
    public Setting<Boolean> pfly = this.register(new Setting<Boolean>("Packetfly", true, v -> this.Ghasts.getValue()));

    public Setting<String> fprefix = this.register(new Setting<String>("Future Prefix", "~", v -> this.pfly.getValue()));
    public Setting<Boolean> Donkeys = this.register(new Setting<Boolean>("Donkeys", true));
    private final Set<Entity> ghasts = new HashSet<Entity>();
    private final Set<Entity> donkeys = new HashSet<Entity>();

    public EntityNotifier() {
        super("Entity Notifier", "Helps you find specific entities", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        this.ghasts.clear();
        this.donkeys.clear();
    }

    @Override
    public void onTick() {
        for (Entity entity : EntityNotifier.mc.world.getLoadedEntityList()) {
            if ((entity instanceof EntityGhast) && !(this.ghasts.contains(entity)) && this.Ghasts.getValue()) {
                Command.sendMessage("Ghast found at X: " + entity.getPosition().getX() + " Y: " + entity.getPosition().getY() + " Z: " + entity.getPosition().getZ());
                if (this.pfly.getValue()) {
                    EntityNotifier.mc.player.sendChatMessage(this.fprefix.getValue() + "toggle packetfly off");
                }
                this.ghasts.add(entity);
                EntityNotifier.mc.player.playSound(SoundEvents.ENTITY_GHAST_WARN, 1.0f, 1.0f);
            }
            if ((entity instanceof EntityDonkey) && !(this.donkeys.contains(entity)) && this.Donkeys.getValue()) {
                Command.sendMessage("Donkey found at X: " + entity.getPosition().getX() + " Y: " + entity.getPosition().getY() + " Z: " + entity.getPosition().getZ());
                this.donkeys.add(entity);
                EntityNotifier.mc.player.playSound(SoundEvents.ENTITY_DONKEY_AMBIENT, 1.0f, 1.0f);
            }
        }
    }
}

