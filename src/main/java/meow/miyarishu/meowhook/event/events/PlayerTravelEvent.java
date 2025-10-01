package meow.miyarishu.meowhook.event.events;

import meow.miyarishu.meowhook.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerTravelEvent extends EventStage {

    public float Strafe;
    public float Vertical;
    public float Forward;

    public PlayerTravelEvent(float Strafe, float Vertical, float Forward) {
        this.Strafe = Strafe;
        this.Vertical = Vertical;
        this.Forward = Forward;
    }

}
