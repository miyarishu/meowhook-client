package meow.miyarishu.meowhook.features.command.commands;

import meow.miyarishu.meowhook.MeowHook;
import com.mojang.realmsclient.gui.ChatFormatting;
import meow.miyarishu.meowhook.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : MeowHook.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.WHITE + MeowHook.commandManager.getPrefix() + command.getName());
        }
    }
}

