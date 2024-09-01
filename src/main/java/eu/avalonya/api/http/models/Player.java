package eu.avalonya.api.http.models;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.models.Rank;
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
        return Endpoint.PLAYERS;
    }

    public Rank getRank() {
        return Rank.values()[this.rankId];
    }

    public void delete() {
        Endpoint endpoint = Endpoint.bind(Endpoint.PLAYERS_ID, this.uuid);
        Backend.delete(endpoint);
    }

    public void create() {
        Backend.post(Endpoint.PLAYER, AvalonyaAPI.getGson().toJson(this));
    }
}
