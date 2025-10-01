package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.event.events.PacketEvent;
import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.modules.client.HUD;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix
        extends Module {
    private Setting<Boolean> commands = this.register(new Setting<Boolean>("Commands", true));
    private Setting<Boolean> fancy = this.register(new Setting<Boolean>("Fancy", true));
    private Setting<Separator> separator = this.register(new Setting<Enum>("Separator", Separator.Normal));
    private Setting<Mode> mode = this.register(new Setting<Enum>("Mode", Mode.Client));
    private Setting<String> customText = this.register(new Setting<String>("Custom Text", "meowhook.cat", v -> mode.getValue() == Mode.Custom));
    public static String[] prefixs = new String[]{"/", "!", ",", ".", "-", ";", "?", "*", "&", "+", "$", "@", "~", Command.getCommandPrefix()};
    public ChatSuffix() {
        super("ChatSuffix", "Adds a suffix to the end of your chat message", Category.MISC, true, false, false);
    }

    private String getFancyText(String s) {
        return s.toLowerCase().replaceAll("a", "\u1D00").replaceAll("z", "\u1D22").replaceAll("e", "\u1D07").replaceAll("r", "\u0280").replaceAll("t", "\u1D1B").replaceAll("y", "\u028F").replaceAll("u", "\u1D1C").replaceAll("i", "\u026A").replaceAll("o", "\u1D0F").replaceAll("p", "\u1D18").replaceAll("q", "Q").replaceAll("s", "\uA731").replaceAll("d", "\u1D05").replaceAll("f", "\uA730").replaceAll("g", "\u0262").replaceAll("h", "\u029C").replaceAll("j", "\u1D0A").replaceAll("k", "\u1D0B").replaceAll("l", "\u029F").replaceAll("m", "\u1D0D").replaceAll("w", "\u1D21").replaceAll("x", "x").replaceAll("c", "\u1D04").replaceAll("v", "\u1D20").replaceAll("b", "\u0299").replaceAll("n", "\u0274");
    }

    private String getMeow(String client, Separator sep) {
        switch (sep) {
            case Hugging: return " (\u3063\u25D4\u25E1\u25D4)\u3063 \u2665 " + (this.fancy.getValue() ? getFancyText(client) : client) + " \u2665";
            case Normal: return " \u23d0 " + (this.fancy.getValue() ? getFancyText(client) : client);
            case Star: return " \u2729 " + (this.fancy.getValue() ? getFancyText(client) : client) + " \u2729";
            default: return "";
        }
    }

    private boolean isCommand(String s) {
        for (String value : prefixs) {
            if (s.startsWith(value)) return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            String name = HUD.getInstance().command.getPlannedValue();
            if (mode.getValue() == Mode.Custom) {
                name = customText.getValue();
            }
            if (!commands.getValue() && isCommand(s)) return;
            s += getMeow(name, separator.getValue());
            if (s.length() >= 256) s = s.substring(0, 256);
            ((CPacketChatMessage) event.getPacket()).message = s;
        }
    }

    private enum Mode {
        Client, Custom
    }

    private enum Separator {
        Normal, Hugging, Star
    }
}