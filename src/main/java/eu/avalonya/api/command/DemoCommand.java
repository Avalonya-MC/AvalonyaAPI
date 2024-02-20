package eu.avalonya.api.command;

import eu.avalonya.api.command.arguments.IntegerArgument;
import eu.avalonya.api.command.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class DemoCommand extends BaseCommand<Player>
{

    public DemoCommand()
    {
        super("demo");

        addSubCommand(BaseCommand.newSubCommand("sub", (sender, args) -> {
            sender.sendMessage("sub command");
        }));
        addArgument(PlayerArgument.class, true);
        addArgument(IntegerArgument.class, true);
    }

    @Override
    public void run(Player sender, ArgumentCollection args)
    {
        sender.sendMessage("oui");
        sender.sendMessage("Hello " + args.get(0, Player.class).getName());
        sender.sendMessage("You are " + args.get(1, Integer.class) + " years old");
    }
}
