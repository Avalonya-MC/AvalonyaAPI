package eu.avalonya.api.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class BaseCommand implements CommandExecutor {

    /**
     * Messages
     */
    private final Component ERROR_OCCURED = Component.text("Une erreur est survenue lors de l'éxécution de cette commande.").color(NamedTextColor.RED);
    private final Component INSUFFICIENT_PERMISSION = Component.text("Vous n'avez pas accès à cette commande.").color(NamedTextColor.RED);
    private final Component NO_CONSOLE = Component.text("Vous ne pouvez pas exécuter cette commande à partir de la commande.").color(NamedTextColor.RED);
    private final Component ONLY_CONSOLE = Component.text("Cette commande ne peut être exécutée qu'à partir de la console.").color(NamedTextColor.RED);

    /**
     * Variables
     */
    private final SenderType senderType;
    private final String[] permissions;

    /**
     * Constructors
     */

    public BaseCommand() {
        this("", null);
    }

    public BaseCommand(String permission) {
        this(permission, null);
    }

    public BaseCommand(SenderType senderType) {
        this("", senderType);
    }


    public BaseCommand(String[] permissions) {
        this(permissions, null);
    }

    public BaseCommand(String permission, SenderType senderType) {
        this(new String[]{permission}, senderType);
    }

    public BaseCommand(String[] permissions, SenderType senderType) {
        this.senderType = senderType;
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    /**
     * Call the abstract method {@link BaseCommand#onCommand(CommandSender, SenderType, String[])}
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        SenderType senderType = (sender instanceof Player) ? SenderType.PLAYER : SenderType.CONSOLE;
        if (this.senderType != null && this.senderType != senderType) {
            sender.sendMessage(senderType == SenderType.PLAYER ? ONLY_CONSOLE : NO_CONSOLE);
            return true;
        }

        boolean hasPerm = Arrays.stream(permissions).anyMatch(p -> p.isEmpty() || sender.hasPermission(p) || (p.equalsIgnoreCase("OP") && sender.isOp()));

        if (!hasPerm) { // Does not have access to this command
            sender.sendMessage(INSUFFICIENT_PERMISSION);
            return false;
        }

        try { // Execute the command
            onCommand(sender, senderType, strings);
        } catch(Exception e) { // An error happened
            e.printStackTrace(); // TODO: Utiliser un Logger (éventuellement)
            sender.sendMessage(ERROR_OCCURED);
        }
        return true;
    }

    public abstract void onCommand(CommandSender sender, SenderType senderType, String[] args);

    public enum SenderType {

        PLAYER,
        CONSOLE

    }


}

