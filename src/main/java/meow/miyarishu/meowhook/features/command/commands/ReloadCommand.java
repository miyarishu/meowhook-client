package meow.miyarishu.meowhook.features.command.commands;

import meow.miyarishu.meowhook.MeowHook;
import meow.miyarishu.meowhook.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        MeowHook.reload();
    }
}

