package eu.avalonya.api.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@DatabaseTable(tableName = "player_1")
@Getter
@Setter
public class APlayer
{

    @DatabaseField(id = true)
    private String uuid;

    @DatabaseField(canBeNull = false)
    private String pseudo;

    @DatabaseField(columnName = "rank_id", canBeNull = false, defaultValue = "0")
    private int rankId;

    @DatabaseField(columnName = "last_login", canBeNull = false)
    private Timestamp lastLogin;

    @DatabaseField(columnName = "first_login", canBeNull = false)
    private Timestamp firstLogin;

    @DatabaseField(columnName = "last_ip", canBeNull = false)
    private String lastIp;

    public APlayer(){}

}
