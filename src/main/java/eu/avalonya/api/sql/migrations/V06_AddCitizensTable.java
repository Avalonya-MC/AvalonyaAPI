package eu.avalonya.api.sql.migrations;

import eu.avalonya.api.sql.Migration;

public class V06_AddCitizensTable extends Migration
{

    private String addCitizensTable = """
            CREATE TABLE `citizens` (
                `player_uuid` VARCHAR(255) NOT NULL,
                `town_id` INT DEFAULT NULL,
                `role_id` INT NOT NULL DEFAULT 0,
                `money` FLOAT NOT NULL DEFAULT 0,
                `joined_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (`player_uuid`),
                FOREIGN KEY (`town_id`) REFERENCES `towns`(`id`),
                FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`),
                FOREIGN KEY (`player_uuid`) REFERENCES `player` (`uuid`)
            );
            """;

    @Override
    public void execute()
    {
        this.execute(this.addCitizensTable);
    }
}
