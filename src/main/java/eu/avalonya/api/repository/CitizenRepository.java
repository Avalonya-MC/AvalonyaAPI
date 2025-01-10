package eu.avalonya.api.repository;

import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.models.towny.Citizen;

import java.util.List;
import java.util.Map;

public class CitizenRepository extends AbstractRepository<Citizen> {

    public CitizenRepository(List<String> vars) {
        super(Citizen.class, vars);
    }

    @Override
    public Map<String, Endpoint> getEndpoints() {
        return Map.of(
                "all", Endpoint.CITIZENS,
                "create", Endpoint.CITIZENS,
                "delete", Endpoint.CITIZENS_ID
        );
    }

    public static Citizen findById(String id) {
        if (!cache.containsKey(Citizen.class.getSimpleName())) {
            return null;
        }

        return (Citizen) cache.get(Citizen.class.getSimpleName()).stream().filter(
                abstractModel -> abstractModel.getId().value().equals(id)
        ).findFirst().orElse(null);
    }
}
