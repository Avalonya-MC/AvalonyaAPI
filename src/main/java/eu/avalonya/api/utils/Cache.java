package eu.avalonya.api.utils;

import eu.avalonya.api.models.AvaPlayer;
import eu.avalonya.api.models.Rank;

import java.util.*;

public class Cache {
    public static final HashMap<UUID, AvaPlayer> players = new HashMap<>();
    public static final HashMap<Rank, List<String>> permissions = new HashMap<>();
}
