package eu.avalonya.api.models.dao;

import eu.avalonya.api.models.AvalonyaPlayer;
import eu.avalonya.api.models.AvalonyaDatabase;
import eu.avalonya.api.models.Rank;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.sql.Timestamp;

public class PlayerAvalonyaDao
{

    public static AvalonyaPlayer addPlayer(Player p) throws SQLException
    {
        AvalonyaPlayer player = new AvalonyaPlayer();
        player.setUuid(p.getUniqueId().toString());
        player.setPseudo(p.getName());
        player.setRankId(0);
        player.setLastLogin(new Timestamp(System.currentTimeMillis()));
        player.setFirstLogin(new Timestamp(System.currentTimeMillis()));
        player.setLastIp(p.getAddress().getAddress().getHostAddress());
        AvalonyaDatabase.getPlayerDao().create(player);
        return player;
    }

    public static void setRankId(Player p, int rank) throws SQLException
    {
        AvalonyaPlayer player = getPlayer(p);
        player.setRankId(rank);
        AvalonyaDatabase.getPlayerDao().update(player);
    }

    public static boolean playerExists(Player p) throws SQLException
    {
        return AvalonyaDatabase.getPlayerDao().idExists(p.getUniqueId().toString());
    }

    public static AvalonyaPlayer getPlayer(Player p) throws SQLException
    {
        AvalonyaPlayer player = AvalonyaDatabase.getPlayerDao().queryForId(p.getUniqueId().toString());
        player.setPlayer(p);
        player.setRank(Rank.rankIdToRank.get(player.getRankId()));
        return player;
    }

}
