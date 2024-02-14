# ConfirmMenu
La classe ConfirmMenu permet de créer un menu de confirmation avec un item pour confirmer et un item pour annuler. Elle etend de la classe [`BaseMenu`](BaseMenu.md).

## Utilisation
Pour utiliser la classe ConfirmMenu, il suffit de créer une nouvelle classe qui étend `ConfirmMenu` et d'implémenter la méthode `onConfirm` qui sera appelée lors du clique sur l'item de confirmation et la méthode `onCancel` qui sera appelée lors du clique sur l'item d'annulation.

```java
public class ExampleConfirmMenu extends ConfirmMenu
{
    public ExampleConfirmMenu()
    {
        super("Titre du menu", false);
    }

    @Override
    public void onConfirm(Player player)
    {
        player.sendMessage("Vous avez confirmé l'action");
    }

    @Override
    public void onCancel(Player player)
    {
        player.sendMessage("Vous avez annulé l'action");
    }
}
```