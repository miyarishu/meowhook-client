package meow.miyarishu.meowhook.features.modules;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.event.events.ClientEvent;
import meow.miyarishu.meowhook.features.Feature;
import meow.miyarishu.meowhook.features.modules.client.Management;
import meow.miyarishu.meowhook.features.modules.client.Notifications;
import meow.miyarishu.meowhook.features.setting.Bind;
import meow.miyarishu.meowhook.features.setting.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.event.events.Render2DEvent;
import meow.miyarishu.meowhook.event.events.Render3DEvent;
import meow.miyarishu.meowhook.features.command.Command;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

public class Module
        extends Feature {
    private final String description;
    private final Category category;
    public Setting<Boolean> enabled = this.register(new Setting<Boolean>("Enabled", false));
    public Setting<Boolean> toggleNotify = this.register(new Setting<Boolean>("Notify", true));
    public Setting<Boolean> drawn = this.register(new Setting<Boolean>("Drawn", true));
    public Setting<Bind> bind = this.register(new Setting<Bind>("Keybind", new Bind(-1)));
    public Setting<String> displayName;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public float vOffset;
    public boolean sliding;

    public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
        super(name);
        this.displayName = this.register(new Setting<String>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return this.enabled.getValue() == false;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public TextComponentString getToggleMsg(boolean enable) {
        switch (Notifications.getInstance().toggleMsg.getValue()) {
            case Normal: {
                return new TextComponentString(MeowHook.commandManager.getClientMessage() + " " + Management.getInstance().accentColor.getValue() + this.getDisplayName() + Management.getInstance().mainColor.getValue() + " was " + (enable ? ChatFormatting.GREEN + "enabled" : ChatFormatting.RED + "disabled"));
            }
            case ForgeHax: {
                return new TextComponentString(MeowHook.commandManager.getClientMessage() + " " + Management.getInstance().accentColor.getValue() + this.getDisplayName() + Management.getInstance().mainColor.getValue() + ".enabled = " + (enable ? ChatFormatting.GREEN + "true" : ChatFormatting.RED + "false"));
            }
            case DotGod: {
                return new TextComponentString(MeowHook.commandManager.getClientMessage() + " " + Management.getInstance().accentColor.getValue() + this.getDisplayName() + Management.getInstance().mainColor.getValue() + " toggled " + (enable ? ChatFormatting.GREEN + "ON" + Management.getInstance().mainColor.getValue() + "!" : ChatFormatting.RED + "OFF" + Management.getInstance().mainColor.getValue() + "!"));
            }
        }
        return new TextComponentString("Failed to fetch toggle message");
    }

    public void enable() {
        this.enabled.setValue(Boolean.TRUE);
        this.onToggle();
        this.onEnable();
        if (this.toggleNotify.getValue()) {
            TextComponentString text = getToggleMsg(true);
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        this.enabled.setValue(false);
        if (this.toggleNotify.getValue()) {
            TextComponentString text = getToggleMsg(false);
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String name) {
        Module module = MeowHook.moduleManager.getModuleByDisplayName(name);
        Module originalModule = MeowHook.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage(ChatFormatting.RED + "A module of this name already exists.");
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public boolean listening() {
        return this.hasListener && this.isOn() || this.alwaysListening;
    }

    public String getFullArrayString() {
        return this.getDisplayName() + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.RESET + "]" : "");
    }

    public enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        VISUAL("Visual"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        CLIENT("Client");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

