package eu.avalonya.api.repository;

import be.seeseemelk.mockbukkit.MockBukkit;
import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.http.Response;
import eu.avalonya.api.models.Citizen;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CitizenRepositoryTest {

    @BeforeEach
    void setUp() {
        MockBukkit.mock();
        MockBukkit.load(AvalonyaAPI.class);

        Backend.fake(Map.of(
                Endpoint.bind(Endpoint.CITIZENS, "test"), () -> {
                    Citizen citizen1 = new Citizen(UUID.randomUUID().toString(), null, 0L);
                    Citizen citizen2 = new Citizen(UUID.randomUUID().toString(), null, 0L);

                    List<Map<String, Object>> datas = List.of(citizen1.serialize(), citizen2.serialize());

                    return new Response(200, AvalonyaAPI.getGson().toJson(datas));
                }
        ));
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testGetAllCitizens() {
        CitizenRepository repository = new CitizenRepository(List.of("test"));

        assertEquals(2, repository.all().size());
    }

}