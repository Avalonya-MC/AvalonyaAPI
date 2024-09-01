package eu.avalonya.api.http.models;

import com.google.gson.Gson;
import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.http.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractModel<T extends AbstractModel<?>> {

    public abstract Endpoint getEndpoint();

    public static <T extends AbstractModel<?>> T create(Class<T> clazz, Map<String, ?> data) {
        String body = AvalonyaAPI.getGson().toJson(data);

        try {
            Endpoint endpoint = clazz.getConstructor().newInstance().getEndpoint();
            Response response = Backend.post(endpoint, body);

            if (response.getStatus() != 200) {
                return null;
            }

            return AvalonyaAPI.getGson().fromJson(response.getBody(), clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return null;
        }
    }

    public static <T extends AbstractModel<?>> T find(Class<T> clazz, String id) {
        try {
            Endpoint endpoint = clazz.getConstructor().newInstance().getEndpoint();
            Response response = Backend.get(Endpoint.bind(endpoint, id), "");

            if (response.getStatus() != 200) {
                return null;
            }

            return AvalonyaAPI.getGson().fromJson(response.getBody(), clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return null;
        }
    }

}
