package eu.avalonya.api;

import eu.avalonya.api.command.DemoCommand;
import eu.avalonya.api.models.AvalonyaDatabase;
import eu.avalonya.api.models.Citizen;
import eu.avalonya.api.models.Plot;
import eu.avalonya.api.models.Town;
import eu.avalonya.api.models.dao.CitizenDao;
import eu.avalonya.api.models.dao.PlotDao;
import eu.avalonya.api.models.dao.TownDao;
import eu.avalonya.api.sql.MigrationUtils;
import eu.avalonya.api.sql.SQL;
import eu.avalonya.api.utils.ConfigFilesManager;
import eu.avalonya.api.utils.CustomConfigFile;
import eu.avalonya.api.utils.PermissionManager;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class AvalonyaAPI extends JavaPlugin
{

    private static AvalonyaAPI instance;
    private static SQL sqlInstance;

    private static AvalonyaDatabase avalonyaDatabase;

    @Override
    public void onEnable()
    {
        instance = this;

        CustomConfigFile sqlConfig = new CustomConfigFile(AvalonyaAPI.getInstance(), "database.yml", "sql");
        CustomConfigFile permissionsConfig = new CustomConfigFile(AvalonyaAPI.getInstance(), "permissions.yml", "permissions");

        FileConfiguration fSql = ConfigFilesManager.getFile("sql").get();

        AvalonyaAPI.sqlInstance = new SQL("jdbc:mysql://", fSql.getString("host"), fSql.getString("database"), fSql.getString("user"), fSql.getString("password"));
        AvalonyaAPI.sqlInstance.connection();

        manageMigration();

        new DemoCommand().register(this);
        getCommand("town").setExecutor(this);

        PermissionManager.loadPermissionsFromConfigFileToCache();

        try
        {
            avalonyaDatabase = new AvalonyaDatabase("jdbc:mysql://" + fSql.getString("host") + "/" + fSql.getString("database") + "?autoreconnect=true", fSql.getString("user"), fSql.getString("password"));
        }
        catch (SQLException e)
        {
            this.getLogger().severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public static AvalonyaDatabase getDb()
    {
        return avalonyaDatabase;
    }

    public void manageMigration()
    {
        MigrationUtils.checkCurrentVersion();

        FastInvManager.register(this);
    }

    @Override
    public void onDisable()
    {
        AvalonyaAPI.sqlInstance.disconnect();
    }

    public static AvalonyaAPI getInstance()
    {
        return instance;
    }

    public static SQL getSqlInstance()
    {
        return sqlInstance;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("claim"))
            {
                Citizen citizen = CitizenDao.find((Player) sender);

                if (citizen == null)
                {
                    sender.sendMessage("You are not a citizen");
                    return true;
                }

                Town town = citizen.getTown();

                town.claim((Player) sender, ((Player) sender).getLocation().getChunk());
                return true;
            }
            else if (args[0].equalsIgnoreCase("unclaim"))
            {
                Town town = CitizenDao.find((Player) sender).getTown();

                town.unclaim((Player) sender, ((Player) sender).getLocation().getChunk());
                return true;
            }
            else if (args[0].equalsIgnoreCase("view"))
            {
                Plot plot = PlotDao.getPlot(((Player) sender).getLocation().getChunk());

                sender.sendMessage("Plot : " + plot.getId() + " | Town : " + plot.getTown().getName() + " | Type : " + plot.getType());
                return true;
            }

            Town town = TownDao.getTown(args[0]);

            if (town == null)
            {
                sender.sendMessage("Town not found");
                return true;
            }

            sender.sendMessage("Name : " + town.getName() + " | ID : " + town.getId());
        }
        else if (args.length == 2)
        {
            TownDao.create(args[0], (Player) sender);

            sender.sendMessage("Town created");
        }
        else
        {
            sender.sendMessage("Usage : /town <name>");
        }

        return true;
    }
}