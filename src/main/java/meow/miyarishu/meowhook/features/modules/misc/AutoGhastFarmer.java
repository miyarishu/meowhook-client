package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.Items;

public class AutoGhastFarmer
        extends Module {
    public Setting<String> baritonePrefix = this.register(new Setting<String>("Baritone Prefix", "#"));
    public int currentX;
    public int currentY;
    public int currentZ;
    public int itemX;
    public int itemY;
    public int itemZ;
    public int ghastX;
    public int ghastY;
    public int ghastZ;

    private int Activity;

    @Override
    public String getDisplayInfo() {
        switch (this.Activity) {
            case 0: {
                return  "Killing";
            }
            case 1: {
                return "Getting Tears";
            }
        }
        return "Idle";
    }

    public AutoGhastFarmer() {
        super("AutoGhastFarmer", "Automatically kills ghasts", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        block3: {
            block2: {
                if (AutoGhastFarmer.mc.player == null) break block2;
                if (AutoGhastFarmer.mc.world != null) break block3;
            }
            return;
        }
        this.currentX = (int) AutoGhastFarmer.mc.player.posX;
        this.currentY = (int) AutoGhastFarmer.mc.player.posY;
        this.currentZ = (int) AutoGhastFarmer.mc.player.posZ;
    }

    @Override
    public void onDisable() {
        block3: {
            block2: {
                if (AutoGhastFarmer.mc.player == null) break block2;
                if (AutoGhastFarmer.mc.world != null) break block3;
            }
            return;
        }
        AutoGhastFarmer.mc.player.sendChatMessage(baritonePrefix.getValue() + "stop");
    }

    @Override
    public void onUpdate() {
        if (AutoGhastFarmer.mc.player == null || AutoGhastFarmer.mc.world == null) {
            return;
        }
        Entity ghastEnt = null;
        double dist = Double.longBitsToDouble(Double.doubleToLongBits(0.017520017079696953) ^ 0x7FC8F0C47187D7FBL);
        for (Entity entity : AutoGhastFarmer.mc.world.loadedEntityList) {
            double ghastDist;
            if (!(entity instanceof EntityGhast) || !((ghastDist = (double) AutoGhastFarmer.mc.player.getDistance(entity)) < dist)) continue;
            dist = ghastDist;
            ghastEnt = entity;
            this.ghastX = (int)entity.posX;
            this.ghastY = (int)entity.posY;
            this.ghastZ = (int)entity.posZ;
        }
        ArrayList entityItems = new ArrayList();
        entityItems.addAll(AutoGhastFarmer.mc.world.loadedEntityList.stream().filter(AutoGhastFarmer::lambda$onUpdate$0).map(AutoGhastFarmer::lambda$onUpdate$1).filter(AutoGhastFarmer::lambda$onUpdate$2).collect(Collectors.toList()));
        Entity itemEnt = null;
        Iterator iterator = entityItems.iterator();
        while (iterator.hasNext()) {
            Entity item;
            itemEnt = item = (Entity)iterator.next();
            this.itemX = (int)item.posX;
            this.itemY = (int)item.posY;
            this.itemZ = (int)item.posZ;
        }
        if (ghastEnt != null) {
            AutoGhastFarmer.mc.player.sendChatMessage(baritonePrefix.getValue() + "goto " + this.ghastX + " " + this.ghastY + " " + this.ghastZ);
            this.Activity = 0;
        } else if (itemEnt != null) {
            AutoGhastFarmer.mc.player.sendChatMessage(baritonePrefix.getValue() + "goto " + this.itemX + " " + this.itemY + " " + this.itemZ);
            this.Activity = 1;
        } else {
            AutoGhastFarmer.mc.player.sendChatMessage(baritonePrefix.getValue() + "goto " + this.currentX + " " + this.currentY + " " + this.currentZ);
            this.Activity = 2;
        }
    }

    public static boolean lambda$onUpdate$2(EntityItem entityItem) {
        return entityItem.getItem().getItem() == Items.GHAST_TEAR;
    }

    public static EntityItem lambda$onUpdate$1(Entity entity) {
        return (EntityItem)entity;
    }

    public static boolean lambda$onUpdate$0(Entity entity) {
        return (Entity)entity instanceof EntityItem;
    }
}
