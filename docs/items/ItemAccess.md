# ItemAccess
Cette interface permet de preciser a un objet qu'il peut etre converti en un ItemStack.

## Utilisation
```java
public class ExampleItem implements ItemAccess {
    @Override
    public ItemStack toItemStack() {
        return new ItemStack(Material.DIAMOND_SWORD);
    }
}
```