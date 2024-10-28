package eu.avalonya.api.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.http.Response;
import eu.avalonya.api.models.AbstractModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepository<T extends AbstractModel> {

    private final Class<T> clazz;
    private final List<String> vars;
    private static Map<String, Map<Object, Object>> items = new HashMap<>();

    public AbstractRepository(Class<T> clazz, List<String> vars) {
        this.vars = vars;
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public List<T> all() {
        if (!getEndpoints().containsKey("all")) {
            throw new RuntimeException("You cannot all this model.");
        }

        Endpoint endpoint = Endpoint.bind(getEndpoints().get("all"), this.vars);
        Response response = Backend.get(endpoint, null);

        List<Map<String, Object>> objs = gson().fromJson(response.body(), new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        List<T> models = new ArrayList<>();

        for (Map<String, Object> data : objs) {
            try {
                Method method = this.clazz.getMethod("deserialize", Map.class);
                T model = (T) method.invoke(null, data);

                putInCache(model);
                models.add(model);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return models;
    }

    @SuppressWarnings("unchecked")
    public T get(final String id) {
        if (!getEndpoints().containsKey("get")) {
            throw new RuntimeException("You cannot get this model.");
        }

        Endpoint endpoint = Endpoint.bind(getEndpoints().get("get"), this.vars);
        Response response = Backend.get(endpoint, null);

        Map<String, Object> data = gson().fromJson(response.body(), new TypeToken<Map<String, Object>>() {
        }.getType());

        try {
            Method method = this.clazz.getMethod("deserialize", Map.class);
            T model = (T) method.invoke(null, data);

            putInCache(model);
            return model;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public T save(final T entity) {
        List<String> params = new ArrayList<>(this.vars);
        String serialized = AvalonyaAPI.getGson().toJson(entity.serialize());

        if (entity.isCreated()) {
            if (!getEndpoints().containsKey("update")) {
                throw new RuntimeException("You cannot update this model.");
            }

            params.add(entity.getId().value());

            Endpoint endpoint = Endpoint.bind(getEndpoints().get("update"), params);

            Backend.put(endpoint, serialized);
        } else {
            if (!getEndpoints().containsKey("create")) {
                throw new RuntimeException("You cannot create this model.");
            }

            Endpoint endpoint = Endpoint.bind(getEndpoints().get("create"), params);

            Backend.post(endpoint, serialized);

            entity.setCreated(true);
        }

        putInCache(entity);
        return entity;
    }

    public void delete(final T entity) {
        if (!getEndpoints().containsKey("delete")) {
            throw new RuntimeException("You cannot delete this model.");
        }

        List<String> params = new ArrayList<>(this.vars);

        params.add(entity.getId().value());

        Endpoint endpoint = Endpoint.bind(getEndpoints().get("delete"), params);

        deleteInCache(entity);
        Backend.delete(endpoint);
    }

    private void putInCache(T entity) {
        if (!items.containsKey(getClass().getSimpleName())) {
            items.put(getClass().getSimpleName(), new HashMap<>());
        }

        items.get(getClass().getSimpleName()).put(entity.getId().value(), entity);
    }

    private void deleteInCache(T entity) {
        if (items.containsKey(getClass().getSimpleName())) {
            items.get(getClass().getSimpleName()).remove(entity.getId().value());
        }
    }

    public abstract Map<String, Endpoint> getEndpoints();

    protected Gson gson() {
        return AvalonyaAPI.getGson();
    }
}
