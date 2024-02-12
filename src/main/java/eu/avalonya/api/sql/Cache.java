package eu.avalonya.api.sql;

import eu.avalonya.api.models.AvalonyaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Cache
{
    public static HashMap<UUID, AvalonyaPlayer> avaloniaPlayers = new HashMap<UUID, AvalonyaPlayer>();
    public static ArrayList<String> staffList = new ArrayList<String>();
}
