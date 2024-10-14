package eu.avalonya.api.http;

import com.google.gson.reflect.TypeToken;
import eu.avalonya.api.AvalonyaAPI;
import java.util.List;

public record Response(int status, String body) {

}
