package eu.avalonya.api.models;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.http.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Model<T extends Model<?>> {

    public abstract Endpoint getEndpoint();

    public static <T extends Model<?>> T create(Class<T> clazz, Map<String, ?> data) {
        String body = "{" + data.entrySet().stream()
            .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
            .reduce((a, b) -> a + "," + b).orElse("") + "}";

        Response response = null;
        try {
            response = Backend.post(clazz.getConstructor().newInstance().getEndpoint(), body);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return AvalonyaAPI.getGson().fromJson(response.getBody(), clazz);
    }

    public static <T extends Model<?>> T find(Class<T> clazz, String id) {
        Endpoint endpoint = clazz.cast(null).getEndpoint();
        Response response = Backend.get(Endpoint.bind(endpoint, id), "");

        if (response.getStatus() != 200) {
            return null;
        }

        return AvalonyaAPI.getGson().fromJson(response.getBody(), clazz);
    }

    public void save() {
        Backend.put(getEndpoint(), AvalonyaAPI.getGson().toJson(this));
    }

}
