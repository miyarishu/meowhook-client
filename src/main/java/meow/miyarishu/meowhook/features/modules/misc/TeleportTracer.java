package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeleportTracer extends Module {

    public TeleportTracer(){
        super("TeleportTracer", "Tracks players when they teleport", Category.MISC, true, false, false);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketEntityTeleport){
            SPacketEntityTeleport sPacketEntityTeleport = (SPacketEntityTeleport) event.getPacket();

            if(Util.mc.world.getEntityByID(sPacketEntityTeleport.getEntityId()) instanceof EntityPlayer && Util.mc.world.getEntityByID(sPacketEntityTeleport.getEntityId()).getDistance(Util.mc.player)>50)
                Command.sendMessage("Teleport detected: "+ Util.mc.world.getEntityByID(sPacketEntityTeleport.getEntityId()).getName()+" X: "+sPacketEntityTeleport.getX()+" Y: "+sPacketEntityTeleport.getY()+" Z: "+sPacketEntityTeleport.getZ());
        }
    }

}
