package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoEmoticon
        extends Module {
    private final Setting<Integer> spam_delay = register(new Setting("Delay", 20, 0, 60));
    private Setting<Boolean> showEmoticons = this.register(new Setting<Boolean>("Emoticons", false));
    private final Timer timer = new Timer();
    private final Random r = new Random();

    public AutoEmoticon() {
        super("AutoEmoticon", "floods chat with cute emoticons :3", Category.MISC, true, false, false);
    }

    List<String> emoticons = new ArrayList<>();

    @Override
    public void onDisable() {
        this.timer.reset();
    }

    @Override
    public void onLoad() { // pee mode
        emoticons.add("(* ^ \u03C9 ^)");
        emoticons.add("(\u00B4 \u2200 ` *)");
        emoticons.add("\u0669(\u25D5\u203F\u25D5\uFF61)\u06F6");
        emoticons.add("\u2606*:.\uFF61.o(\u2267\u25BD\u2266)o.\uFF61.:*\u2606");
        emoticons.add("(o^\u25BD^o)");
        emoticons.add("(\u2312\u25BD\u2312)\u2606");
        emoticons.add("<(\uFFE3\uFE36\uFFE3)>");
        emoticons.add("\u3002.:\u2606*:\uFF65'(*\u2312\u2015\u2312*)))");
        emoticons.add("\u30FD(\u30FB\u2200\u30FB)\uFF89");
        emoticons.add("(\u00B4\uFF61\u2022 \u03C9 \u2022\uFF61`)");
        emoticons.add("(\uFFE3\u03C9\uFFE3)");
        emoticons.add("\uFF40;:\u309B;\uFF40;\uFF65(\u00B0\u03B5\u00B0 )");
        emoticons.add("(o\uFF65\u03C9\uFF65o)");
        emoticons.add("(\uFF20\uFF3E\u25E1\uFF3E)");
        emoticons.add("\u30FD(*\u30FB\u03C9\u30FB)\uFF89");
        emoticons.add("(o_ _)\uFF89\u5F61\u2606");
        emoticons.add("(^\u4EBA^)");
        emoticons.add("(o\u00B4\u25BD`o)");
        emoticons.add("(*\u00B4\u25BD`*)");
        emoticons.add("\uFF61\uFF9F( \uFF9F^\u2200^\uFF9F)\uFF9F\uFF61");
        emoticons.add("( \u00B4 \u03C9 ` )");
        emoticons.add("(((o(*\u00B0\u25BD\u00B0*)o)))");
        emoticons.add("(\u2267\u25E1\u2266)");
        emoticons.add("(o\u00B4\u2200`o)");
        emoticons.add("(\u00B4\u2022 \u03C9 \u2022`)");
        emoticons.add("(\uFF3E\u25BD\uFF3E)");
        emoticons.add("(\u2312\u03C9\u2312)");
        emoticons.add("\u2211d(\u00B0\u2200\u00B0d)");
        emoticons.add("\u2570(\u2594\u2200\u2594)\u256F");
        emoticons.add("(\u2500\u203F\u203F\u2500)");
        emoticons.add("(*^\u203F^*)");
        emoticons.add("\u30FD(o^ ^o)\uFF89");
        emoticons.add("(\u272F\u25E1\u272F)");
        emoticons.add("(\u25D5\u203F\u25D5)");
        emoticons.add("(*\u2267\u03C9\u2266*)");
        emoticons.add("(\u2606\u25BD\u2606)");
        emoticons.add("(\u2312\u203F\u2312)");
        emoticons.add("\uFF3C(\u2267\u25BD\u2266)\uFF0F");
        emoticons.add("\u30FD(o\uFF3E\u25BD\uFF3Eo)\u30CE");
        emoticons.add("\u2606 \uFF5E('\u25BD^\u4EBA)");
        emoticons.add("(*\u00B0\u25BD\u00B0*)");
        emoticons.add("\u0669(\uFF61\u2022\u0301\u203F\u2022\u0300\uFF61)\u06F6");
        emoticons.add("(\u2727\u03C9\u2727)");
        emoticons.add("\u30FD(*\u2312\u25BD\u2312*)\uFF89");
        emoticons.add("(\u00B4\uFF61\u2022 \u1D55 \u2022\uFF61`)");
        emoticons.add("( \u00B4 \u25BD ` )");
        emoticons.add("(\uFFE3\u25BD\uFFE3)");
        emoticons.add("\u2570(*\u00B4\uFE36`*)\u256F");
        emoticons.add("\u30FD(>\u2200<\u2606)\u30CE");
        emoticons.add("o(\u2267\u25BD\u2266)o");
        emoticons.add("(\u2606\u03C9\u2606)");
        emoticons.add("(\u3063\u02D8\u03C9\u02D8\u03C2 )");
        emoticons.add("\uFF3C(\uFFE3\u25BD\uFFE3)\uFF0F");
        emoticons.add("(*\u00AF\uFE36\u00AF*)");
        emoticons.add("\uFF3C(\uFF3E\u25BD\uFF3E)\uFF0F");
        emoticons.add("\u0669(\u25D5\u203F\u25D5)\u06F6");
        emoticons.add("(o\u02D8\u25E1\u02D8o)");
        emoticons.add("(\u3003\uFF3E\u25BD\uFF3E\u3003)");
        emoticons.add("(\u256F\u2727\u25BD\u2727)\u256F");
        emoticons.add("o(>\u03C9<)o");
        emoticons.add("o( \u275B\u1D17\u275B )o");
        emoticons.add("\uFF61\uFF9F(T\u30EET)\uFF9F\uFF61");
        emoticons.add("( \u203E\u0301 \u25E1 \u203E\u0301 )");
        emoticons.add("(\uFF89\u00B4\u30EE`)\uFF89*: \uFF65\uFF9F");
        emoticons.add("(b \u1D54\u25BD\u1D54)b");
        emoticons.add("(\u0E51\u02C3\u1D17\u02C2)\uFEED");
        emoticons.add("(\u0E51\u02D8\uFE36\u02D8\u0E51)");
        emoticons.add("(*\uA4A6\u0EB4\uA4B3\uA4A6\u0EB5)");
        emoticons.add("\u00B0\u02D6\u2727\u25DD(\u2070\u25BF\u2070)\u25DC\u2727\u02D6\u00B0");
        emoticons.add("(\u00B4\uFF65\u1D17\uFF65 ` )");
        emoticons.add("(\uFF89\u25D5\u30EE\u25D5)\uFF89*:\uFF65\uFF9F\u2727");
        emoticons.add("(\u201E\u2022 \u058A \u2022\u201E)");
        emoticons.add("(.\u275B \u1D17 \u275B.)");
        emoticons.add("(\u2040\u15E2\u2040)");
        emoticons.add("(\uFFE2\u203F\uFFE2 )");
        emoticons.add("(\u00AC\u203F\u00AC )");
        emoticons.add("(*\uFFE3\u25BD\uFFE3)b");
        emoticons.add("( \u02D9\u25BF\u02D9 )");
        emoticons.add("(\u00AF\u25BF\u00AF)");
        emoticons.add("( \u25D5\u25BF\u25D5 )");
        emoticons.add("\uFF3C(\u0665\u2040\u25BD\u2040 )\uFF0F");
        emoticons.add("(\u201E\u2022 \u1D17 \u2022\u201E)");
        emoticons.add("(\u1D54\u25E1\u1D54)");
        emoticons.add("( \u00B4 \u25BF ` )");
        emoticons.add("(\uFF89\u00B4 \u0437 `)\u30CE");
        emoticons.add("(\u2661\u03BC_\u03BC)");
        emoticons.add("(*^^*)\u2661");
        emoticons.add("\u2606\u2312\u30FD(*'\uFF64^*)chu");
        emoticons.add("(\u2661-_-\u2661)");
        emoticons.add("(\uFFE3\u03B5\uFFE3\uFF20)");
        emoticons.add("\u30FD(\u2661\u203F\u2661)\u30CE");
        emoticons.add("( \u00B4 \u2200 `)\u30CE\uFF5E \u2661");
        emoticons.add("(\u2500\u203F\u203F\u2500)\u2661");
        emoticons.add("(\u00B4\uFF61\u2022 \u1D55 \u2022\uFF61`) \u2661");
        emoticons.add("(*\u2661\u2200\u2661)");
        emoticons.add("(\uFF61\u30FB//\u03B5//\u30FB\uFF61)");
        emoticons.add("(\u00B4 \u03C9 `\u2661)");
        emoticons.add("\u2661( \u25E1\u203F\u25E1 )");
        emoticons.add("(\u25D5\u203F\u25D5)\u2661");
        emoticons.add("(/\u25BD\uFF3C*)\uFF61o\u25CB\u2661");
        emoticons.add("(\u10E6\u02D8\u2323\u02D8\u10E6)");
        emoticons.add("(\u2661\u00B0\u25BD\u00B0\u2661)");
        emoticons.add("\u2661(\uFF61- \u03C9 -)");
        emoticons.add("\u2661 \uFF5E('\u25BD^\u4EBA)");
        emoticons.add("(\u00B4\u2022 \u03C9 \u2022`) \u2661");
        emoticons.add("(\u00B4 \u03B5 ` )\u2661");
        emoticons.add("(\u00B4\uFF61\u2022 \u03C9 \u2022\uFF61`) \u2661");
        emoticons.add("( \u00B4 \u25BD ` ).\uFF61\uFF4F\u2661");
        emoticons.add("\u2570(*\u00B4\uFE36`*)\u256F\u2661");
        emoticons.add("(*\u02D8\uFE36\u02D8*).\uFF61.:*\u2661");
        emoticons.add("(\u2661\u02D9\uFE36\u02D9\u2661)");
        emoticons.add("\u2661\uFF3C(\uFFE3\u25BD\uFFE3)\uFF0F\u2661");
        emoticons.add("(\u2267\u25E1\u2266) \u2661");
        emoticons.add("(\u2312\u25BD\u2312)\u2661");
        emoticons.add("(*\u00AF \u00B3\u00AF*)\u2661");
        emoticons.add("(\u3063\u02D8\u0437(\u02D8\u2323\u02D8 ) \u2661");
        emoticons.add("\u2661 (\u02D8\u25BD\u02D8>\u0505( \u02D8\u2323\u02D8)");
        emoticons.add("( \u02D8\u2323\u02D8)\u2661(\u02D8\u2323\u02D8 )");
        emoticons.add("(/^-^(^ ^*)/ \u2661");
        emoticons.add("\u0669(\u2661\u03B5\u2661)\u06F6");
        emoticons.add("\u03C3(\u2267\u03B5\u2266\u03C3) \u2661");
        emoticons.add("\u2661 (\u21C0 3 \u21BC)");
        emoticons.add("\u2661 (\uFFE3\u0417\uFFE3)");
        emoticons.add("(\u2764\u03C9\u2764)");
        emoticons.add("(\u02D8\u2200\u02D8)/(\u03BC\u203F\u03BC) \u2764");
        emoticons.add("\u2764 (\u0254\u02C6\u0437(\u02C6\u2323\u02C6c)");
        emoticons.add("(\u00B4\u2661\u203F\u2661`)");
        emoticons.add("(\u00B0\u25E1\u00B0\u2661)");
        emoticons.add("\u03A3>\u2015(\u3003\u00B0\u03C9\u00B0\u3003)\u2661\u2192");
        emoticons.add("(\u00B4,,\u2022\u03C9\u2022,,)\u2661");
        emoticons.add("(\u00B4\uA4B3`)\u2661");
        emoticons.add("\u2661(>\u1D17\u2022)");
        emoticons.add("(\u2312_\u2312;)");
        emoticons.add("(o^v^o)");
        emoticons.add("(*/\u03C9\uFF3C)");
        emoticons.add("(*/\u3002\uFF3C)");
        emoticons.add("(*/_\uFF3C)");
        emoticons.add("(*\uFF89\u03C9\uFF89)");
        emoticons.add("(o-_-o)");
        emoticons.add("(*\u03BC_\u03BC)");
        emoticons.add("( \u25E1\u203F\u25E1 *)");
        emoticons.add("(\u1D54.\u1D54)");
        emoticons.add("(*\uFF89\u2200`*)");
        emoticons.add("(//\u25BD//)");
        emoticons.add("(//\u03C9//)");
        emoticons.add("(\u30CE*\u00B0\u25BD\u00B0*)");
        emoticons.add("(*^.^*)");
        emoticons.add("(*\uFF89\u25BD\uFF89)");
        emoticons.add("(\uFFE3\u25BD\uFFE3*)\u309E");
        emoticons.add("(\u2044 \u2044\u2022\u2044\u03C9\u2044\u2022\u2044 \u2044)");
        emoticons.add("(*/\u25BD\uFF3C*)");
        emoticons.add("(\u2044 \u2044>\u2044 \u25BD \u2044<\u2044 \u2044)");
        emoticons.add("(\u201E\u0CA1\u03C9\u0CA1\u201E)");
        emoticons.add("(\u0E07 \u0E37\u25BF \u0E37)\u0E27");
        emoticons.add("( \u3003\u25BD\u3003)");
        emoticons.add("(/\u25BF\uFF3C )");
        emoticons.add("(///\uFFE3 \uFFE3///)");
        emoticons.add("(\u30CE_<\u3002)\u30FE(\u00B4 \u25BD ` )");
        emoticons.add("\uFF61\uFF65\uFF9F\uFF65(\uFF89\u0414`)\u30FD(\uFFE3\u03C9\uFFE3 )");
        emoticons.add("\u03C1(- \u03C9 -\u3001)\u30FE(\uFFE3\u03C9\uFFE3; )");
        emoticons.add("\u30FD(\uFFE3\u03C9\uFFE3(\u3002\u3002 )\u309D");
        emoticons.add("(*\u00B4 I `)\uFF89\uFF9F(\uFF89\u0414\uFF40\uFF9F)\uFF9F\uFF61");
        emoticons.add("\u30FD(~_~(\u30FB_\u30FB )\u309D");
        emoticons.add("(\uFF89_\uFF1B)\u30FE(\u00B4 \u2200 ` )");
        emoticons.add("(; \u03C9 ; )\u30FE(\u00B4\u2200`* )");
        emoticons.add("(*\u00B4\u30FC)\uFF89(\u30CE\u0434`)");
        emoticons.add("(\u00B4-\u03C9-`( _ _ )");
        emoticons.add("(\u3063\u00B4\u03C9`)\uFF89(\u2565\u03C9\u2565)");
        emoticons.add("(\uFF4F\u30FB_\u30FB)\u30CE\u201D(\u30CE_<\u3001)");
        emoticons.add("(\uFF03\uFF1E\uFF1C)");
        emoticons.add("(\uFF1B\u2323\u0300_\u2323\u0301)");
        emoticons.add("\u2606\uFF4F(\uFF1E\uFF1C\uFF1B)\u25CB");
        emoticons.add("(\uFFE3 \uFFE3|||)");
        emoticons.add("(\uFF1B\uFFE3\u0414\uFFE3)");
        emoticons.add("(\uFFE3\u25A1\uFFE3\u300D)");
        emoticons.add("(\uFF03\uFFE30\uFFE3)");
        emoticons.add("(\uFF03\uFFE3\u03C9\uFFE3)");
        emoticons.add("(\uFFE2_\uFFE2;)");
        emoticons.add("(\uFF1E\uFF4D\uFF1C)");
        emoticons.add("(\u300D\u00B0\u30ED\u00B0)\u300D");
        emoticons.add("(\u3003\uFF1E\uFF3F\uFF1C;\u3003)");
        emoticons.add("(\uFF3E\uFF3E\uFF03)");
        emoticons.add("(\uFE36\uFE39\uFE3A)");
        emoticons.add("(\uFFE3\u30D8\uFFE3)");
        emoticons.add("<(\uFFE3 \uFE4C \uFFE3)>");
        emoticons.add("(\uFFE3\uFE3F\uFFE3)");
        emoticons.add("(\uFF1E\uFE4F\uFF1C)");
        emoticons.add("\u51F8(\uFFE3\u30D8\uFFE3)");
        emoticons.add("\u30FE( \uFFE3O\uFFE3)\u30C4");
        emoticons.add("(\u21C0\u2038\u21BC\u2036)");
        emoticons.add("(\u300D\uFF1E\uFF1C)\u300D");
        emoticons.add("(\u15D2\u15E3\u15D5)\u055E");
        emoticons.add("(\uB208_\uB208)");
    }

    @Override
    public void onTick() {
        if (this.showEmoticons.getValue()) {
            int es = 0;
            for (String e : this.emoticons) {
                Command.sendMessage(e);
                es++;
            }
            Command.sendMessage(es + " total emoticons");
            this.showEmoticons.setValue(false);
        }
    }


    @Override
    public void onUpdate() {
        if (timer.passedS(this.spam_delay.getValue())) {
            String emoticon = emoticons.get(r.nextInt(emoticons.size()));
            this.mc.player.sendChatMessage(emoticon);
            timer.reset();
        }
    }
}