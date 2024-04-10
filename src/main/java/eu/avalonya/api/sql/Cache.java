package eu.avalonya.api.sql;

import eu.avalonya.api.models.AvalonyaPlayer;
import eu.avalonya.api.models.Mariage;
import eu.avalonya.api.models.Rank;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Cache
{
    public static HashMap<UUID, AvalonyaPlayer> avaloniaPlayers = new HashMap<UUID, AvalonyaPlayer>();
    public static HashMap<Rank, List<String>> permissions = new HashMap<>();
    public static ArrayList<Mariage> mariagesRequest = new ArrayList<>();

    /**
     * Pour le moment on n'a pas besoin de stocker un AvalonyaPlayer, un Player spigot suffit.
     * Car on s'en sert uniquement pour envoyer un message, à voir dans le temps si on a besoin de plus.
     */
    public static ArrayList<Player> staffList = new ArrayList<>();


    public static boolean contains(Player sender, Player receiver)
    {
        for (Mariage m : mariagesRequest)
        {
            if (m.getSender().getPlayer().getPlayer().equals(sender) && m.getReceiver().getPlayer().getPlayer().equals(receiver))
                return true;
        }
        return false;
    }

    public static int find(Player sender, Player receiver)
    {
        int id = -1;
        for (int i = 0; i < mariagesRequest.size(); i++)
        {
            Mariage m = mariagesRequest.get(i);
            if (m.getSender().getPlayer().getPlayer() == sender
                    && m.getReceiver().getPlayer().getPlayer() == receiver)
            {
                id = i;
            }
        }
        return id;
    }

}
