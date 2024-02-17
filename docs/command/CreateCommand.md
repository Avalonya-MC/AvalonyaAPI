# Cree une Commande
Pour créer une commande, il suffit de créer une nouvelle classe qui étend `BaseCommand` et d'implémenter la méthode `run` qui sera appelée lors de l'exécution de la commande.

## Utilisation

```java
import eu.avalonya.api.command.BaseCommand;

public class ExampleCommand extends BaseCommand
{
    public ExampleCommand()
    {
        super("example");
    }

    @Override
    public void run(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la commande avec les arguments: " + Arrays.toString(args));
    }
}
```

## Ajouter des permissions
Pour ajouter des permissions à une commande, il suffit d'utiliser la methode `addPermission` dans le constructeur de la commande.

```java
import eu.avalonya.api.command.BaseCommand;

public class ExampleCommand extends BaseCommand
{
    public ExampleCommand()
    {
        super("example");
        
        addPermission("example.permission");
    }

    @Override
    public void run(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la commande avec les arguments: " + Arrays.toString(args));
    }
}
```
Il est possible de limiter la commande à un type de sender en precisant le type dans le constructeur de la commande.

```java
import eu.avalonya.api.command.BaseCommand;

public class ExampleCommand extends BaseCommand
{
    public ExampleCommand()
    {
        super("example", SenderType.PLAYER);
        
        addPermission("example.permission");
    }

    @Override
    public void run(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la commande avec les arguments: " + Arrays.toString(args));
    }
}
```
Les types de sender disponibles sont:
- `SenderType.PLAYER`
- `SenderType.CONSOLE`
- `SenderType.ALL`

## Ajouter un cooldown
Il est possible d'ajouter un cooldown à une commande en utilisant la methode `setCooldown` dans le constructeur de la commande.

```java
import eu.avalonya.api.command.BaseCommand;

public class ExampleCommand extends BaseCommand
{
    public ExampleCommand()
    {
        super("example");
        
        addPermission("example.permission");
        
        setCooldown(5);
    }

    @Override
    public void run(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la commande avec les arguments: " + Arrays.toString(args));
    }
}
```

## Ajouter des sous-commandes
Il est possible d'ajouter des sous-commandes à une commande en utilisant la methode `addSubCommand` dans le constructeur de la commande.

```java
import eu.avalonya.api.command.BaseCommand;

public class ExampleCommand extends BaseCommand
{
    public ExampleCommand()
    {
        super("example");
        
        addPermission("example.permission");
        
        addSubCommand("subcommand", this::runSubCommand);
    }

    @Override
    public void run(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la commande avec les arguments: " + Arrays.toString(args));
    }
    
    private void runSubCommand(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la sous-commande avec les arguments: " + Arrays.toString(args));
    }
}
```

### Ajouter des permissions aux sous-commandes
Il est possible d'ajouter des permissions à une sous-commande en utilisant la methode `addSubCommand` dans le constructeur de la sous-commande, grace au 3eme parametre de la methode `addSubCommand`.

```java
import eu.avalonya.api.command.BaseCommand;

public class ExampleCommand extends BaseCommand
{
    public ExampleCommand()
    {
        super("example");
        
        addPermission("example.permission");
        
        addSubCommand("subcommand", this::runSubCommand, "example.subcommand.permission");
    }

    @Override
    public void run(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la commande avec les arguments: " + Arrays.toString(args));
    }
    
    private void runSubCommand(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la sous-commande avec les arguments: " + Arrays.toString(args));
    }
}
```

### Ajouter un cooldown aux sous-commandes

Il est possible d'ajouter un cooldown à une sous-commande en utilisant la methode `setCooldown` dans le constructeur de la sous-commande, grace au 3eme parametre de la methode `addSubCommand`.

```java
import eu.avalonya.api.command.BaseCommand;

public class ExampleCommand extends BaseCommand
{
    public ExampleCommand()
    {
        super("example");
        
        addPermission("example.permission");
        
        addSubCommand("subcommand", this::runSubCommand, 5);
    }

    @Override
    public void run(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la commande avec les arguments: " + Arrays.toString(args));
    }
    
    private void runSubCommand(CommandSender sender, BaseCommand.SenderType type, String[] args)
    {
        sender.sendMessage("Vous avez exécuté la sous-commande avec les arguments: " + Arrays.toString(args));
    }
}
```