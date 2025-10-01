package meow.miyarishu.meowhook.features.command;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.Feature;
import meow.miyarishu.meowhook.util.Util;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command
        extends Feature {
    protected String name;
    protected String[] commands;

    public Command(String name) {
        super(name);
        this.name = name;
        this.commands = new String[]{""};
    }

    public Command(String name, String[] commands) {
        super(name);
        this.name = name;
        this.commands = commands;
    }

    public static void sendMessage(String message) {
        Command.sendSilentMessage(MeowHook.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + message);
    }

    public static void sendTempMessage(String message) {
        Command.sendTempSilentMessage(MeowHook.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + message, -1337);
    }

    public static void sendTempMessageID(String message, int id) {
        Command.sendTempSilentMessage(MeowHook.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + message, id);
    }

    public static void sendTempSilentMessage(String message, int id) {
        if (Command.nullCheck()) {
            return;
        }
        Util.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatMessage(message), id);
    }

    public static void sendSilentMessage(String message) {
        if (Command.nullCheck()) {
            return;
        }
        Command.mc.player.sendMessage(new ChatMessage(message));
    }

    public static String getCommandPrefix() {
        return MeowHook.commandManager.getPrefix();
    }

    public abstract void execute(String[] var1);

    @Override
    public String getName() {
        return this.name;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public static class ChatMessage
            extends TextComponentBase {
        private final String text;

        public ChatMessage(String text) {
            Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher matcher = pattern.matcher(text);
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                String replacement = matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }

        public String getUnformattedComponentText() {
            return this.text;
        }

        public ITextComponent createCopy() {
            return null;
        }

        public ITextComponent shallowCopy() {
            return new ChatMessage(this.text);
        }
    }
}

