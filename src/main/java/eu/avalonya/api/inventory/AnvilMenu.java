package eu.avalonya.api.inventory;

import eu.avalonya.api.items.CustomItemStack;
import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public abstract class AnvilMenu extends FastInv {

    public final static ItemStack FIRST_SLOT_ITEM = new CustomItemStack(Material.PAPER, " ");
    public final static ItemStack OUTPUT_SLOT_ITEM = new CustomItemStack(Material.PAPER, " ");

    public AnvilMenu(String title) {
        super(InventoryType.ANVIL, title);

        AnvilInventory anvilInventory = (AnvilInventory) getInventory();

        anvilInventory.setRepairCost(0);
        setItem(0, FIRST_SLOT_ITEM, event -> event.setCancelled(true));
        setItem(2, OUTPUT_SLOT_ITEM, event -> {
            event.setCancelled(true);
            onConfirm((Player) event.getWhoClicked(), anvilInventory.getRenameText());
        });
    }

    public abstract void onConfirm(Player player, String text);

}
