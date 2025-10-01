package meow.miyarishu.meowhook.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.command.Command;
import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

import java.util.*;

public class Notifications
        extends Module {

    private static Notifications INSTANCE = new Notifications();
    public Setting<toggleMode> toggleMsg = register(new Setting("Toggle Message", toggleMode.Normal));
    private final Setting<Boolean> visualRange = this.register(new Setting<Boolean>("Visual Range", true));
    private final Setting<Boolean> visualRangePublic = this.register(new Setting<Boolean>("Public-VR", false, v -> this.visualRange.getValue()));
    private Setting<Integer> visualRangePublicDelay = this.register(new Setting<Integer>("Public-VR Delay", 5000, 0, 20000, v -> this.visualRange.getValue()));

    private final Setting<Boolean> visualRangeSound = this.register(new Setting<Boolean>("Sound", true, v -> this.visualRange.getValue()));
    private final Setting<Boolean> popCounter = this.register(new Setting<Boolean>("Totem Pops", true));
    private final Setting<Boolean> showOwnPops = this.register(new Setting<Boolean>("Show Own Pops", false, v -> this.popCounter.getValue()));
    private final Setting<Boolean> pearlNotify = this.register(new Setting<Boolean>("Pearl Notify", true));
    private final Setting<Boolean> showOwnPearls = this.register(new Setting<Boolean>("Show Own Pearls", false, v -> this.pearlNotify.getValue()));
    private final meow.miyarishu.meowhook.util.Timer timerChat = new Timer();

    public Notifications() {
        super("Notifications", "Notifications", Category.CLIENT, true, false, false);
        this.setInstance();
    }

    private Entity enderPearl;
    private boolean flag;
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    private List<String> knownPlayers = new ArrayList<>();
    ;
    private Set<EntityPlayer> str = Collections.newSetFromMap(new WeakHashMap<>());

    public static Notifications getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notifications();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onToggle() {
        this.knownPlayers = new ArrayList<>();
        this.TotemPopContainer.clear();
    }

    @Override
    public void onEnable() {
        this.flag = true;
    }

    @Override
    public void onTick() {
        if (this.visualRange.getValue()) {
            List<String> tickPlayerList = new ArrayList<>();

            try {
                for (Entity entity : mc.world.getLoadedEntityList()) {
                    if (entity instanceof EntityPlayer) {
                        tickPlayerList.add(entity.getName());
                    }
                }
            } catch (Exception e) {

            }

            if (tickPlayerList.size() > 0) {
                for (String playerName : tickPlayerList) {
                    if (playerName.equals(mc.player.getName())) {
                        continue;
                    }
                    if (!knownPlayers.contains(playerName)) {
                        knownPlayers.add(playerName);
                        Command.sendTempMessageID((MeowHook.friendManager.isFriend(playerName) ? ChatFormatting.AQUA : ChatFormatting.RED) + playerName + Management.getInstance().mainColor.getValue() + " has entered your " + Management.getInstance().accentColor.getValue() + "visual range", -8043809);
                        if (this.visualRangeSound.getValue()) {
                            mc.player.playSound(SoundEvents.BLOCK_NOTE_PLING, 0.5f, 1.0f);
                        }
                        if (this.visualRangePublic.getValue() && this.timerChat.passedMs(this.visualRangePublicDelay.getValue())) {
                            mc.player.sendChatMessage((MeowHook.friendManager.isFriend(playerName) ? "My friend " : "") + playerName + " just entered my visual range thanks to meowhook!");
                            this.timerChat.reset();
                        }
                        return;
                    }
                }
            }

            if (knownPlayers.size() > 0) {
                for (String playerName : knownPlayers) {
                    if (!tickPlayerList.contains(playerName)) {
                        knownPlayers.remove(playerName);
                        return;
                    }
                }
            }
        }
    }

    public void onDeath(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            int l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.remove(player.getName());
            if (this.popCounter.getValue()) {
                Command.sendTempMessageID((MeowHook.friendManager.isFriend(player.getName()) ? ChatFormatting.AQUA : ChatFormatting.RED) + player.getName() + Management.getInstance().mainColor.getValue() + " died after popping their " + Management.getInstance().accentColor.getValue() + l_Count + getPopString(l_Count) + " totem", -42069);
            }
        }
    }

    public void onTotemPop(EntityPlayer player) {
        if (Notifications.fullNullCheck()) {
            return;
        }
        if (Notifications.mc.player.equals(player) && !showOwnPops.getValue()) {
            return;
        }
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (this.popCounter.getValue()) {
            Command.sendTempMessageID((MeowHook.friendManager.isFriend(player.getName()) ? ChatFormatting.AQUA : ChatFormatting.RED) + player.getName() + Management.getInstance().mainColor.getValue() + " popped their " + Management.getInstance().accentColor.getValue() + l_Count + getPopString(l_Count) + " totem", -1337);
        }
    }

    @Override
    public void onUpdate() {
        if (pearlNotify.getValue()) {
            if (Notifications.mc.world == null || Notifications.mc.player == null) {
                return;
            }
            this.enderPearl = null;
            for (final Entity e : Notifications.mc.world.loadedEntityList) {
                if (e instanceof EntityEnderPearl) {
                    this.enderPearl = e;
                    break;
                }
            }
            if (this.enderPearl == null) {
                this.flag = true;
                return;
            }
            EntityPlayer closestPlayer = null;
            for (final EntityPlayer entity : Notifications.mc.world.playerEntities) {
                if (closestPlayer == null) {
                    closestPlayer = entity;
                } else {
                    if (closestPlayer.getDistance(this.enderPearl) <= entity.getDistance(this.enderPearl)) {
                        continue;
                    }
                    closestPlayer = entity;
                }
            }
            if (closestPlayer == Notifications.mc.player && !this.showOwnPearls.getValue()) {
                this.flag = false;
            }
            if (closestPlayer != null && this.flag) {
                String faceing = this.enderPearl.getHorizontalFacing().toString();
                if (faceing.equals("west")) {
                    faceing = "east";
                } else if (faceing.equals("east")) {
                    faceing = "west";
                }
                Command.sendMessage(MeowHook.friendManager.isFriend(closestPlayer.getName()) ? (ChatFormatting.AQUA + closestPlayer.getName() + Management.getInstance().mainColor.getValue() + " threw a pearl going " + Management.getInstance().accentColor.getValue() + faceing) : (ChatFormatting.RED + closestPlayer.getName() + Management.getInstance().mainColor.getValue() + " threw a pearl going " + Management.getInstance().accentColor.getValue() + faceing));
                this.flag = false;
            }
        }
    }

    public enum toggleMode {
        Normal,
        ForgeHax,
        DotGod
    }

    public String getPopString(int pops) {
        if (pops == 1) {
            return "st";
        } else if (pops == 2) {
            return "nd";
        } else if (pops == 3) {
            return "rd";
        } else if (pops >= 4 && pops < 21) {
            return "th";
        } else {
            int lastDigit = pops % 10;
            if (lastDigit == 1) {
                return "st";
            } else if (lastDigit == 2) {
                return "nd";
            } else if (lastDigit == 3) {
                return "rd";
            } else {
                return "th";
            }
        }
    }
}