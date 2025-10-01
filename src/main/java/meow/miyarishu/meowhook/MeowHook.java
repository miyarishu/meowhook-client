package meow.miyarishu.meowhook;

import meow.miyarishu.meowhook.manager.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = "meowhook", name = "meowhook", version = "1.0.0")
public class MeowHook {
    public static final String MODID = "meowhook";
    public static final String MODNAME = "meowhook";
    public static final String MODVER = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger("meowhook");
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static TimerManager timerManager;
    public static InteractionManager interactionManager;
    @Mod.Instance
    public static MeowHook INSTANCE;
    private static boolean unloaded;

    static {
        unloaded = false;
    }

    public static void load() {
        LOGGER.info("\n\nLoading Meowhook by miyarishu");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        timerManager = new TimerManager();
        interactionManager = new InteractionManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("Meowhook successfully loaded!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading Meowhook by miyarishu");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        MeowHook.onUnload();
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("Meowhook unloaded!\n");
    }

    public static void reload() {
        MeowHook.unload(false);
        MeowHook.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(MeowHook.configManager.config.replaceFirst("meowhook/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("meow -terjanq");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle(MODNAME + " - " + MODVER);
        MeowHook.load();
    }
}

