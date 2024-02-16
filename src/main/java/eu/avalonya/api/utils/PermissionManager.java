package eu.avalonya.api.utils;

import eu.avalonya.api.models.Rank;
import eu.avalonya.api.sql.Cache;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;

public class PermissionManager
{

    public static void loadPermissionsFromConfigFileToCache()
    {
        FileConfiguration configFile = ConfigFilesManager.getFile("permissions").get();

        ConfigurationSection ranksSection = configFile.getConfigurationSection("ranks");

        if (ranksSection != null)
        {
            for (String rank : ranksSection.getKeys(false))
            {
                Rank r = Rank.rankIdToRank.get(configFile.getInt("ranks." + rank + ".rankId"));
                List<String> permissionsFromFile = configFile.getStringList("ranks." + rank + ".permissions");
                Cache.permissions.put(r, permissionsFromFile);
            }
        }
        else
        {
            System.out.println("La section 'ranks' n'existe pas dans le fichier de configuration.");
        }

        for (Map.Entry<Rank, List<String>> entry : Cache.permissions.entrySet())
        {
            Rank rank = entry.getKey();
            List<String> permissionList = entry.getValue();

            System.out.println("Rang: " + rank);
            System.out.println("Permissions: " + permissionList);
        }

    }

}
