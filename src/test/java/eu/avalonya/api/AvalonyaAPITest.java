package eu.avalonya.api;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AvalonyaAPITest {

    private JavaPlugin plugin;

    @BeforeEach
    public void setUp()
    {
        MockBukkit.mock();
        plugin = MockBukkit.load(AvalonyaAPI.class);
    }

    @AfterEach
    public void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    void testOnEnable()
    {
        Assertions.assertTrue(plugin.isEnabled());
    }

    @Test
    void testInstance()
    {
        Assertions.assertNotNull(AvalonyaAPI.getInstance());
    }

}
