package eu.avalonya.api.sql.migrations;

import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.sql.Migration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class V01_AddPlayer extends Migration
{

    private String createTablePlayer = """
            CREATE TABLE `player` (
              `uuid` varchar(255) NOT NULL)
    """;


    @Override
    public void execute()
    {
        // Logique spécifique pour AddPlayer
        AvalonyaAPI.getInstance().getLogger().info(("Executing AddPlayerMigration"));
        try
        {
            PreparedStatement r = AvalonyaAPI.getSqlInstance().getConnection().prepareStatement(this.createTablePlayer);
            r.execute();
            r.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
