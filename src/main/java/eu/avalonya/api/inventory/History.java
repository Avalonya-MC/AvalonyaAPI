package eu.avalonya.api.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class History implements Listener
{

    private static Map<UUID, List<Inventory>> history = new HashMap<>();

    public History()
    {
        this.history = new HashMap<>();
    }

    public static void setHistory(Player player, Inventory inventory)
    {
        UUID playerUUID = player.getUniqueId();

        if (!history.containsKey(playerUUID))
        {
            history.put(playerUUID, new LinkedList<>());
        }

        history.get(playerUUID).add(inventory);
    }

    public static Map<UUID, List<Inventory>> getHistory()
    {
        return history;
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

    @EventHandler
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
