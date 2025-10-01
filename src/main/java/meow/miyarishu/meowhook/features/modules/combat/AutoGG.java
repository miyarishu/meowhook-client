package meow.miyarishu.meowhook.features.modules.combat;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.event.events.DeathEvent;
import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityEnderCrystal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoGG
        extends Module {
    private final Setting<String> ggText = this.register(new Setting<String>("Text", "GG <player>! Better luck next time! :3"));
    private final Setting<Boolean> onOwnDeath = this.register(new Setting<Boolean>("OwnDeath", false));
    private final Setting<Integer> targetResetTimer = this.register(new Setting<Integer>("Reset", 30, 0, 90));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 10, 0, 30));
    private final Setting<Boolean> test = this.register(new Setting<Boolean>("Test", false));
    public Map<EntityPlayer, Integer> targets = new ConcurrentHashMap<EntityPlayer, Integer>();
    private final Timer timer = new Timer();
    private final Timer cooldownTimer = new Timer();
    private boolean cooldown;
    private EntityEnderCrystal crystal;
    public AutoGG() {
        super("AutoGG", "Automatically GGs", Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {

        this.timer.reset();
        this.cooldownTimer.reset();
    }

    @Override
    public void onTick() {
        if (this.test.getValue().booleanValue()) {
            this.announceDeath(AutoGG.mc.player);
            this.test.setValue(false);
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.passedS(this.delay.getValue().intValue()) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        this.targets.replaceAll((p, v) -> (int) (this.timer.getPassedTimeMs() / 1000L));
        for (EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get(player) <= this.targetResetTimer.getValue()) continue;
            this.targets.remove(player);
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onEntityDeath(DeathEvent event) {
        if (this.targets.containsKey(event.player) && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
            this.targets.remove(event.player);
        }
        if (event.player == AutoGG.mc.player && this.onOwnDeath.getValue().booleanValue()) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityEnderCrystal) {
            this.crystal = (EntityEnderCrystal)event.getTarget();
        }
        if (event.getTarget() instanceof EntityPlayer && !MeowHook.friendManager.isFriend(event.getEntityPlayer())) {
            this.targets.put((EntityPlayer) event.getTarget(), 0);
        }
    }

    @SubscribeEvent
    public void onSendAttackPacket(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld(AutoGG.mc.world) instanceof EntityPlayer && !MeowHook.friendManager.isFriend((EntityPlayer) packet.getEntityFromWorld(AutoGG.mc.world))) {
            this.targets.put((EntityPlayer) packet.getEntityFromWorld(AutoGG.mc.world), 0);
        }
    }



    public void announceDeath(EntityPlayer target) {
        AutoGG.mc.player.sendChatMessage(this.ggText.getValue().replaceAll("<player>", target.getDisplayNameString()));
    }
}