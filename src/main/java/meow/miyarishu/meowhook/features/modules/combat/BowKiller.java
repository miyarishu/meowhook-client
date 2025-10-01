package meow.miyarishu.meowhook.features.modules.combat;

import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BowKiller extends Module {

    private boolean shooting;
    private long lastShootTime;

    public BowKiller() { super("BowKiller", "Uno hitter w bows", Category.COMBAT, true, false, false); }

    public Setting<Boolean> Bows = this.register ( new Setting <> ( "Bows", true ) );
    public Setting <Boolean> pearls = this.register ( new Setting <> ( "Pearls", true ) );
    public Setting <Boolean> eggs = this.register ( new Setting <> ( "Eggs", true ) );
    public Setting <Boolean> snowballs = this.register ( new Setting <> ( "Snowballs", true ) );
    public Setting <Integer> Timeout = this.register ( new Setting <> ( "Timeout", 5000, 100, 20000 ) );
    public Setting <Integer> spoofs = this.register ( new Setting <> ( "Spoofs", 10, 1, 300 ) );
    public Setting <Boolean> bypass = this.register ( new Setting <> ( "Bypass", false));
    public Setting <Boolean> debug = this.register ( new Setting <> ( "Debug", false));




    @Override
    public void onEnable() {
        if ( this.isEnabled()) {
            shooting = false;
            lastShootTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() != 0) return;

        if (event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();

            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                ItemStack handStack = Util.mc.player.getHeldItem(EnumHand.MAIN_HAND);

                if (!handStack.isEmpty() && handStack.getItem() != null && handStack.getItem() instanceof ItemBow && Bows.getValue()) {
                    doSpoofs();
                    if (debug.getValue()) Command.sendMessage("Trying to bop");
                }
            }

        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem) {
            CPacketPlayerTryUseItem packet2 = (CPacketPlayerTryUseItem) event.getPacket();

            if (packet2.getHand() == EnumHand.MAIN_HAND) {
                ItemStack handStack = Util.mc.player.getHeldItem(EnumHand.MAIN_HAND);

                if (!handStack.isEmpty() && handStack.getItem() != null) {
                    if (handStack.getItem() instanceof ItemEgg && eggs.getValue()) {
                        doSpoofs();
                    } else if (handStack.getItem() instanceof ItemEnderPearl && pearls.getValue()) {
                        doSpoofs();
                    } else if (handStack.getItem() instanceof ItemSnowball && snowballs.getValue()) {
                        doSpoofs();
                    }
                }
            }
        }
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - lastShootTime >= Timeout.getValue()) {
            shooting = true;
            lastShootTime = System.currentTimeMillis();

            Util.mc.player.connection.sendPacket(new CPacketEntityAction(Util.mc.player, CPacketEntityAction.Action.START_SPRINTING));

            for (int index = 0; index < spoofs.getValue(); ++index) {
                if (bypass.getValue()) {
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 1e-10, Util.mc.player.posZ, false));
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY - 1e-10, Util.mc.player.posZ, true));
                } else {
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY - 1e-10, Util.mc.player.posZ, true));
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 1e-10, Util.mc.player.posZ, false));
                }

            }

            if (debug.getValue()) Command.sendMessage("Bopped");

            shooting = false;
        }
    }
}