package meow.miyarishu.meowhook.manager;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.Feature;
import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.event.events.Render2DEvent;
import meow.miyarishu.meowhook.event.events.Render3DEvent;
import meow.miyarishu.meowhook.features.gui.MeowHookGui;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.modules.client.*;
import meow.miyarishu.meowhook.features.modules.movement.*;
import meow.miyarishu.meowhook.util.Util;
import meow.miyarishu.meowhook.features.modules.combat.BowKiller;
import meow.miyarishu.meowhook.features.modules.misc.Crasher;
import meow.miyarishu.meowhook.features.modules.misc.TeleportTracer;
import meow.miyarishu.meowhook.features.modules.combat.*;
import meow.miyarishu.meowhook.features.modules.misc.*;
import meow.miyarishu.meowhook.features.modules.player.*;
import meow.miyarishu.meowhook.features.modules.visual.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        this.modules.add(new ClickGui());
        this.modules.add(new FontMod());
        this.modules.add(new ExtraTab());
        this.modules.add(new HUD());
        this.modules.add(new BlockHighlight());
        this.modules.add(new HoleESP());
        this.modules.add(new Wireframe());
        this.modules.add(new Replenish());
        this.modules.add(new Trajectories());
        this.modules.add(new FakePlayer());
        this.modules.add(new MCP());
        this.modules.add(new LiquidInteract());
        this.modules.add(new Speedmine());
        this.modules.add(new ReverseStep());
        this.modules.add(new AntiVoid());
        this.modules.add(new NoHandShake());
        this.modules.add(new MCF());
        this.modules.add(new ToolTips());
        this.modules.add(new Offhand());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoWeb());
        this.modules.add(new Criticals());
        this.modules.add(new HoleFiller());
        this.modules.add(new AutoArmor());
        this.modules.add(new ESP());
        this.modules.add(new Burrow());
        this.modules.add(new ArrowESP());
        this.modules.add(new OldOffhand());
        this.modules.add(new NoSway());
        this.modules.add(new HitAnimations());
        this.modules.add(new AspectRatio());
        this.modules.add(new SkyColor());
        this.modules.add(new BurrowESP());
        this.modules.add(new Background());
        this.modules.add(new Hitmarkers());
        this.modules.add(new PacketFly());
        this.modules.add(new LogoutSpots());
        this.modules.add(new EGapFinder());
        this.modules.add(new Crasher());
        this.modules.add(new Killaura());
        this.modules.add(new BedBomb());
        this.modules.add(new GlintModify());
        this.modules.add(new Quiver());
        this.modules.add(new AntiWeb());
        this.modules.add(new TeleportTracer());
        this.modules.add(new PenisESP());
        this.modules.add(new CityESP());
        this.modules.add(new ElytraFlight());
        this.modules.add(new Velocity());
        this.modules.add(new NameTags());
        this.modules.add(new FastPlace());
        this.modules.add(new NoSlowDown());
        this.modules.add(new MCX());
        this.modules.add(new AntiHunger());
        this.modules.add(new NewChunks());
        this.modules.add(new Fullbright());
        this.modules.add(new BowKiller());
        this.modules.add(new PopChams());
        this.modules.add(new ArmorAlert());
        this.modules.add(new Safety());
        this.modules.add(new ChatModifier());
        this.modules.add(new Colors());
        this.modules.add(new MobOwner());
        this.modules.add(new CrystalScale());
        this.modules.add(new Skeleton());
        this.modules.add(new AutoGhastFarmer());
        this.modules.add(new CowDupe());
        this.modules.add(new ClipOld());
        this.modules.add(new KillEffect());
        this.modules.add(new Notifications());
        this.modules.add(new AutoJewbase());
        this.modules.add(new AutoGG());
        this.modules.add(new UnicodeLagger());
        this.modules.add(new AutoEmoticon());
        this.modules.add(new NoBossBar());
        this.modules.add(new NoBlockRender());
        this.modules.add(new EntityNotifier());
        this.modules.add(new ChatFlooder());
        this.modules.add(new SlowHand());
        this.modules.add(new Swing());
        this.modules.add(new ChatSuffix());
        this.modules.add(new Management());
        this.modules.add(new NoInterp());
        this.modules.add(new Blocker());
        this.modules.add(new AntiCA());
        this.modules.add(new ShulkerNuker());
        this.modules.add(new GrabCoords());
        this.modules.add(new Media());
        this.modules.add(new MultiTask());
        this.modules.add(new Clip());
        this.modules.add(new FastFall());
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof MeowHookGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName()  + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.RESET + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = MeowHook.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.RESET + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

