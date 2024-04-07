package eu.avalonya.api.models.dao;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.models.AvalonyaDatabase;
import eu.avalonya.api.models.Citizen;
import eu.avalonya.api.models.Mariage;

import java.sql.SQLException;
import java.util.Date;

public class MariageDao
{

    public static Mariage create(Citizen sender, Citizen receiver)
    {
        Mariage mariage = new Mariage();
        mariage.setSender(sender);
        mariage.setReceiver(receiver);
        mariage.setCreatedAt(new Date());
        return mariage;
    }

    public boolean isAlreadyMarried(Citizen player)
    {
        Mariage m = null;
        // Coquin
        try
        {
            m = AvalonyaDatabase.getMariageDao().queryBuilder()
                    .where().eq("sender_id", player)
                    .or().eq("receiver_id", player)
                    .queryForFirst();
        } catch (SQLException e)
        {
            AvalonyaAPI.getInstance().getLogger().severe("...");
        }
        return m == null;
    }

}
