package eu.avalonya.api.inventory;

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

    public BaseMenu(int size, String title) {
        super(size, title);

        setBorderItem(new ItemStack(Material.BLUE_STAINED_GLASS_PANE), meta -> {
            meta.displayName(Component.text(" "));
        });

        setBackgroundItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), meta -> {
            meta.displayName(Component.text(" "));
        });

        setCloseItem(new ItemStack(Material.BARRIER), meta -> {
            meta.displayName(Component.text("§cFermer"));
        });

        init();
    }

    public abstract void init();

    public void setBorderItem(@Nullable ItemStack itemStack, @Nullable Consumer<ItemMeta> consumer) {
        if (itemStack == null) {
            return;
        }

        if (consumer != null) {
            final ItemMeta meta = itemStack.getItemMeta();
            consumer.accept(meta);
            itemStack.setItemMeta(meta);
        }

        for (int slot : getBorders()) {
            setItem(slot, itemStack, e -> e.setCancelled(true));
        }
    }

    public void setBackgroundItem(@Nullable ItemStack itemStack, @Nullable Consumer<ItemMeta> consumer) {
        if (itemStack == null) {
            return;
        }

        if (consumer != null) {
            final ItemMeta meta = itemStack.getItemMeta();
            consumer.accept(meta);
            itemStack.setItemMeta(meta);
        }

        for (int slot : getInventory().all(Material.AIR).keySet()) {
            setItem(slot, itemStack, e -> e.setCancelled(true));
        }
    }

    public void setCloseItem(@Nullable ItemStack itemStack, @Nullable Consumer<ItemMeta> consumer) {
        if (itemStack == null) {
            return;
        }

        if (consumer != null) {
            final ItemMeta meta = itemStack.getItemMeta();
            consumer.accept(meta);
            itemStack.setItemMeta(meta);
        }

        setItem(8, itemStack, e -> {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
        });
    }

    public void open(HumanEntity humanEntity) {
        super.open((Player) humanEntity);
    }

}
