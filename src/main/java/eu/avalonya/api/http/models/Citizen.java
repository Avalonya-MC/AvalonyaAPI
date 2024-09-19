package eu.avalonya.api.http.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class Citizen extends AbstractModel {

    private String uuid;
    private long joinedAt;

    public String formatJoinedAt(String format) {
        Date date = new Timestamp(this.getJoinedAt());

        return new SimpleDateFormat(format).format(date);
    }

}
