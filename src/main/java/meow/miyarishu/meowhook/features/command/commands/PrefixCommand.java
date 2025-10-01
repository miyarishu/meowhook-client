package meow.miyarishu.meowhook.features.command.commands;

import meow.miyarishu.meowhook.MeowHook;
import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.features.command.Command;

public class PrefixCommand
        extends Command {
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.GREEN + "Current prefix is " + MeowHook.commandManager.getPrefix());
            return;
        }
        MeowHook.commandManager.setPrefix(commands[0]);
        Command.sendMessage("Prefix changed to " + ChatFormatting.GREEN + commands[0]);
    }
}

