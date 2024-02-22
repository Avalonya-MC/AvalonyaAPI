package eu.avalonya.api.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseCommand<T extends CommandSender> implements CommandExecutor, TabCompleter, RunnableCommand<T>
{

    /**
     * Messages
     */
    private final Component INVALID_SENDER_TYPE = Component.text("Vous n'avez pas accès à cette commande.").color(NamedTextColor.RED);
    private final Component INSUFFICIENT_PERMISSION = Component.text("Vous n'avez pas accès à cette commande.").color(NamedTextColor.RED);
    /*private final Component ERROR_OCCURED = Component.text("Une erreur est survenue lors de l'éxécution de cette commande.").color(NamedTextColor.RED);
    private final Component INSUFFICIENT_PERMISSION = Component.text("Vous n'avez pas accès à cette commande.").color(NamedTextColor.RED);
    private final Component COOLDOWN_MESSAGE = Component.text("Vous devez attendre %s secondes avant de pouvoir réutiliser cette commande.").color(NamedTextColor.RED);
*/

    private final String name;
    private final ArgumentCollection arguments = new ArgumentCollection();
    private final Map<String, BaseCommand<?>> subCommands = new HashMap<>();
    private final List<UUID> onlyAccess = new ArrayList<>();
    private final List<String> permissions = new ArrayList<>();
    protected int cooldown = 0;
    private final Map<T, Long> cooldowns = new HashMap<>();

    /**
     * Constructors
     */

    public BaseCommand(@NotNull String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addArgument(Argument<?> argument)
    {
        arguments.add(argument);
    }

    public <U extends Argument<?>> void addArgument(Class<U> argument)
    {
        try
        {
            addArgument(argument.getDeclaredConstructor().newInstance());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public <U extends Argument<?>> void addArgument(Class<U> argument, boolean required)
    {
        try
        {
            Argument<?> arg = argument.getDeclaredConstructor().newInstance();
            arg.setRequired(required);
            addArgument(arg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addSubCommand(BaseCommand<?> command)
    {
        subCommands.put(command.getName(), command);
    }

    public void addOnlyAccess(UUID ...uuid)
    {
        onlyAccess.addAll(Arrays.asList(uuid));
    }

    public void addPermissions(String ...permissions)
    {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public List<String> getPermissions()
    {
        return permissions;
    }

    public void setCooldown(int cooldown)
    {
        this.cooldown = cooldown;
    }

    public int getCooldown()
    {
        return cooldown;
    }

    private boolean canExecute(T sender)
    {
        if (onlyAccess.size() > 0 && sender instanceof Entity)
        {
            return onlyAccess.contains(((Entity) sender).getUniqueId());
        }
        if (getPermissions().size() > 0)
        {
            for (String permission : getPermissions())
            {
                if (!sender.hasPermission(permission))
                {
                    return false;
                }
            }
        }
        if (getCooldown() > 0)
        {
            if (cooldowns.containsKey(sender))
            {
                return cooldowns.get(sender) <= System.currentTimeMillis();
            }
        }
        return true;
    }

    private void saveCooldown(T sender)
    {
        if (getCooldown() > 0)
        {
            cooldowns.put(sender, System.currentTimeMillis() + (cooldown * 1000L));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        try
        {
            T sender = (T) commandSender;
            if (!canExecute(sender))
            {
                sender.sendMessage(INSUFFICIENT_PERMISSION);
                return true;
            }

            if (args.length > 0)
            {
                BaseCommand<?> subCommand = subCommands.get(args[0]);
                if (subCommand != null)
                {
                    subCommand.onCommand(sender, command, s, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
            }

            for (int i = 0; i < this.arguments.size(); i++)
            {
                Argument<?> argument = this.arguments.get(i);
                if (args.length <= i)
                {
                    if (argument.isRequired())
                    {
                        sender.sendMessage("Argument " + argument.getClass().getSimpleName() + " is required.");
                        return true;
                    }
                }
                if (!argument.test(args[i]))
                {
                    if (argument.getErrorMessage() != null)
                        sender.sendMessage(argument.getErrorMessage());
                    return true;
                }
                argument.setInput(args[i]);
            }

            saveCooldown(sender);
            run(sender, arguments);
        }
        catch (ClassCastException e)
        {
            commandSender.sendMessage(INVALID_SENDER_TYPE);
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        final List<String> completions = new ArrayList<>();

        BaseCommand<?> subCommand = subCommands.get(strings[0]);
        if (subCommand != null)
        {
            return subCommand.onTabComplete(commandSender, command, s, Arrays.copyOfRange(strings, 1, strings.length));
        }
        else
        {
            if (strings.length == 1)
            {
                appendIfStartsWith(completions, subCommands.keySet(), strings[0]);
            }
            if (arguments.size() > strings.length - 1)
            {
                Argument<?> argument = arguments.get(strings.length - 1);
                appendIfStartsWith(completions, argument.getCompletions(), strings[strings.length - 1]);
            }
        }
        return completions;
    }

    private void appendIfStartsWith(List<String> from, Collection<String> collection, String start)
    {
        collection.forEach(completion -> {
            if (completion.startsWith(start))
            {
                from.add(completion);
            }
        });
    }

    public static <T extends CommandSender> BaseCommand<T> newSubCommand(String name, RunnableCommand<T> run)
    {
        return new BaseCommand<>(name)
        {
            @Override
            public void run(T sender, ArgumentCollection args)
            {
                run.run(sender, args);
            }
        };
    }

    public static void register(JavaPlugin plugin, BaseCommand<?> command)
    {
        final PluginCommand pluginCommand = plugin.getCommand(command.getName());

        if (pluginCommand == null)
        {
            throw new IllegalArgumentException("Command " + command.getName() + " is not registered in plugin.yml");
        }

        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }

}

