package meow.miyarishu.meowhook.features.modules.player;

import meow.miyarishu.meowhook.event.events.UpdateWalkingPlayerEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiHunger extends Module {

    public AntiHunger(){
        super("AntiHunger", "Stops you from getting hungry", Category.PLAYER, true, false, false);
    }

    private boolean onGround;

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {

        if(event.getStage() == 0){
            onGround = mc.player.onGround;

            if (mc.player.posY == mc.player.prevPosY)
            {
                mc.player.onGround = false;
            }
        }else{
            mc.player.onGround = onGround;
        }
    }

}
