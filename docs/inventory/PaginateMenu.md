# PaginateMenu
La classe PaginateMenu est une classe abstraite qui permet de créer des menus avec des items paginés. Elle etend de la classe [`BaseMenu`](BaseMenu.md).
La taille du menu est de 54 slots et pagine 28 items par page.

## Utilisation
Pour utiliser la classe PaginateMenu, il suffit de créer une nouvelle classe qui étend `PaginateMenu` et d'implémenter la méthode `getItems` qui sera appelée lors de l'ouverture du menu.

```java
import java.util.List;

public class ExamplePaginateMenu extends PaginateMenu
{
    public ExamplePaginateMenu()
    {
        super("Titre du menu");
    }

    @Override
    public List<ItemStack> getItems()
    {
        List<ItemStack> items = new ArrayList<>();
        
        for (int i = 0; i < 100; i++)
        {
            items.add(new ItemStack(Material.DIAMOND));
            items.add(new ItemStack(Material.GOLD_INGOT));
            items.add(new ItemStack(Material.IRON_INGOT));
        }
    }
}
```