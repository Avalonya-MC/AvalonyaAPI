package eu.avalonya.api.repository;

import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.models.Citizen;

import java.util.List;
import java.util.Map;

public class CitizenRepository extends AbstractRepository<Citizen> {

    public CitizenRepository(List<String> vars) {
        super(vars);
    }

    @Override
    public Map<String, Endpoint> getEndpoints() {
        return Map.of(
                "all", Endpoint.CITIZENS,
                "create", Endpoint.CITIZENS
        );
    }
}
