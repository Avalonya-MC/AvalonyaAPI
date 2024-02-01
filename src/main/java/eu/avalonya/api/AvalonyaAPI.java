package eu.avalonya.api;

import eu.avalonya.api.sql.SQL;
import eu.avalonya.api.utils.ConfigFilesManager;
import eu.avalonya.api.utils.CustomConfigFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class AvalonyaAPI extends JavaPlugin
{

    private static AvalonyaAPI instance;
    public static SQL sqlInstance;

    @Override
    public void onEnable()
    {
        instance = this;
        CustomConfigFile sqlConfig = new CustomConfigFile("database.yml");

        FileConfiguration fSql = ConfigFilesManager.getFile("sql").get();

        AvalonyaAPI.sqlInstance = new SQL("jdbc:mysql://", fSql.getString("host"), fSql.getString("database"), fSql.getString("user"), fSql.getString("password"));
        AvalonyaAPI.sqlInstance.connection();
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

}