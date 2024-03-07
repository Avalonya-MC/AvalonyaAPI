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
        Town town = new Town(name);
        Citizen mayor = CitizenDao.getCitizen(player);

        town.setSpawnLocation(player.getLocation().toString());
        AvalonyaDatabase.getTownDao().create(town);

        if(mayor == null)
        {
            CitizenDao.create(player, town);
        }

        return town;
    }

    public static Town getTown(String name) throws SQLException
    {
        Town town = AvalonyaDatabase.getTownDao().queryBuilder().where().eq("name", name).queryForFirst();

        if (town == null)
        {
            return null;
        }

        return town;
    }

    public static void update(Town town) throws SQLException
    {
        AvalonyaDatabase.getTownDao().update(town);
    }

    public static void delete(Town town) throws SQLException
    {
        AvalonyaDatabase.getTownDao().delete(town);
    }

    public static void delete(String name) throws SQLException
    {
        AvalonyaDatabase.getTownDao().delete(AvalonyaDatabase.getTownDao().queryBuilder().where().eq("name", name).queryForFirst());
    }

}
