package meow.miyarishu.meowhook.features.modules.client;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.modules.misc.ToolTips;
import meow.miyarishu.meowhook.event.events.ClientEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.event.events.Render2DEvent;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Timer;
import meow.miyarishu.meowhook.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class HUD extends Module {
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final ItemStack gapple = new ItemStack(Items.GOLDEN_APPLE);
    private static final ItemStack crystal = new ItemStack(Items.END_CRYSTAL);
    public static HUD INSTANCE = new HUD();

    private static final double HALF_PI = Math.PI / 2;
    private final Setting<Boolean> waterMark = register(new Setting("Watermark", Boolean.valueOf(false), "displays watermark"));
    private final Setting<Boolean> grayNess = register(new Setting("Gray", Boolean.valueOf(true)));
    private final Setting<Boolean> renderingUp = register(new Setting("RenderingUp", Boolean.valueOf(false), "Orientation of the HUD-Elements."));
    private final Setting<Boolean> arrayList = register(new Setting("ActiveModules", Boolean.valueOf(false), "Lists the active modules."));
    private final Setting<Boolean> coords = register(new Setting("Coords", Boolean.valueOf(false), "Your current coordinates"));
    private final Setting<Boolean> direction = register(new Setting("Direction", Boolean.valueOf(false), "The Direction you are facing."));
    private final Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(false), "ArmorHUD"));
    public Setting<Boolean> totems = register(new Setting("Totems", false));
    public Setting<Integer> totemsX = register(new Setting("TotemsX", 0, 0, 1000, v -> totems.getValue()));
    public Setting<Integer> totemsY = register(new Setting("TotemsY", 0, 0, 1000, v -> totems.getValue()));
    public Setting<Boolean> gapples = register(new Setting("Gapples", false));
    public Setting<Integer> gapplesX = register(new Setting("GapplesX", 0, 0, 1000, v -> gapples.getValue()));
    public Setting<Integer> gapplesY = register(new Setting("GapplesY", 0, 0, 1000, v -> gapples.getValue()));
    public Setting<Boolean> crystals = register(new Setting("Crystals", false));
    public Setting<Integer> crystalsX = register(new Setting("CrystalsX", 0, 0, 1000, v -> crystals.getValue()));
    public Setting<Integer> crystalsY = register(new Setting("CrystalsY", 0, 0, 1000, v -> crystals.getValue()));
    private final Setting<Boolean> speed = register(new Setting("Speed", Boolean.valueOf(false), "Your Speed"));
    private final Setting<Boolean> potions = register(new Setting("Potions", Boolean.valueOf(false), "Your Speed"));
    public Setting<Boolean> potionsRoman = register(new Setting("Roman Numerical", Boolean.valueOf(true), v -> this.potions.getValue()));
    private final Setting<Boolean> ping = register(new Setting("Ping", Boolean.valueOf(false), "Your response time to the server."));
    private final Setting<Boolean> tps = register(new Setting("TPS", Boolean.valueOf(false), "Ticks per second of the server."));
    private final Setting<Boolean> fps = register(new Setting("FPS", Boolean.valueOf(false), "Your frames per second."));
    private final Setting<Boolean> lag = register(new Setting("LagNotifier", Boolean.valueOf(false), "The time"));
    private final meow.miyarishu.meowhook.util.Timer timer = new Timer();
    private Map<String, Integer> players = new HashMap<>();
    public Setting<GreeterMode> greetermode = register(new Setting("Greeter", GreeterMode.Swag));
    public Setting<String> greeterText = register(new Setting("Greeter Text", "welcome <player> to <client> :3", v -> this.greetermode.getValue() == GreeterMode.Custom));
    public Setting<String> command = register(new Setting("Client Name", "meowhook"));
    public Setting<TextUtil.Color> bracketColor = register(new Setting("BracketColor", TextUtil.Color.WHITE));
    public Setting<TextUtil.Color> commandColor = register(new Setting("NameColor", TextUtil.Color.LIGHT_PURPLE));
    public Setting<String> commandBracket = register(new Setting("Open Bracket", "<"));
    public Setting<String> commandBracket2 = register(new Setting("Closed Bracket", ">"));
    public Setting<Integer> animationHorizontalTime = register(new Setting("AnimationHTime", Integer.valueOf(500), Integer.valueOf(1), Integer.valueOf(1000), v -> this.arrayList.getValue().booleanValue()));
    public Setting<Integer> animationVerticalTime = register(new Setting("AnimationVTime", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(500), v -> this.arrayList.getValue().booleanValue()));
    public Setting<RenderingMode> renderingMode = register(new Setting("Ordering", RenderingMode.ABC));
    public Setting<Boolean> time = register(new Setting("Time", false, "The time"));
    public Setting<Boolean> textRadar = register(new Setting("TextRadar", false, "A textradar"));
    public Setting<Integer> lagTime = register(new Setting("LagTime", 1000, 0, 2000));
    public Setting<Boolean> rainbowPrefix = this.register(new Setting<Boolean>("RainbowPrefix", false));
    public Setting<Integer> waterMarkY = register(new Setting("WatermarkPosY", 2, 0, 20, v -> this.waterMark.getValue()));
    public Setting<Boolean> shadow = register(new Setting("Shadow", false));
    public Setting<Boolean> inventory = register(new Setting("Inventory", false));
    public Setting<Boolean> invBackground = register(new Setting("InvBox", false, v -> inventory.getValue()));
    public Setting<Integer> invX = register(new Setting("InvX", 564, 0, 1000, v -> inventory.getValue()));
    public Setting<Integer> invY = register(new Setting("InvY", 467, 0, 1000, v -> inventory.getValue()));
    public Setting<Integer> fineinvX = register(new Setting("InvFineX", 0, v -> inventory.getValue()));
    public Setting<Integer> fineinvY = register(new Setting("InvFineY", 0, v -> inventory.getValue()));
    public Setting<Boolean> renderXCarry = register(new Setting("RenderXCarry", false, v -> inventory.getValue()));
    public Setting<Integer> invH = register(new Setting("InvH", 3, v -> inventory.getValue()));
    public Setting<Boolean> holeHud = register(new Setting("HoleHUD", false));
    public Setting<Integer> holeX = register(new Setting("HoleX", 279, 0, 1000, v -> holeHud.getValue()));
    public Setting<Integer> holeY = register(new Setting("HoleY", 485, 0, 1000, v -> holeHud.getValue()));
    public Setting<Compass> compass = register(new Setting("Compass", Compass.NONE));
    public Setting<Integer> compassX = register(new Setting("CompX", 472, 0, 1000, v -> compass.getValue() != Compass.NONE));
    public Setting<Integer> compassY = register(new Setting("CompY", 424, 0, 1000, v -> compass.getValue() != Compass.NONE));
    public Setting<Integer> scale = register(new Setting("Scale", 3, 0, 10, v -> compass.getValue() != Compass.NONE));
    public Setting<Boolean> playerViewer = register(new Setting("PlayerViewer", false));
    public Setting<Integer> playerViewerX = register(new Setting("PlayerX", 752, 0, 1000, v -> playerViewer.getValue()));
    public Setting<Integer> playerViewerY = register(new Setting("PlayerY", 497, 0, 1000, v -> playerViewer.getValue()));
    public Setting<Float> playerScale = register(new Setting("PlayerScale", 1.0f, 0.1f, 2.0f, v -> playerViewer.getValue()));
    private int color;

    private boolean shouldIncrement;
    private int hitMarkerTimer;
    public Map<Integer, Integer> colorHeightMap = new HashMap<>();

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static HUD getInstance() {
        if (INSTANCE == null)
            INSTANCE = new HUD();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onUpdate() {
        if (this.shouldIncrement)
            this.hitMarkerTimer++;
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
        if (this.timer.passedMs(Management.getInstance().trUpdates.getValue())) {
            this.players = this.getTextRadarPlayers();
            this.timer.reset();
        }
    }

    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck())
            return;
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA((Colors.getInstance()).red.getValue(), (Colors.getInstance()).green.getValue(), (Colors.getInstance()).blue.getValue().intValue());
        int[] counter1 = {1};
        int j = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.waterMark.getValue()) {
            String string = this.command.getPlannedValue() + ChatFormatting.WHITE + " - " + MeowHook.MODVER;
            if ((Colors.getInstance()).rainbow.getValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0F, this.waterMarkY.getValue().intValue(), ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = string.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0F + f, this.waterMarkY.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(string, 2.0F, this.waterMarkY.getValue().intValue(), this.color, true);
            }
        }
        if (this.textRadar.getValue().booleanValue()) {
            this.drawTextRadar(ToolTips.getInstance().isOff() || ToolTips.getInstance().shulkerSpy.getValue() == false || ToolTips.getInstance().render.getValue() == false ? 0 : ToolTips.getInstance().getTextRadarY());
        }
        if (this.arrayList.getValue().booleanValue())
            if (this.renderingUp.getValue().booleanValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < MeowHook.moduleManager.sortedModulesABC.size(); k++) {
                        String str = MeowHook.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (2 + j * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                        j++;
                        counter1[0] = counter1[0] + 1;
                    }
                } else {
                    for (int k = 0; k < MeowHook.moduleManager.sortedModules.size(); k++) {
                        Module module = MeowHook.moduleManager.sortedModules.get(k);
                        String str = module.getDisplayName() + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.RESET + "]") : "");
                        this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (2 + j * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                        j++;
                        counter1[0] = counter1[0] + 1;
                    }
                }
            } else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < MeowHook.moduleManager.sortedModulesABC.size(); k++) {
                    String str = MeowHook.moduleManager.sortedModulesABC.get(k);
                    j += 12;
                    this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (height - j), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                for (int k = 0; k < MeowHook.moduleManager.sortedModules.size(); k++) {
                    Module module = MeowHook.moduleManager.sortedModules.get(k);
                    String str = module.getDisplayName() + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.RESET + "]") : "");
                    j += 12;
                    this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (height - j), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        String grayString = this.grayNess.getValue().booleanValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && this.renderingUp.getValue().booleanValue()) ? 13 : (this.renderingUp.getValue().booleanValue() ? -2 : 0);
        if (this.renderingUp.getValue().booleanValue()) {
            if (this.potions.getValue().booleanValue()) {
                List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = MeowHook.potionManager.getColoredPotionString(potionEffect);
                    i += 10;
                    this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                String str = grayString + "Speed: " + ChatFormatting.WHITE + MeowHook.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                String str = grayString + "Time: " + ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());
                i += 10;
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                String str = grayString + "TPS: " + ChatFormatting.WHITE + MeowHook.serverManager.getTPS();
                i += 10;
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            String fpsText = grayString + "FPS: " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping: " + ChatFormatting.WHITE + MeowHook.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    i += 10;
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (height - 2 - i), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (height - 2 - i), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (height - 2 - i), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    i += 10;
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (height - 2 - i), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            if (this.potions.getValue().booleanValue()) {
                List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = MeowHook.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                String str = grayString + "Speed: " + ChatFormatting.WHITE + MeowHook.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                String str = grayString + "Time: " + ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                String str = grayString + "TPS: " + ChatFormatting.WHITE + MeowHook.serverManager.getTPS();
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            String fpsText = grayString + "FPS: " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping: " + ChatFormatting.WHITE + MeowHook.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (2 + i++ * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (2 + i++ * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (2 + i++ * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (2 + i++ * 10), (Colors.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        boolean inHell = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell");
        int posX = (int) mc.player.posX;
        int posY = (int) mc.player.posY;
        int posZ = (int) mc.player.posZ;
        float nether = !inHell ? 0.125F : 8.0F;
        int hposX = (int) (mc.player.posX * nether);
        int hposZ = (int) (mc.player.posZ * nether);
        i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0;
        String coordinates = (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "] " + (this.direction.getValue().booleanValue() ? MeowHook.rotationManager.getDirection4D(false) : "")));
        String coords = this.coords.getValue().booleanValue() ? coordinates : "";
        i += 10;
        if ((Colors.getInstance()).rainbow.getValue().booleanValue()) {
            String rainbowCoords = this.coords.getValue().booleanValue() ? ((inHell ? (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]" + (this.direction.getValue().booleanValue() ? MeowHook.rotationManager.getDirection4D(false) : "")))) : "";
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(rainbowCoords, 2.0F, (height - i), ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
            } else {
                int[] counter2 = {1};
                float s = 0.0F;
                int[] counter3 = {1};
                char[] stringToCharArray2 = rainbowCoords.toCharArray();
                float u = 0.0F;
                for (char c : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c), 2.0F + u, (height - i), ColorUtil.rainbow(counter3[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c));
                    counter3[0] = counter3[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(coords, 2.0F, (height - i), this.color, true);
        }
        if (this.armor.getValue().booleanValue())
            renderArmorHUD(true);
        if (this.totems.getValue())
            renderTotemHUD();
        if (this.gapples.getValue())
            renderGappleHUD();
        if (this.crystals.getValue())
            renderCrystalHUD();
        if (this.greetermode.getValue() != GreeterMode.None)
            renderGreeter();
        if (this.lag.getValue().booleanValue())
            renderLag();
        if(playerViewer.getValue()) {
            drawPlayer();
        }

        if(compass.getValue() != Compass.NONE) {
            drawCompass();
        }

        if(holeHud.getValue()) {
            drawOverlay(event.partialTicks);
        }

        if(inventory.getValue()) {
            renderInventory();
        }
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderGreeter() {
        int width = this.renderer.scaledWidth;
        String text = "";
        if (this.greetermode.getValue() == GreeterMode.Swag) {
            text = text + "looking good today " + mc.player.getDisplayNameString() + ", welcome to " + this.command.getPlannedValue() + " >:3";
        } else if (this.greetermode.getValue() == GreeterMode.Nazi) {
            text = text + "Sieg heil " + mc.player.getDisplayNameString() + "! Wilkommen bei " + this.command.getPlannedValue() + " >:3";
        } else if (this.greetermode.getValue() == GreeterMode.Uwu) {
            text = text + "gweetings " + mc.player.getDisplayNameString() + ", welcome to " + this.command.getPlannedValue() + " uwu";
        } else if (this.greetermode.getValue() == GreeterMode.Cat) {
            text = text + "meow " + mc.player.getDisplayNameString() + ", welcome to " + this.command.getPlannedValue() + " :3";
        } else if (this.greetermode.getValue() == GreeterMode.Custom) {
            text = text + (this.greeterText.getValue()).replaceAll("<player>", mc.player.getDisplayNameString()).replaceAll("<client>", this.command.getPlannedValue());
        } else if (this.greetermode.getValue() == GreeterMode.None) {
            text = text + "";
        }
        if ((Colors.getInstance()).rainbow.getValue().booleanValue()) {
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, ColorUtil.rainbow((Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
            } else {
                int[] counter1 = {1};
                char[] stringToCharArray = text.toCharArray();
                float i = 0.0F;
                for (char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F + i, 2.0F, ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    i += this.renderer.getStringWidth(String.valueOf(c));
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, this.color, true);
        }
    }


    public void renderLag() {
        int width = this.renderer.scaledWidth;
        if (MeowHook.serverManager.isServerNotResponding()) {
            String text = ChatFormatting.RED + "Server lagging for " + MathUtil.round((float) MeowHook.serverManager.serverRespondingTime() / 1000.0F, 1) + "s";
            this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 40.0F, this.color, true);
        }
    }

    public void renderTotemHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int[] counter1 = {1};
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
            totems += mc.player.getHeldItemOffhand().getCount();
        if (totems > 0) {
            int i = width / 2;
            int iteration = 0;
            int y = this.totemsY.getValue();
            int x = this.totemsX.getValue();
            if (this.totems.getValue()) {
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                RenderUtil.itemRender.zLevel = 200.0F;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(totem, x, y);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, totem, x, y, "");
                RenderUtil.itemRender.zLevel = 0.0F;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                this.renderer.drawString(totems + "", (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (y + 9), ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                counter1[0] = counter1[0] + 1;
                GlStateManager.enableDepth();
                GlStateManager.disableLighting();
            }
        }
    }

    public void renderGappleHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int[] counter1 = {1};
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.GOLDEN_APPLE)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE)
            totems += mc.player.getHeldItemOffhand().getCount();
        if (totems > 0) {
            int i = width / 2;
            int iteration = 0;
            int y = this.gapplesY.getValue();
            int x = this.gapplesX.getValue();
            if (this.totems.getValue()) {
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                RenderUtil.itemRender.zLevel = 200.0F;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(gapple, x, y);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, gapple, x, y, "");
                RenderUtil.itemRender.zLevel = 0.0F;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                this.renderer.drawString(totems + "", (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (y + 9), ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                counter1[0] = counter1[0] + 1;
                GlStateManager.enableDepth();
                GlStateManager.disableLighting();
            }
        }
    }

    public void renderCrystalHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int[] counter1 = {1};
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.END_CRYSTAL)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
            totems += mc.player.getHeldItemOffhand().getCount();
        if (totems > 0) {
            int i = width / 2;
            int iteration = 0;
            int y = this.crystalsY.getValue();
            int x = this.crystalsX.getValue();
            if (this.totems.getValue()) {
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                RenderUtil.itemRender.zLevel = 200.0F;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(crystal, x, y);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, crystal, x, y, "");
                RenderUtil.itemRender.zLevel = 0.0F;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                this.renderer.drawString(totems + "", (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (y + 9), ColorUtil.rainbow(counter1[0] * (Colors.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                counter1[0] = counter1[0] + 1;
                GlStateManager.enableDepth();
                GlStateManager.disableLighting();
            }
        }
    }

    public void renderArmorHUD(boolean percent) {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
        for (ItemStack is : HUD.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
            if (!percent) continue;
            int dmg = 0;
            int itemDurability = is.getMaxDamage() - is.getItemDamage();
            float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
            float red = 1.0f - green;
            dmg = percent ? 100 - (int) (red * 100.0f) : itemDurability;
            this.renderer.drawStringWithShadow(dmg + "", x + 8 - this.renderer.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int) (red * 255.0f), (int) (green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(AttackEntityEvent event) {
        this.shouldIncrement = true;
    }

    public void onLoad() {
        MeowHook.commandManager.setClientMessage(getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 &&
                equals(event.getSetting().getFeature()))
            MeowHook.commandManager.setClientMessage(getCommandMessage());
    }

    public String getCommandMessage ( ) {
        if ( this.rainbowPrefix.getPlannedValue ( ) ) {
            StringBuilder stringBuilder = new StringBuilder ( this.getRawCommandMessage ( ) );
            stringBuilder.insert ( 0 , "\u00a7+" );
            stringBuilder.append ( "\u00a7r" );
            return stringBuilder.toString ( );
        }
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command.getPlannedValue(), this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue() , this.bracketColor.getPlannedValue());
    }

    public
    String getRainbowCommandMessage ( ) {
        StringBuilder stringBuilder = new StringBuilder ( this.getRawCommandMessage ( ) );
        stringBuilder.insert ( 0 , "\u00a7+" );
        stringBuilder.append ( "\u00a7r" );
        return stringBuilder.toString ( );
    }

    public
    String getRawCommandMessage ( ) {
        return this.commandBracket.getValue ( ) + this.command.getValue ( ) + this.commandBracket2.getValue ( );
    }

    public void drawTextRadar(int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (Map.Entry<String, Integer> player : this.players.entrySet()) {
                String text = player.getKey() + " ";
                int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0F, y, this.color, true);
                y += textheight;
            }
        }
    }

    public void drawCompass() {
        final ScaledResolution sr = new ScaledResolution(Util.mc);
        if(compass.getValue() == Compass.LINE) {
            float playerYaw = Util.mc.player.rotationYaw;
            float rotationYaw = MathUtil.wrap(playerYaw);
            RenderUtil.drawRect(compassX.getValue(), compassY.getValue(), compassX.getValue() + 100, compassY.getValue() + renderer.getFontHeight(), 0x75101010);
            RenderUtil.glScissor(compassX.getValue(), compassY.getValue(), compassX.getValue() + 100, compassY.getValue() + renderer.getFontHeight(), sr);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            final float zeroZeroYaw = MathUtil.wrap((float) (Math.atan2(0 - Util.mc.player.posZ, 0 - Util.mc.player.posX) * 180.0d / Math.PI) - 90.0f);
            RenderUtil.drawLine(compassX.getValue() - rotationYaw + (100 / 2) + zeroZeroYaw, compassY.getValue() + 2, compassX.getValue() - rotationYaw + (100 / 2) + zeroZeroYaw, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFF1010);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) + 45, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) + 45, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) - 45, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) - 45, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) + 135, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) + 135, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() - rotationYaw + (100 / 2)) - 135, compassY.getValue() + 2, (compassX.getValue() - rotationYaw + (100 / 2)) - 135, compassY.getValue() + renderer.getFontHeight() - 2, 2, 0xFFFFFFFF);
            renderer.drawStringWithShadow("n", (compassX.getValue() - rotationYaw + (100 / 2)) + 180 - renderer.getStringWidth("n") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("n", (compassX.getValue() - rotationYaw + (100 / 2)) - 180 - renderer.getStringWidth("n") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("e", (compassX.getValue() - rotationYaw + (100 / 2)) - 90 - renderer.getStringWidth("e") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("s", (compassX.getValue() - rotationYaw + (100 / 2)) - renderer.getStringWidth("s") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            renderer.drawStringWithShadow("w", (compassX.getValue() - rotationYaw + (100 / 2)) + 90 - renderer.getStringWidth("w") / 2.0f, compassY.getValue(), 0xFFFFFFFF);
            RenderUtil.drawLine((compassX.getValue() + 100 / 2), compassY.getValue() + 1, (compassX.getValue() + 100 / 2), compassY.getValue() + renderer.getFontHeight() - 1, 2, 0xFF909090);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            final double centerX = compassX.getValue();
            final double centerY = compassY.getValue();
            for (Direction dir : Direction.values()) {
                double rad = getPosOnCompass(dir);
                renderer.drawStringWithShadow(dir.name(), (float) (centerX + getX(rad)), (float) (centerY + getY(rad)), dir == Direction.N ? 0xFFFF0000 : 0xFFFFFFFF);
            }
        }
    }

    public void drawPlayer(EntityPlayer player, int x, int y) {
        final EntityPlayer ent = player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(playerViewerX.getValue() + 25, playerViewerY.getValue() + 25, 50.0f);
        GlStateManager.scale(-50.0f * playerScale.getValue(), 50.0f * playerScale.getValue(), 50.0f * playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Util.mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        } catch(Exception ignored) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public void drawPlayer() {
        final EntityPlayer ent = Util.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(playerViewerX.getValue() + 25, playerViewerY.getValue() + 25, 50.0f);
        GlStateManager.scale(-50.0f * playerScale.getValue(), 50.0f * playerScale.getValue(), 50.0f * playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Util.mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        } catch(Exception ignored) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    private double getX(double rad) {
        return Math.sin(rad) * (scale.getValue() * 10);
    }

    private double getY(double rad) {
        final double epicPitch = MathHelper.clamp(Util.mc.player.rotationPitch + 30f, -90f, 90f);
        final double pitchRadians = Math.toRadians(epicPitch); // player pitch
        return Math.cos(rad) * Math.sin(pitchRadians) * (scale.getValue() * 10);
    }

    private enum Direction {
        N,
        W,
        S,
        E
    }

    private static double getPosOnCompass(Direction dir) {
        double yaw = Math.toRadians(MathHelper.wrapDegrees(Util.mc.player.rotationYaw));
        int index = dir.ordinal();
        return yaw + (index * HALF_PI);
    }

    public enum Compass {
        NONE,
        CIRCLE,
        LINE
    }

    public void drawOverlay(float partialTicks) {
        float yaw = 0;
        final int dir = (MathHelper.floor((double) (Util.mc.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);

        switch (dir) {
            case 1:
                yaw = 90;
                break;
            case 2:
                yaw = -180;
                break;
            case 3:
                yaw = -90;
                break;
            default:
        }

        final BlockPos northPos = traceToBlock(partialTicks, yaw);
        final Block north = getBlock(northPos);
        if (north != null && north != Blocks.AIR) {
            final int damage = getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue() + 16, holeY.getValue(), holeX.getValue() + 32, holeY.getValue() + 16, 0x60ff0000);
            }
            drawBlock(north, holeX.getValue() + 16, holeY.getValue());
        }

        final BlockPos southPos = traceToBlock(partialTicks, yaw - 180.0f);
        final Block south = getBlock(southPos);
        if (south != null && south != Blocks.AIR) {
            final int damage = getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue() + 16, holeY.getValue() + 32, holeX.getValue() + 32, holeY.getValue() + 48, 0x60ff0000);
            }
            drawBlock(south, holeX.getValue() + 16, holeY.getValue() + 32);
        }

        final BlockPos eastPos = traceToBlock(partialTicks, yaw + 90.0f);
        final Block east = getBlock(eastPos);
        if (east != null && east != Blocks.AIR) {
            final int damage = getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue() + 32, holeY.getValue() + 16, holeX.getValue() + 48, holeY.getValue() + 32, 0x60ff0000);
            }
            drawBlock(east, holeX.getValue() + 32, holeY.getValue() + 16);
        }

        final BlockPos westPos = traceToBlock(partialTicks, yaw - 90.0f);
        final Block west = getBlock(westPos);
        if (west != null && west != Blocks.AIR) {
            final int damage = getBlockDamage(westPos);

            if (damage != 0) {
                RenderUtil.drawRect(holeX.getValue(), holeY.getValue() + 16, holeX.getValue() + 16, holeY.getValue() + 32, 0x60ff0000);
            }
            drawBlock(west, holeX.getValue(), holeY.getValue() + 16);
        }
    }

    public void drawOverlay(float partialTicks, Entity player, int x, int y) {
        float yaw = 0;
        final int dir = (MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);

        switch (dir) {
            case 1:
                yaw = 90;
                break;
            case 2:
                yaw = -180;
                break;
            case 3:
                yaw = -90;
                break;
            default:
        }

        final BlockPos northPos = traceToBlock(partialTicks, yaw, player);
        final Block north = getBlock(northPos);
        if (north != null && north != Blocks.AIR) {
            final int damage = getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y, x + 32, y + 16, 0x60ff0000);
            }
            drawBlock(north, x + 16, y);
        }

        final BlockPos southPos = traceToBlock(partialTicks, yaw - 180.0f, player);
        final Block south = getBlock(southPos);
        if (south != null && south != Blocks.AIR) {
            final int damage = getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y + 32, x + 32, y + 48, 0x60ff0000);
            }
            drawBlock(south, x + 16, y + 32);
        }

        final BlockPos eastPos = traceToBlock(partialTicks, yaw + 90.0f, player);
        final Block east = getBlock(eastPos);
        if (east != null && east != Blocks.AIR) {
            final int damage = getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 32, y + 16, x + 48, y + 32, 0x60ff0000);
            }
            drawBlock(east, x + 32, y + 16);
        }

        final BlockPos westPos = traceToBlock(partialTicks, yaw - 90.0f, player);
        final Block west = getBlock(westPos);
        if (west != null && west != Blocks.AIR) {
            final int damage = getBlockDamage(westPos);

            if (damage != 0) {
                RenderUtil.drawRect(x, y + 16, x + 16, y + 32, 0x60ff0000);
            }
            drawBlock(west, x, y + 16);
        }
    }

    private int getBlockDamage(BlockPos pos) {
        for (DestroyBlockProgress destBlockProgress : Util.mc.renderGlobal.damagedBlocks.values()) {
            if (destBlockProgress.getPosition().getX() == pos.getX() && destBlockProgress.getPosition().getY() == pos.getY() && destBlockProgress.getPosition().getZ() == pos.getZ()) {
                return destBlockProgress.getPartialBlockDamage();
            }
        }
        return 0;
    }

    private BlockPos traceToBlock(float partialTicks, float yaw) {
        final Vec3d pos = EntityUtil.interpolateEntity(Util.mc.player, partialTicks);
        final Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }

    private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
        final Vec3d pos = EntityUtil.interpolateEntity(player, partialTicks);
        final Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }

    private Block getBlock(BlockPos pos) {
        final Block block = Util.mc.world.getBlockState(pos).getBlock();
        if ((block == Blocks.BEDROCK) || (block == Blocks.OBSIDIAN)) {
            return block;
        }
        return Blocks.AIR;
    }

    private void drawBlock(Block block, float x, float y) {
        final ItemStack stack = new ItemStack(block);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate(x, y, 0);
        Util.mc.getRenderItem().zLevel = 501;
        Util.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        Util.mc.getRenderItem().zLevel = 0.f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public void renderInventory() {
        if (invBackground.getValue()) {
            boxrender(invX.getValue() + fineinvX.getValue(), invY.getValue() + fineinvY.getValue());
        }
        itemrender(Util.mc.player.inventory.mainInventory, invX.getValue() + fineinvX.getValue(), invY.getValue() + fineinvY.getValue());
    }

    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
        GlStateManager.color(255, 255, 255, 255);
    }

    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }

    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    private void boxrender(final int x, final int y) {
        preboxrender();
        Util.mc.renderEngine.bindTexture(box);
        RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + invH.getValue(), 500);
        RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
        postboxrender();
    }

    private void itemrender(final NonNullList<ItemStack> items, final int x, final int y) {
        for (int i = 0; i < items.size() - 9; i++) {
            int iX = x + (i % 9) * (18) + 8;
            int iY = y + (i / 9) * (18) + 18;
            ItemStack itemStack = items.get(i + 9);
            preitemrender();
            Util.mc.getRenderItem().zLevel = 501;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(Util.mc.fontRenderer, itemStack, iX, iY, null);
            Util.mc.getRenderItem().zLevel = 0.f;
            postitemrender();
        }

        if(renderXCarry.getValue()) {
            for (int i = 1; i < 5; i++) {
                int iX = x + ((i + 4) % 9) * (18) + 8;
                ItemStack itemStack = Util.mc.player.inventoryContainer.inventorySlots.get(i).getStack();
                if(itemStack != null && !itemStack.isEmpty) {
                    preitemrender();
                    Util.mc.getRenderItem().zLevel = 501;
                    RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, y + 1);
                    RenderUtil.itemRender.renderItemOverlayIntoGUI(Util.mc.fontRenderer, itemStack, iX, y + 1, null);
                    Util.mc.getRenderItem().zLevel = 0.f;
                    postitemrender();
                }
            }
        }
        /*
        for(int size = items.size(), item = 9; item < size; ++item) {
            final int slotx = x + 1 + item % 9 * 18;
            final int sloty = y + 1 + (item / 9 - 1) * 18;
            preitemrender();
            mc.getRenderItem().renderItemAndEffectIntoGUI(items.get(item), slotx, sloty);
            mc.getRenderItem().renderItemOverlays(mc.fontRenderer, items.get(item), slotx, sloty);
            postitemrender();
        }*/
    }

    public static void drawCompleteImage(int posX, int posY, int width, int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0F);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3f(0.0F, 0.0F, 0.0F);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3f(0.0F, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(width, height, 0.0F);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3f(width, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopMatrix();
    }


    public enum RenderingMode {
        Length, ABC
    }
    public enum GreeterMode {
        Nazi, Swag, Uwu, Cat, Custom, None
    }
}
