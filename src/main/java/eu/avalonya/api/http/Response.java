package eu.avalonya.api.http;

import com.google.gson.reflect.TypeToken;
import eu.avalonya.api.AvalonyaAPI;
import java.util.List;

public record Response(int status, String body) {

    public <T> T to(Class<T> clazz) {
        return AvalonyaAPI.getGson().fromJson(this.body, clazz);
    }

    public <T> List<T> toList(Class<T> clazz) {
        return AvalonyaAPI.getGson().fromJson(this.body, new TypeToken<List<T>>() {
        }.getType());
    }

}
