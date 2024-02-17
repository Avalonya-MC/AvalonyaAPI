package eu.avalonya.api.command.admin;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.command.BaseCommand;
import eu.avalonya.api.models.Rank;
import eu.avalonya.api.sql.RankRequest;
import eu.avalonya.api.utils.TabManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetRankCommand extends BaseCommand implements TabCompleter {

    public SetRankCommand()
    {
        super("setrank", SenderType.ALL);
        this.addPermissions("avalonya.admin");
    }
    @Override
    public void run(CommandSender sender, SenderType senderType, String[] args)
    {
        if (args.length != 2)
        {
            sender.sendMessage(getHelp());
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            sender.sendMessage("§cJoueur introuvable");
            return;
        }

        String rank = args[1];
        if (Rank.getRanksName().contains(rank))
        {
            int rankId = Rank.getIdFromName(rank);
            if (rankId == -1)
            {
                sender.sendMessage("§cRang introuvable");
                return;
            }
            try
            {
                RankRequest.setRankInDb(target, rankId);
                sender.sendMessage("§2Mise à jour du rang du joueur §l" + target.getName() + "§r§2 à §l" + rank);
                target.kick(Component.text("§2Votre rang vient d'être mit à jour à §l" + rank));
            }
            catch (Exception e)
            {
                sender.sendMessage("§cUne erreur est survenue : " + e.getMessage());
            }

        }
        else
        {
            sender.sendMessage("§cRang introuvable");
            sender.sendMessage(getHelp());
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        Set<String> completions = new HashSet<>();

        if (strings.length == 1)
        {
            ArrayList<String> onlinePlayers = new ArrayList<>();
            for(Player p : AvalonyaAPI.getInstance().getServer().getOnlinePlayers())
            {
                onlinePlayers.add(p.getName());
            }
            completions.addAll(onlinePlayers);
        }

        if (strings.length == 2)
        {
            completions.addAll(Rank.getRanksName());
        }

        return new ArrayList<>(completions);
    }

    public void subRunHelp(CommandSender sender, SenderType senderType, String[] args)
    {
        sender.sendMessage("help");
    }
    public String getUsage()
    {
        StringBuilder usage = new StringBuilder("§c➤ Utilisation : /setrank <player> <rank>\n");
        usage.append("§6Permet de modifier le grade d'un joueur.\n");
        usage.append("Rangs disponibles : \n");
        for(String rankName : Rank.getRanksName())
        {
            usage.append("    - " + rankName + "\n");
        }
        return usage.toString();
    }

    public String getHelp()
    {
        return Rank.getRanksName().toString();
    }

}
