# BaseMenu
La classe BaseMenu est une classe abstraite qui permet de créer des menus avec des items statiques. Elle etend de la classe [`FastInv`](https://github.com/MrMicky-FR/FastInv).

## Utilisation
Pour utiliser la classe BaseMenu, il suffit de créer une nouvelle classe qui étend `BaseMenu` et d'implémenter la méthode `init` qui sera appelée lors de l'ouverture du menu.

```java
public class ExampleMenu extends BaseMenu
{
    public ExampleMenu()
    {
        super(27, "Titre du menu");
    }

    @Override
    public void init()
    {
        setItem(13, new ItemStack(Material.DIAMOND), player -> player.sendMessage("Vous avez cliqué sur le diamant"));
    }
}
```
Des bordures ainsi qu'un fond sont automatiquement ajoutés au menu.
- Les bordures sont des items de type `Material.BLUE_STAINED_GLASS_PANE` et sont placés sur les bords du menu.
- Le fond est un item de type `Material.GRAY_STAINED_GLASS_PANE` et est placé sur tout le reste du menu.