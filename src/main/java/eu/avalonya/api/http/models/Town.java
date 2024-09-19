package eu.avalonya.api.http.models;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Town extends AbstractModel {

    private int id;
    private String name;
    private String spawnLocation;

    @Unmodifiable
    public List<Citizen> getCitizens() {
        Endpoint endpoint = Endpoint.bind(Endpoint.CITIZENS, this.getName());

        return Backend.get(endpoint, null).toList(Citizen.class);
    }

    public void addCitizen(Citizen citizen) {
        Endpoint endpoint = Endpoint.bind(Endpoint.CITIZENS, this.getName());

        Backend.post(endpoint, AvalonyaAPI.getGson().toJson(citizen));
    }

    public static Town create(String name, Location location) {
        Map<String, Object> data = Map.of(
                "name", name,
                "spawnLocation", String.format("%f;%f;%f", location.getX(), location.getY(), location.getZ())
        );

        return Backend.post(Endpoint.TOWNS, AvalonyaAPI.getGson().toJson(data)).to(Town.class);
    }

    public static Town get(String name) {
        Endpoint endpoint = Endpoint.bind(Endpoint.TOWNS_NAME, name);

        return Backend.get(endpoint, null).to(Town.class);
    }

    public static List<Town> getAll() {
        return Backend.get(Endpoint.TOWNS, null).toList(Town.class);
    }

}
