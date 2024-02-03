package eu.avalonya.api.sql;

import eu.avalonya.api.AvalonyaAPI;

import java.io.IOException;
import java.sql.*;

public class MigrationUtils
{

    public static boolean doesTableExist(String tableName)
    {
        try
        {
            Connection connection = AvalonyaAPI.getSqlInstance().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet resultSet = metaData.getTables(null, null, tableName, null);

           boolean r = resultSet.next();
           resultSet.close();

           return r;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

   public static int getCurrentMigrationVersion()
   {
       int version = -1;

       try
       {
           Connection connection = AvalonyaAPI.getSqlInstance().getConnection();
           PreparedStatement statement = connection.prepareStatement("SELECT version FROM migration_version");
           ResultSet resultSet = statement.executeQuery();

           if (resultSet.next())
           {
               version = resultSet.getInt("version");
           }
           else
           {
               System.out.println("Aucune ligne trouvée dans la table de migration.");
           }

           statement.close();

       }
       catch (SQLException e)
       {
           e.printStackTrace();
       }

       return version;
   }

   public void executeMigration(Migration migration)
   {
       migration.execute();
   }


}
