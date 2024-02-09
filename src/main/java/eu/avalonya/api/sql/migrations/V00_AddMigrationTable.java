package eu.avalonya.api.sql.migrations;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.sql.Migration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class V00_AddMigrationTable extends Migration
{

    private String createTableRequest = """
            CREATE TABLE `migration_version` (
              `version` int(11) NOT NULL)
    """;

    private String uniqueKeyRequest = """               
            ALTER TABLE `migration_version`
              ADD UNIQUE KEY `i_version` (`version`);
    """;

    @Override
    public void execute()
    {
        try
        {
            AvalonyaAPI.getInstance().getLogger().info("Executing AddMigrationVersion");
            // Merge 2 requests to avoid disconnect after first request
            PreparedStatement r = AvalonyaAPI.getSqlInstance().getConnection().prepareStatement(this.createTableRequest);
            r.execute();
            r.close();

            PreparedStatement r2 = AvalonyaAPI.getSqlInstance().getConnection().prepareStatement(this.uniqueKeyRequest);
            r2.execute();
            r2.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
