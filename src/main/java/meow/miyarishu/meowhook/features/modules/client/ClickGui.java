package meow.miyarishu.meowhook.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.event.events.ClientEvent;
import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.gui.MeowHookGui;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", "."));
    public Setting<Boolean> descriptions = this.register(new Setting<Boolean>("Show Descriptions", true));
    public Setting<Boolean> moduleNumber = this.register(new Setting<Boolean>("Show Module Amount", false));
    public Setting<Boolean> lowerCase = this.register(new Setting<Boolean>("Lowercase Text", false));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    public Setting<Boolean> componentCenter = this.register(new Setting<Boolean>("Component Center", false));
    public Setting<String> moduleButtonClosed = this.register(new Setting<String>("Closed", "<"));
    public Setting<String> moduleButtonOpen = this.register(new Setting<String>("Open", "V"));
    public Setting<Boolean> moduleButtonSound = this.register(new Setting<Boolean>("Sound", true));
    public Setting<Integer> backgroundAlpha = this.register(new Setting<Integer>("Background Alpha", 100, 0, 255));
    public Setting<Integer> buttonAlpha = this.register(new Setting<Integer>("On Button Alpha", 255, 0, 255));
    public Setting<Integer> buttonDisabledAlpha = this.register(new Setting<Integer>("Off Button Alpha", 40, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Hover Alpha", 120, 0, 255));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> Colors.getInstance().rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> Colors.getInstance().rainbow.getValue()));
    private MeowHookGui click;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                MeowHook.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + MeowHook.commandManager.getPrefix());
            }
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(MeowHookGui.getClickGui());
    }

    @Override
    public void onLoad() {
        MeowHook.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof MeowHookGui)) {
            this.disable();
        }
    }

    public enum rainbowModeArray {
        Static,
        Up

    }

    public enum rainbowMode {
        Static,
        Sideway

    }
}

