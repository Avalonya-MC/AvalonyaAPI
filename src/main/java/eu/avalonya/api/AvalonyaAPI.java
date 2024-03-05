package eu.avalonya.api;

import eu.avalonya.api.command.DemoCommand;
import eu.avalonya.api.models.AvalonyaDatabase;
import eu.avalonya.api.sql.MigrationUtils;
import eu.avalonya.api.sql.SQL;
import eu.avalonya.api.utils.ConfigFilesManager;
import eu.avalonya.api.utils.CustomConfigFile;
import eu.avalonya.api.utils.DiscordLogger;
import eu.avalonya.api.utils.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class AvalonyaAPI extends JavaPlugin
{

    private static AvalonyaAPI instance;
    private static SQL sqlInstance;

    private static AvalonyaDatabase avalonyaDatabase;

    private static DiscordLogger discordLogger;

    @Override
    public void onEnable()
    {
        instance = this;

        discordLogger = new DiscordLogger("https://discord.com/api/webhooks/");
        discordLogger.sendMessage("Hello la zone");

        CustomConfigFile sqlConfig = new CustomConfigFile(AvalonyaAPI.getInstance(), "database.yml", "sql");
        CustomConfigFile permissionsConfig = new CustomConfigFile(AvalonyaAPI.getInstance(), "permissions.yml", "permissions");

        FileConfiguration fSql = ConfigFilesManager.getFile("sql").get();

        AvalonyaAPI.sqlInstance = new SQL("jdbc:mysql://", fSql.getString("host"), fSql.getString("database"), fSql.getString("user"), fSql.getString("password"));
        AvalonyaAPI.sqlInstance.connection();

        manageMigration();

        new DemoCommand().register(this);

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

    public void enableDiscordLogger() throws Exception {
        FileConfiguration discordConf = ConfigFilesManager.getFile("discord").get();
        discordLogger = new DiscordLogger(discordConf.getString("webhook"));
        discordLogger.sendSingleMessage("Démarrage du logger...");
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

    public static DiscordLogger getDiscordLogger()
    {
        return discordLogger;
    }

}