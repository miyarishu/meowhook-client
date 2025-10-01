package meow.miyarishu.meowhook.event.events;

import meow.miyarishu.meowhook.event.EventStage;
import net.minecraft.util.math.BlockPos;

public class BlockDestructionEvent extends EventStage {

    BlockPos pos;

    public BlockDestructionEvent(BlockPos blockPos){
        super();
        pos = blockPos;
    }

    public BlockPos getPos(){
        return pos;
    }

    public void setPos(BlockPos pos){
        this.pos = pos;
    }
}
