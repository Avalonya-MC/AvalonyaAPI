package eu.avalonya.api.http;

import lombok.Getter;
import lombok.Setter;

public enum Endpoint {

    // POST
    PLAYER("/player"),
    // GET
    PLAYERS("/players"),
    // GET, PUT, DELETE
    PLAYERS_ID("/players/{id}"),
    // GET, POST
    PLAYER_WARNS("/player/{id}/warns"),
    // GET, POST
    BACKPACKS("/backpacks"),
    // GET, PUT, DELETE
    BACKPACKS_ID("/backpacks/{id}"),
    // GET, POST
    NATIONS("/nations"),
    // GET, PUT, DELETE
    NATIONS_NAME("/nations/{name}"),
    // GET, POST
    RANKS("/ranks"),
    // GET, PUT, DELETE
    RANKS_NAME("/ranks/{name}"),
    ;

    private static final String REGEX = "\\{[a-zA-Z0-9]+\\}";

    @Getter
    @Setter
    private String path;

    Endpoint(String path) {
        this.path = path;
    }

    public static Endpoint bind(Endpoint endpoint, String... params) {
        String path = endpoint.getPath();
        for (String param : params) {
            path = path.replaceFirst(REGEX, param);
        }

        endpoint.setPath(path);

        return endpoint;
    }
}
