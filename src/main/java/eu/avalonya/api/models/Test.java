package eu.avalonya.api.models;

import eu.avalonya.api.http.Endpoint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Test extends Model<Test> {

    private String uuid;
    private String pseudo;
    private Integer rank;
    private Long first_login;
    private String last_ip;
    private Double money;

    @Override
    public Endpoint getEndpoint() {
        return Endpoint.PLAYER;
    }

}
