package eu.avalonya.api.command;

import org.bukkit.command.CommandSender;

public class DemoCommand extends BaseCommand
{

    public DemoCommand()
    {
        addSubCommand("sub", this::subRun);
    }

    @Override
    public void run(CommandSender sender, SenderType senderType, String[] args)
    {
        sender.sendMessage("Hello, world!");
    }

    private void subRun(CommandSender sender, SenderType senderType, String[] args)
    {
        sender.sendMessage("Hello, sub world!");
    }

}
