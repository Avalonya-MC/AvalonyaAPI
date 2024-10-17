package eu.avalonya.api.inventory;

import eu.avalonya.api.items.CustomItemStack;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.IntStream;

public abstract class BaseMenu extends FastInv
{

    public final static ItemStack DEFAULT_BORDER_ITEM = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, " ");
    public final static ItemStack DEFAULT_BACKGROUND_ITEM = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ");
    public final static ItemStack DEFAULT_CLOSE_ITEM = new CustomItemStack(Material.BARRIER, "§cFermer");
    public final static ItemStack BACK_ITEM = new CustomItemStack("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5NzFkZDg4MWRiYWY0ZmQ2YmNhYTkzNjE0NDkzYzYxMmY4Njk2NDFlZDU5ZDFjOTM2M2EzNjY2YTVmYTYifX19", "&fRetour");

    public Map<UUID, List<Inventory>> history;

    public BaseMenu(int size, String title) {
        this(size, title, DEFAULT_BORDER_ITEM);
//        this.history = history;
    }

    public BaseMenu(int size, String title, ItemStack borderItem) {
        super(size, title);
        super.getInventory().toString();

        for (int i : getBorders()) {
            setItem(i, borderItem, e -> e.setCancelled(true));
        }

        for (int i = 0; i < size; i++) {
            if (getInventory().getItem(i) == null)
                setItem(i, DEFAULT_BACKGROUND_ITEM, e -> e.setCancelled(true));
        }

        setItem(8, DEFAULT_CLOSE_ITEM, e -> e.getInventory().close());

        init();
    }

    public abstract void init();

//    public void open(HumanEntity humanEntity) {
//        super.open((Player) humanEntity);
//    }

    public int[] getContent() {
        int size = this.getInventory().getSize();

        // On verifie que i ne soit pas un multiple de 9 (bordure gauche)
        // ou un multiple de 9 - 1 (bordure droite)
        // ou inférieur à 9 (bordure haute)
        // ou supérieur à size - 9 (bordure basse)
        return IntStream.range(0, size).filter(i -> !(size < 27 || i < 9
                || i % 9 == 0 || (i - 8) % 9 == 0 || i > size - 9)).toArray();
    }

    /*
    History
     */

//    public History()
//    {
//        this.history = new HashMap<>();
//    }

    public void open(HumanEntity humanEntity)
    {
        Player player = (Player) humanEntity;

//        player.openInventory(inventory);
        super.open(player);

        Inventory inventory = super.getInventory();

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
