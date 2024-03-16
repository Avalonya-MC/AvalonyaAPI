package eu.avalonya.api;

import eu.avalonya.api.command.ArgumentCollection;
import eu.avalonya.api.command.BaseCommand;
import eu.avalonya.api.exceptions.CitizenAlreadyHasTownException;
import eu.avalonya.api.models.Citizen;
import eu.avalonya.api.models.dao.CitizenDao;
import eu.avalonya.api.models.dao.TownDao;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class TestCommand extends BaseCommand<Player>
{
    public TestCommand()
    {
        super("test");
    }

    @Override
    public void run(Player sender, ArgumentCollection args)
    {
        try
        {
            if (args.getRest().isEmpty())
            {
                Citizen citizen = CitizenDao.find(sender);

                sender.sendMessage("Mayor: " + citizen.getTown().getMayor().getPlayer().getName());
            }
            else
            {
                TownDao.create(args.getRest().get(0), sender);
                sender.sendMessage("§aLa ville a été créée avec succès !");
            }
        }
        catch (SQLException | CitizenAlreadyHasTownException e)
        {
            e.printStackTrace();
        }
    }
}
