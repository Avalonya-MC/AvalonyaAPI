package eu.avalonya.api.command;

import eu.avalonya.api.exceptions.CitizenAlreadyHasTownException;
import eu.avalonya.api.models.dao.TownDao;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Blabla extends BaseCommand<Player>{

    public Blabla() {
        super("blabla");
    }

    @Override
    public void run(Player sender, ArgumentCollection args) {
        try {
            TownDao.create("Daweii-Town", sender);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CitizenAlreadyHasTownException e) {
            throw new RuntimeException(e);
        }
    }
}
