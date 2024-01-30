package eu.avalonya.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BaseCommand implements CommandExecutor {

    private final String name;
    private final String permission;

    public BaseCommand(String name) {
        this(name, null);
    }

    public BaseCommand(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
}
