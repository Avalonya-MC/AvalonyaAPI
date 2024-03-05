package eu.avalonya.api.models.dao;

import eu.avalonya.api.models.AvalonyaDatabase;
import eu.avalonya.api.models.Citizen;
import eu.avalonya.api.models.Town;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class CitizenDao
{

    public static Citizen create(Player pseudo)
    {
        // TODO : Create citizen in database
        return null;
    }

    public static Citizen getCitizen(Player pseudo)
    {
        // TODO : Get citizen from database
        return null;
    }

    public static List<Citizen> getCitizens(Town town) throws SQLException
    {
        return AvalonyaDatabase.getCitizenDao().queryForEq("town_id", town.getId());
    }

}
