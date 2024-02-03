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
        if(MigrationUtils.doesTableExist("migration_version"))
        {
            getLogger().info("Current migration version : " + MigrationUtils.getCurrentMigrationVersion());
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