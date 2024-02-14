# AnvilMenu
L'AnvilMenu est un utilitaire permettant de créer des menus d'enclume personnalisés effectuant une action de confirmation lors du clique sur l'item de d'output.

## Utilisation
Pour utiliser l'AnvilMenu, il suffit de créer une nouvelle classe qui étend `AnvilMenu` et d'implémenter la méthode `onConfirm` qui sera appelée lors du clique sur l'item de d'output.

```java
public class ExampleAnvilMenu extends AnvilMenu
{
    public ExampleAnvilMenu()
    {
        super("Titre du menu", "Texte dans le barre d'input");
    }

    @Override
    public List<AnvilGUI.ResponseAction> onConfirm(Player player, String text)
    {
        player.sendMessage("Vous avez confirmé l'action avec l'input: " + input);
        
        return Arrays.asList(AnvilGUI.ResponseAction.close());
    }
}
```

les retour de la méthode `onConfirm` sont les actions à effectuer après la confirmation. Il est possible de faire les actions suivantes:
- `AnvilGUI.ResponseAction.close()`: Ferme le menu
- `AnvilGUI.ResponseAction.replaceInputText(String text)`: Change le texte dans la barre d'input
- `AnvilGUI.ResponseAction.updateTitle(String literalTitle, boolean preserveRenameText)` : Change le titre du menu
- `AnvilGUI.ResponseAction.updateJsonTitle(String json, boolean preserveRenameText)` : Change le titre du menu avec du json
- `AnvilGUI.ResponseAction.openInventory(Inventory otherInventory)` : Ouvre un autre inventaire
- `AnvilGUI.ResponseAction.run(Runnable runnable)` : Exécute une action