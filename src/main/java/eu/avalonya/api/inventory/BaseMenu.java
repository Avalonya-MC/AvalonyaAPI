package eu.avalonya.api.inventory;

import eu.avalonya.api.items.CustomItemStack;
import fr.mrmicky.fastinv.FastInv;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class BaseMenu extends FastInv {

    public static ItemStack DEFAULT_BORDER_ITEM = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, " ");
    public static ItemStack DEFAULT_BACKGROUND_ITEM = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ");
    public static ItemStack DEFAULT_CLOSE_ITEM = new CustomItemStack(Material.BARRIER, "§cFermer");

    public BaseMenu(int size, String title) {
        super(size, title);

        for (int i : getBorders()) {
            setItem(i, DEFAULT_BORDER_ITEM, e -> e.setCancelled(true));
        }

        for (int i : getInventory().all(Material.AIR).keySet()) {
            setItem(i, DEFAULT_BACKGROUND_ITEM, e -> e.setCancelled(true));
        }

        setItem(8, DEFAULT_CLOSE_ITEM, e -> e.getInventory().close());

        init();
    }

    public abstract void init();

    public void open(HumanEntity humanEntity) {
        super.open((Player) humanEntity);
    }

}
