package eu.avalonya.api.models.dao;

import eu.avalonya.api.models.AvalonyaDatabase;
import eu.avalonya.api.models.Citizen;
import eu.avalonya.api.models.Town;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class TownDao
{

    public static Town create(String name, Player player) throws SQLException
    {
        Citizen mayor = CitizenDao.getCitizen(player);

        if(mayor == null)
        {
            mayor = CitizenDao.create(player);
        }

        Town town = new Town(name, mayor);

        AvalonyaDatabase.getTownDao().create(town);
        return town;
    }

    public static Town getTown(String name) throws SQLException
    {
        return AvalonyaDatabase.getTownDao().queryBuilder().where().eq("name", name).queryForFirst();
    }

}
