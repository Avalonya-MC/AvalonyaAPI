package eu.avalonya.api.inventory;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import eu.avalonya.api.AvalonyaAPI;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BaseMenuTest
{

    private PlayerMock player;
    private BaseMenu testMenu;

    @BeforeEach
    void setUp()
    {
        ServerMock serverMock = MockBukkit.mock();
        MockBukkit.load(AvalonyaAPI.class);

        player = serverMock.addPlayer();
        testMenu = new BaseMenu(54, "Test")
        {
            @Override
            public void init()
            {
                setItem(10, new ItemStack(Material.APPLE), e -> e.getWhoClicked().sendMessage("Hello, world!"));
            }
        };
    }

    @AfterEach
    void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    void testOpenMenu()
    {
        testMenu.open(player);
        Assertions.assertEquals(player.getOpenInventory().getTopInventory(), testMenu.getInventory());
    }

    @Test
    void testExistCloseItem()
    {
        testMenu.open(player);
        final ItemStack closeItem = testMenu.getInventory().getItem(8);

        Assertions.assertNotNull(closeItem);
        Assertions.assertEquals(closeItem.getType(), Material.BARRIER);
    }

}
