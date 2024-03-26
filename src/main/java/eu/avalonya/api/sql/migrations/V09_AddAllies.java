package eu.avalonya.api.sql.migrations;

import eu.avalonya.api.sql.Migration;

public class V09_AddAllies extends Migration {

    private final String addAlliesTable = """
            CREATE TABLE `town_allies` (
                `town_sender` INT NOT NULL,
                `town_receiver` INT NOT NULL,
                `pending` BOOLEAN NOT NULL DEFAULT FALSE,
                `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (`town_sender`) REFERENCES `towns`(`id`),
                FOREIGN KEY (`town_receiver`) REFERENCES `towns`(`id`)
            );
            """;

    @Override
    public void execute() {
        this.execute(addAlliesTable);
    }
}
