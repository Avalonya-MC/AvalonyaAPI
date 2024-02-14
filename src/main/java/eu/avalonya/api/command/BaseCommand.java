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
    private final SenderType senderType;
    private final String[] permissions;
    private final Map<String, ICommand> subCommands = new HashMap<>();
    private int cooldown = 0;
    private final Map<CommandSender, Long> cooldowns = new HashMap<>();

    /**
     * Constructors
     */

    public BaseCommand() {
        this(null, SenderType.ALL);
    }

    public BaseCommand(@Nullable String permission) {
        this(permission, SenderType.ALL);
    }

    public BaseCommand(@NotNull SenderType senderType) {
        this(null, senderType);
    }


    public BaseCommand(@Nullable String ...permissions) {
        this(SenderType.ALL, permissions);
    }

    public BaseCommand(@Nullable String permission, @NotNull SenderType senderType) {
        this(senderType, permission);
    }

    public BaseCommand(@NotNull SenderType senderType, @Nullable String ...permissions) {
        this.senderType = senderType;
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
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
        boolean hasPerm = Arrays.stream(permissions).anyMatch(p -> p == null || sender.hasPermission(p) || sender.isOp());

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

    public static void register(@NotNull JavaPlugin plugin, @NotNull String name, @NotNull BaseCommand command) {
        final PluginCommand pluginCommand = plugin.getCommand(name);

        if (pluginCommand == null) {
            plugin.getLogger().severe("Impossible de trouver la commande " + name);
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

