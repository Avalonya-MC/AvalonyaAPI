package eu.avalonya.api.http.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import eu.avalonya.api.http.Endpoint;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private static final Gson gson = new Gson();

    @Test
    public void testGetEndpoint() {
        Player player = new Player();
        assertEquals(Endpoint.PLAYER, player.getEndpoint());
    }

}
