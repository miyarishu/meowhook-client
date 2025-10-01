package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.modules.client.HUD;
import meow.miyarishu.meowhook.event.events.BlockDestructionEvent;
import meow.miyarishu.meowhook.util.BlockUtil;
import meow.miyarishu.meowhook.util.MathUtil;
import meow.miyarishu.meowhook.util.Timer;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.features.Feature;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.Random;

public class ChatFlooder extends Module {

    private final Setting<Boolean> move = this.register(new Setting<Boolean>("Move", true));
    private final Setting<Boolean> breakBlock = this.register(new Setting<Boolean>("Break", true));
    private final Setting<Boolean> eat = this.register(new Setting<Boolean>("Eat", true));

    private final Setting<Integer> delay = register(new Setting("Delay", 10, 2, 30));

    private double lastPositionX;
    private double lastPositionY;
    private double lastPositionZ;

    private int eaten;

    private int broken;

    private final Timer delayTimer = new Timer();

    public ChatFlooder() {
        super("ChatFlooder", "Floods chat", Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        eaten = 0;
        broken = 0;

        delayTimer.reset();
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) return;

        double traveledX = lastPositionX - Util.mc.player.lastTickPosX;
        double traveledY = lastPositionY - Util.mc.player.lastTickPosY;
        double traveledZ = lastPositionZ - Util.mc.player.lastTickPosZ;

        double traveledDistance = Math.sqrt(traveledX * traveledX + traveledY * traveledY + traveledZ * traveledZ);

        if (move.getValue()
                && traveledDistance >= 1
                && traveledDistance <= 1000
                && delayTimer.passedS(delay.getValue())) {

            Util.mc.player.sendChatMessage(getWalkMessage()
                    .replace("<blocks>", new DecimalFormat("0.00").format(traveledDistance)));

            lastPositionX = Util.mc.player.lastTickPosX;
            lastPositionY = Util.mc.player.lastTickPosY;
            lastPositionZ = Util.mc.player.lastTickPosZ;

            delayTimer.reset();
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent.Finish event) {
        if (Feature.fullNullCheck()) return;

        int random = MathUtil.getRandom(1, 6);

        if (eat.getValue()
                && event.getEntity() == Util.mc.player
                && event.getItem().getItem() instanceof ItemFood
                || event.getItem().getItem() instanceof ItemAppleGold) {

            ++eaten;

            if (eaten >= random && delayTimer.passedS(delay.getValue())) {

                Util.mc.player.sendChatMessage(getEatMessage()
                        .replace("<amount>", "" + eaten)
                        .replace("<name>", "" + event.getItem().getDisplayName()));

                eaten = 0;

                delayTimer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockDestructionEvent event) {
        if (Feature.fullNullCheck()) return;

        int random = MathUtil.getRandom(1, 6);

        ++broken;

        if (breakBlock.getValue()
                && broken >= random
                && delayTimer.passedS(delay.getValue())) {

            Util.mc.player.sendChatMessage(getBreakMessage()
                    .replace("<amount>", "" + broken)
                    .replace("<name>", "" + BlockUtil.getBlock(event.getPos()).getLocalizedName()));

            broken = 0;

            delayTimer.reset();
        }
    }

    private String getWalkMessage() {

        String[] walkMessage = {
                "I just meowed across <blocks> blocks like a kitty thanks to " + HUD.getInstance().command.getValue() + "! :3",
                "Je viens de miauler \u00E0 travers <blocks> blocs comme un chaton gr\u00E2ce \u00E0 " + HUD.getInstance().command.getValue() + "! :3",
                "Ho appena miagolato per <blocks> isolati come un gattino grazie a " + HUD.getInstance().command.getValue() + "! :3",
                "\u6211\u521A\u521A\u50CF\u4E00\u53EA\u5C0F\u732B\u4E00\u6837\u55B5\u55B5\u53EB\u7740\u7A7F\u8FC7\u4E86 <blocks> \u4E2A\u8857\u533A, \u611F\u8C22 " + HUD.getInstance().command.getValue() + "! :3",
                HUD.getInstance().command.getValue() + " \u306E\u304A\u304B\u3052\u3067\u5B50\u732B\u306E\u3088\u3046\u306B <blocks> \u306B\u30CB\u30E3\u30FC\u3068\u9CF4\u3044\u305F\u3060\u3051\u3067\u3059",
                "\u042F \u0442\u043E\u043B\u044C\u043A\u043E \u0447\u0442\u043E \u043C\u044F\u0443\u043A\u043D\u0443\u043B \u043D\u0430 <blocks>, \u043A\u0430\u043A \u043A\u043E\u0442\u0435\u043D\u043E\u043A, \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F " + HUD.getInstance().command.getValue() + "! :3",
        };

        return walkMessage[new Random().nextInt(walkMessage.length)];
    }

    private String getBreakMessage() {

        String[] breakMessage = {
                "I just meowed <amount> <name> thanks to " + HUD.getInstance().command.getValue() + "! :3",
                "Je viens de miauler <amount> <name> comme un chaton gr\u00E2ce \u00E0 " + HUD.getInstance().command.getValue() + "! :3",
                "Ho appena miagolato <amount> <name> come un gattino grazie a " + HUD.getInstance().command.getValue() + "! :3",
                "\u6211\u521A\u521A\u50CF\u5C0F\u732B\u4E00\u6837\u55B5\u55B5\u53EB\u4E86<amount> <name>, \u611F\u8C22 " + HUD.getInstance().command.getValue() + "! :3",
                HUD.getInstance().command.getValue() + " \u306E\u304A\u304B\u3052\u3067\u5B50\u732B\u306E\u3088\u3046\u306B <amount> <name> \u3068\u9CF4\u3044\u305F\u3088 " + "! :3",
                "\u042F \u0442\u043E\u043B\u044C\u043A\u043E \u0447\u0442\u043E \u043C\u044F\u0443\u043A\u043D\u0443\u043B <amount> <name> \u043A\u0430\u043A \u043A\u043E\u0442\u0435\u043D\u043E\u043A, \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F " + HUD.getInstance().command.getValue() + "! :3",
        };

        return breakMessage[new Random().nextInt(breakMessage.length)];
    }

    private String getEatMessage() {

        String[] eatMessage = {
                "I just meowed <amount> <name> thanks to " + HUD.getInstance().command.getValue() + "! :3",
                "Je viens de miauler <amount> <name> comme un chaton gr\u00E2ce \u00E0 " + HUD.getInstance().command.getValue() + "! :3",
                "Ho appena miagolato <amount> <name> come un gattino grazie a " + HUD.getInstance().command.getValue() + "! :3",
                "\u6211\u521A\u521A\u50CF\u5C0F\u732B\u4E00\u6837\u55B5\u55B5\u53EB\u4E86<amount> <name>, \u611F\u8C22 " + HUD.getInstance().command.getValue() + "! :3",
                HUD.getInstance().command.getValue() + " \u306E\u304A\u304B\u3052\u3067\u5B50\u732B\u306E\u3088\u3046\u306B <amount> <name> \u3068\u9CF4\u3044\u305F\u3088 " + "! :3",
                "\u042F \u0442\u043E\u043B\u044C\u043A\u043E \u0447\u0442\u043E \u043C\u044F\u0443\u043A\u043D\u0443\u043B <amount> <name> \u043A\u0430\u043A \u043A\u043E\u0442\u0435\u043D\u043E\u043A, \u0431\u043B\u0430\u0433\u043E\u0434\u0430\u0440\u044F " + HUD.getInstance().command.getValue() + "! :3",
        };

        return eatMessage[new Random().nextInt(eatMessage.length)];
    }
}