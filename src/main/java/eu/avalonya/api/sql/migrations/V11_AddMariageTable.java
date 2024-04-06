package eu.avalonya.api.sql.migrations;

import eu.avalonya.api.sql.Migration;

public class V11_AddMariageTable extends Migration
{

    private final String addMariageTable = """
            CREATE TABLE `mariage` (
              `id` INT NOT NULL AUTO_INCREMENT,
              `sender` INT NOT NULL,
              `receiver` INT NOT NULL,
              `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
              FOREIGN KEY (`sender`) REFERENCES `citizens`(`id`),
              FOREIGN KEY (`receiver`) REFERENCES `citizens`(`id`),
              PRIMARY KEY (`id`)
            );
            """;

    @Override
    public void execute()
    {
        this.execute(addMariageTable);
    }

}
