package eu.avalonya.api.http.models;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.models.Rank;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends AbstractModel {

    private String uuid;
    private String pseudo;
    private int rankId;
    private long firstLogin;
    private String lastIp;
    private double money;

    public Rank getRank() {
        return Rank.values()[this.rankId];
    }

    public static Player create(org.bukkit.entity.Player player) {
        Map<String, Object> data = Map.of(
            "uuid", player.getUniqueId().toString(),
            "pseudo", player.getName(),
            "firstLogin", player.getFirstPlayed(),
            "lastIp", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress()
        );

        return Backend.post(Endpoint.PLAYER, AvalonyaAPI.getGson().toJson(data)).to(Player.class);
    }

    public static Player get(String uuid) {
        Endpoint endpoint = Endpoint.bind(Endpoint.PLAYERS, uuid);

        return Backend.get(endpoint, null).to(Player.class);
    }

    public static List<Player> getAll() {
        return Backend.get(Endpoint.PLAYERS, null).toList(Player.class);
    }
}
