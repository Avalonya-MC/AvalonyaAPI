package eu.avalonya.api.command;

import com.google.common.base.Preconditions;
import eu.avalonya.api.AvalonyaAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseCommand implements CommandExecutor, ICommand, TabCompleter {

    /**
     * Messages
     */
    private final Component ERROR_OCCURED = Component.text("Une erreur est survenue lors de l'éxécution de cette commande.").color(NamedTextColor.RED);
    private final Component INSUFFICIENT_PERMISSION = Component.text("Vous n'avez pas accès à cette commande.").color(NamedTextColor.RED);
    private final Component INVALID_SENDER_TYPE = Component.text("Vous n'avez pas accès à cette commande.").color(NamedTextColor.RED);
    private final Component COOLDOWN_MESSAGE = Component.text("Vous devez attendre %s secondes avant de pouvoir réutiliser cette commande.").color(NamedTextColor.RED);

    /**
     * Variables
     */
    private final String name;
    private final SenderType senderType;
    private final List<String> permissions = new ArrayList<>();
    private final Map<String, ICommand> subCommands = new HashMap<>();
    private int cooldown = 0;
    private final Map<CommandSender, Long> cooldowns = new HashMap<>();

    /**
     * Constructors
     */

    public BaseCommand(@NotNull String name)
    {
        this(name, SenderType.ALL);
    }

    public BaseCommand(@NotNull String name, @NotNull SenderType senderType) {
        this.name = name;
        this.senderType = senderType;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getPermissions()
    {
        return permissions;
    }

    public void addPermission(String permission)
    {
        permissions.add(permission);
    }

    public void addPermissions(String... permissions)
    {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    private boolean canExecute(CommandSender sender)
    {
        return permissions.stream().anyMatch(sender::hasPermission) || permissions.isEmpty();
    }

    public void addSubCommand(String name, ICommand command)
    {
        subCommands.put(name, command);
    }

    public boolean hasCooldown() {
        return cooldown > 0;
    }

    /**
     * Set the cooldown for the command
     * @param cooldown The cooldown in seconds
     */
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Call the abstract method {@link BaseCommand#run(CommandSender, SenderType, String[])}
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings)
    {
        final SenderType type = SenderType.fromInstance(sender);

        if (this.senderType != SenderType.ALL && this.senderType != type)
        {
            sender.sendMessage(INVALID_SENDER_TYPE);
            return true;
        }

        // Si p == null ou le joueur a la permission ou le joueur est op alors on retourne true
        boolean hasPerm = canExecute(sender);

        if (!hasPerm)
        {
            sender.sendMessage(INSUFFICIENT_PERMISSION);
            return true;
        }

        try
        {
            if (strings.length > 0)
            {
                ICommand subCommand = subCommands.get(strings[0]);
                if (subCommand != null)
                {
                    subCommand.run(sender, type, Arrays.copyOfRange(strings, 1, strings.length));
                    return true;
                }
            }
            if (hasCooldown())
            {
                if (this.cooldowns.containsKey(sender) && this.cooldowns.get(sender) >= System.currentTimeMillis())
                {
                    final TextReplacementConfig config = TextReplacementConfig.builder()
                            .replacement(String.valueOf((this.cooldowns.get(sender) - System.currentTimeMillis()) / 1000))
                            .match("%s")
                            .build();

                    sender.sendMessage(COOLDOWN_MESSAGE.replaceText(config));
                    return true;
                }
                this.cooldowns.put(sender, System.currentTimeMillis() + (this.cooldown * 1000L));
            }
            run(sender, type, strings);
        }
        catch(Exception e)
        {
            sender.sendMessage(ERROR_OCCURED);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Set<String> completions = new HashSet<>();

        if (strings.length == 1)
        {
            completions.addAll(subCommands.keySet());
        }

        return new ArrayList<>(completions);
    }

    public static void register(@NotNull JavaPlugin plugin, @NotNull BaseCommand command) {
        final PluginCommand pluginCommand = plugin.getCommand(command.getName());

        if (pluginCommand == null) {
            plugin.getLogger().severe("Impossible de trouver la commande " + command.getName());
            return;
        }
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }

    public enum SenderType {

        NONE,
        PLAYER,
        CONSOLE,
        ALL;

        public static SenderType fromInstance(CommandSender sender) {
            if (sender instanceof Player)
            {
                return PLAYER;
            }
            else if (sender instanceof ConsoleCommandSender)
            {
                return CONSOLE;
            }
            return NONE;
        }

    }


}

