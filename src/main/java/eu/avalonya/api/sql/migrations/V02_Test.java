package eu.avalonya.api.sql.migrations;

import eu.avalonya.api.sql.Migration;

public class V02_Test extends Migration
{

    @Override
    public void execute()
    {
        // Logique spécifique pour AddPlayer
        System.out.println("Executing AddPlayerMigration");
    }

}
