package eu.avalonya.api.models;

import org.bukkit.entity.Player;

import java.util.UUID;

public class AvalonyaPlayer
{

    private Player player;
    private UUID uuid;
    private Rank rank;

    public AvalonyaPlayer(Player player, UUID uuid, int rank)
    {
        this.player = player;
        this.uuid = uuid;
        this.rank = Rank.rankIdToRank.get(rank);
    }

    /*
     * Ajouter des fonctions genre getChatFormat qui retourne un string avec le bon format
     */

    public Player getPlayer()
    {
        return player;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public Rank getRank()
    {
        return rank;
    }

}
