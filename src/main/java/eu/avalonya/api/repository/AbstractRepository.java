package eu.avalonya.api.repository;

import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.models.AbstractModel;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;

public abstract class AbstractRepository<T extends AbstractModel> {

    private final UnifiedJedis jedis;

    public AbstractRepository(final UnifiedJedis jedis) {
        this.jedis = jedis;
    }

    public T get(final String id) {
        if (!getEndpoints().containsKey("get")) {
            return null; // TODO: Throw a exception
        }

        String result = jedis.get(getClass() + id); // getClass() != model class

        if (result == null) {
            return null;
        }

        return null; // TODO: Deserialize string
    }

    public T save(final T entity) {
        Map<String, String> params = entity.getRepositoryAttributes();
        String serialized = jsonSerialize(entity);

        jedis.set(entity.getClass() + entity.getId().value(), serialized);

        if (entity.isCreated()) {
            if (!getEndpoints().containsKey("update")) {
                // TODO: Throw a exception
            }

            params.put(entity.getId().key(), entity.getId().value());

            Endpoint endpoint = getEndpoints().get("update");
            endpoint.bindAssoc(params);

            Backend.put(endpoint, serialized);

            return entity;
        } else {
            if (!getEndpoints().containsKey("create")) {
                // TODO: Throw a exception
            }

            Endpoint endpoint = getEndpoints().get("create");
            endpoint.bindAssoc(params);

            Backend.post(endpoint, serialized);

            entity.setCreated(true);

            return entity;
        }
    }

    public void delete(final String id) {
        // TODO: Unimplemented
    }

    public abstract Map<String, Endpoint> getEndpoints();

    private String jsonSerialize(final T entity) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> serialized = entity.serialize();

        builder.append("{");

        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            builder.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");

        return builder.toString();
    }
}
