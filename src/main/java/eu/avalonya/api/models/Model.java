package eu.avalonya.api.models;

import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import java.util.Map;

public abstract class Model<T extends Model<?>> {

    public abstract Endpoint getEndpoint();

    public static <T extends Model<?>> T create(Class<T> clazz, Map<String, Object> data) {
        String body = "{" + data.entrySet().stream()
            .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
            .reduce((a, b) -> a + "," + b).orElse("") + "}";

        String response = Backend.post(clazz.cast(null).getEndpoint(), body);

        return null; // TODO: Create instance of T from response
    }

    public static <T extends Model<?>> T find(Class<T> clazz, int id) {
        // TODO: Implement this method
        return null;
    }

    public void save() {
        Backend.put(getEndpoint(), "{}"); // TODO: Implement this method
    }

}
