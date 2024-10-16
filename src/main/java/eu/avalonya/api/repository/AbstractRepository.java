package eu.avalonya.api.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.http.Response;
import eu.avalonya.api.models.AbstractModel;
import redis.clients.jedis.UnifiedJedis;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepository<T extends AbstractModel> {

    private final UnifiedJedis jedis;
    private final Class<T> clazz;
    private final List<String> vars;

    public AbstractRepository(final UnifiedJedis jedis, List<String> vars) {
        this.jedis = jedis;
        this.vars = vars;
        this.clazz = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }

    public List<T> all() {
        if (!getEndpoints().containsKey("all")) {
            return null; // TODO: Throw a exception
        }

        Endpoint endpoint = Endpoint.bind(getEndpoints().get("all"), this.vars);
        Response response = Backend.get(endpoint, null);

        return gson().fromJson(response.body(), new TypeToken<List<T>>() {}.getType());
    }

    public T get(final String id) {
        if (!getEndpoints().containsKey("get")) {
            return null; // TODO: Throw a exception
        }

        String model = jedis.get(clazz + id);

        if (model == null) {
            Endpoint endpoint = Endpoint.bind(getEndpoints().get("get"), this.vars);
            Response response = Backend.get(endpoint, null);

            jedis.set(clazz + id, response.body());
            return gson().fromJson(response.body(), clazz);
        }

        return gson().fromJson(model, clazz);
    }

    public T save(final T entity) {
        Map<String, String> params = entity.getRepositoryAttributes();
        String serialized = AvalonyaAPI.getGson().toJson(entity);

        jedis.set(entity.getClass() + entity.getId().value(), serialized);

        if (entity.isCreated()) {
            if (!getEndpoints().containsKey("update")) {
                // TODO: Throw a exception
            }

            params.put(entity.getId().key(), entity.getId().value());

            Endpoint endpoint = getEndpoints().get("update");
            endpoint.bindAssoc(params);

            Backend.put(endpoint, serialized);
        } else {
            if (!getEndpoints().containsKey("create")) {
                // TODO: Throw a exception
            }

            Endpoint endpoint = getEndpoints().get("create");
            endpoint.bindAssoc(params);

            Backend.post(endpoint, serialized);

            entity.setCreated(true);
        }

        return entity;
    }

    public void delete(final T entity) {
        if (!getEndpoints().containsKey("delete")) {
            // TODO: Throw a exception
        }

        Map<String, String> params = entity.getRepositoryAttributes();

        jedis.del(entity.getClass() + entity.getId().value());

        Endpoint endpoint = getEndpoints().get("create");
        endpoint.bindAssoc(params);

        Backend.delete(endpoint);
    }

    public abstract Map<String, Endpoint> getEndpoints();

    protected Gson gson() {
        return AvalonyaAPI.getGson();
    }
}
