package meow.miyarishu.meowhook.features.modules.combat;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.event.events.Render2DEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.DamageUtil;
import meow.miyarishu.meowhook.util.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ArmorAlert extends Module {

    //https://www.youtube.com/watch?v=Zmr4KAPk958

    public ArmorAlert() {
        super("ArmorAlert", "Alerts you and your friends if you have low durability", Category.COMBAT, true, false, false);
    }

    private Setting<Integer> dura = this.register(new Setting<Integer>("Durability", 10, 1, 100));
    private final Map<EntityPlayer, Integer> entityArmorArraylist = new HashMap<EntityPlayer, Integer>();
    private final Timer timer = new Timer();
    private boolean lowDura = false;

    @Override
    public void onUpdate() {
        lowDura = false;

        try {
            for (ItemStack is : mc.player.getArmorInventoryList()) {
                float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int) (red * 100.0f);
                if (!((float) dmg <= dura.getValue())) continue;
                this.lowDura = true;
            }
        } catch (Exception ignored) {
        }
    }
    @Override
    public void onRender2D(Render2DEvent event) {
        if (lowDura) {
            final ScaledResolution sr = new ScaledResolution(mc);
            MeowHook.textManager.drawString("Your armor is below " + dura.getValue() + "% durability!", (sr.getScaledWidth() / 2) - ((MeowHook.textManager.getStringWidth("Your armor is below " + dura.getValue() + "% durability!")) / 2), 15, 0xffff0000, false);
        }
    }
}
