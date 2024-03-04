package eu.avalonya.api.sql.migrations;

import eu.avalonya.api.sql.Migration;

public class V06_AddCitizensTable extends Migration
{

    private String addCitizensTable = """
            CREATE TABLE `citizens` (
                `id` INT NOT NULL AUTO_INCREMENT,
                `town_id` INT DEFAULT NULL,
                `role_id` INT NOT NULL DEFAULT 0,
                `money` FLOAT NOT NULL DEFAULT 0,
                `joined_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (`id`),
                FOREIGN KEY (`town_id`) REFERENCES `towns`(`id`),
                FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`)
            );
            """;

    @Override
    public void execute()
    {
        this.execute(this.addCitizensTable);
    }
}
