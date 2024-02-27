package eu.avalonya.api.models.dao;

import eu.avalonya.api.models.APlayer;
import eu.avalonya.api.models.AvalonyaDatabase;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.sql.Timestamp;

public class PlayerDao
{

    public static APlayer addPlayer(Player p) throws SQLException
    {
        APlayer player = new APlayer();
        player.setUuid(p.getUniqueId().toString());
        player.setPseudo(p.getName());
        player.setRankId(0);
        player.setLastLogin(new Timestamp(System.currentTimeMillis()));
        player.setFirstLogin(new Timestamp(System.currentTimeMillis()));
        player.setLastIp("");
        AvalonyaDatabase.getPlayerDao().create(player);
        return player;
    }

    public static boolean playerExists(Player p) throws SQLException
    {
        return AvalonyaDatabase.getPlayerDao().idExists(p.getUniqueId().toString());
    }

    public static APlayer getPlayer(Player p) throws SQLException
    {
        return AvalonyaDatabase.getPlayerDao().queryForId(p.getUniqueId().toString());
    }

}
