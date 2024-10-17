package eu.avalonya.api.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class History implements Listener
{

    private final Map<UUID, List<Inventory>> history;

    public History()
    {
        this.history = new HashMap<>();
    }

    public void open(HumanEntity humanEntity, Inventory inventory)
    {
        Player player = (Player) humanEntity;

        player.openInventory(inventory);

        if (!history.containsKey(player.getUniqueId()))
        {
            history.put(player.getUniqueId(), new ArrayList<>());
        }
        history.get(player.getUniqueId()).add(inventory);
    }

    public void close(Player player)
    {
        if (history.containsKey(player.getUniqueId()))
        {
            history.remove(player.getUniqueId());
        }
    }

    public void openLast(Player player)
    {
        UUID playerUUID = player.getUniqueId();

        int inventoryListSize = history.get(playerUUID).size();

        player.openInventory(history.get(playerUUID).get(inventoryListSize - 1));
        history.get(playerUUID).removeLast();
    }

    public void playerCloseInv(InventoryCloseEvent event)
    {
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (history.containsKey(playerUUID))
        {
            if (!event.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW))
            {
                history.remove(playerUUID);

            }

        }

    }
}
