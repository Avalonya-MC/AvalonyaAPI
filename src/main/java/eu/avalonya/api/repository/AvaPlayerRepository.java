package eu.avalonya.api.repository;

import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.models.AvaPlayer;

import java.util.List;
import java.util.Map;

public class AvaPlayerRepository extends AbstractRepository<AvaPlayer> {

    public AvaPlayerRepository(List<String> vars) {
        super(AvaPlayer.class, vars);
    }

    @Override
    public Map<String, Endpoint> getEndpoints() {
        return Map.of(
                "all", Endpoint.PLAYERS,
                "get", Endpoint.PLAYERS_ID,
                "create", Endpoint.PLAYER,
                "update", Endpoint.PLAYERS_ID,
                "delete", Endpoint.PLAYERS_ID
        );
    }

    public static AvaPlayer findById(String id) {
        if (!cache.containsKey(AvaPlayer.class.getSimpleName())) {
            return null;
        }

        return (AvaPlayer) cache.get(AvaPlayer.class.getSimpleName()).stream().filter(
                abstractModel -> abstractModel.getId().value().equals(id)
        ).findFirst().orElse(null);
    }
}
