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
public class Player extends AbstractModel<Player> {

    private String uuid;
    private String pseudo;
    private int rankId;
    private long firstLogin;
    private String lastIp;
    private double money;

    @Override
    public Endpoint getEndpoint() {
        return Endpoint.PLAYER;
    }

    public Rank getRank() {
        return Rank.values()[this.rankId];
    }

    public void delete() {
        Endpoint endpoint = Endpoint.bind(Endpoint.PLAYERS_ID, this.uuid);
        Backend.delete(endpoint);
    }

    public static List<Player> getAll() {
        return Backend.get(Endpoint.PLAYERS, null).toList(Player.class);
    }

    public static Player create(org.bukkit.entity.Player player) {
        Map<String, Object> data = Map.of(
            "uuid", player.getUniqueId().toString(),
            "pseudo", player.getName(),
            "firstLogin", player.getFirstPlayed(),
            "lastIp", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress()
        );

        return Backend.post(Endpoint.PLAYERS, AvalonyaAPI.getGson().toJson(data)).to(Player.class);
    }
}
