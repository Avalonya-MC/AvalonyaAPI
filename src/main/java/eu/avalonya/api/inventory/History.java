package eu.avalonya.api.inventory;



import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class History {

    private final Map<UUID, List<Inventory>> history;

    public History() {
        this.history = new HashMap<>();
    }

    public void open(HumanEntity humanEntity, Inventory inventory) {
        Player player = (Player) humanEntity;

        player.openInventory(inventory);

        if (!history.containsKey(player.getUniqueId())) {
            history.put(player.getUniqueId(), new ArrayList<>());
        }
        history.get(player.getUniqueId()).add(inventory);
    }

    public void close(Player player) {
        if (history.containsKey(player.getUniqueId())) {
            history.get(player.getUniqueId()).removeAll(history.get(player.getUniqueId()));
        }
    }

    public Inventory getLast(Player player) {
        return history.get(player.getUniqueId()).getLast();
    }
}
