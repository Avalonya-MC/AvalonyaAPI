package eu.avalonya.api;

import eu.avalonya.api.sql.Migration;
import eu.avalonya.api.sql.MigrationUtils;
import eu.avalonya.api.sql.SQL;
import eu.avalonya.api.sql.migrations.MigrationMapping;
import eu.avalonya.api.utils.ConfigFilesManager;
import eu.avalonya.api.utils.CustomConfigFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class AvalonyaAPI extends JavaPlugin
{

    private static AvalonyaAPI instance;
    private static SQL sqlInstance;

    @Override
    public void onEnable()
    {
        instance = this;
        CustomConfigFile sqlConfig = new CustomConfigFile("database.yml");

        FileConfiguration fSql = ConfigFilesManager.getFile("sql").get();

        AvalonyaAPI.sqlInstance = new SQL("jdbc:mysql://", fSql.getString("host"), fSql.getString("database"), fSql.getString("user"), fSql.getString("password"));
        AvalonyaAPI.sqlInstance.connection();

        manageMigration();
    }

    public void manageMigration()
    {
        int currentVersion = -1;
        if(MigrationUtils.doesTableExist("migration_version"))
        {
            currentVersion = MigrationUtils.getCurrentMigrationVersion();
            getLogger().info("Current migration version : " + currentVersion);
        }
        else
        {
           getLogger().warning("`migration` table does not exists. Run the creation.");
            Migration migration1;
            try
            {
                migration1 = MigrationMapping.createMigrationById(0);
            }
            catch (IllegalAccessException | InstantiationException e)
            {
                throw new RuntimeException(e);
            }
            migration1.execute();
            currentVersion = 0;
        }
        if(currentVersion == MigrationMapping.values().length)
        {
            AvalonyaAPI.getInstance().getLogger().info("Database is up to date");
        }
        else if(currentVersion < MigrationMapping.values().length)
        {
           AvalonyaAPI.getInstance().getLogger().info("Database is not up to date, need to be updated.");
           for(int i = 1; i < (MigrationMapping.values().length - 1); i++)
           {
               Migration migration;
               try
               {
                   migration = MigrationMapping.createMigrationById(i);
               }
               catch (IllegalAccessException | InstantiationException e)
               {
                   throw new RuntimeException(e);
               }
               AvalonyaAPI.getInstance().getLogger().info("Apply migration number : " + i);
               migration.execute();
           }
           AvalonyaAPI.getInstance().getLogger().info("Database is up to date.");
        }
        else
        {
            AvalonyaAPI.getInstance().getLogger().severe("Error during migration");
        }

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

}