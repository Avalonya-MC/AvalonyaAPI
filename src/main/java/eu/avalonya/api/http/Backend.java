package eu.avalonya.api.http;

import eu.avalonya.api.utils.ConfigFilesManager;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.SneakyThrows;

public class
Backend {

    public final static String BACKEND_URL = ConfigFilesManager.getFile("backend").get()
        .getString("url");

    public static Response get(Endpoint endpoint, String body) {
        return request("GET", endpoint, body);
    }

    public static Response post(Endpoint endpoint, String body) {
        return request("POST", endpoint, body);
    }

    public static Response put(Endpoint endpoint, String body) {
        return request("PUT", endpoint, body);
    }

    public static Response delete(Endpoint endpoint) {
        return request("DELETE", endpoint, null);
    }

    @SneakyThrows
    public static Response request(String method, Endpoint endpoint, String body) {
        URL url = new URL(BACKEND_URL + endpoint.getPath());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization",
            "Bearer " + ConfigFilesManager.getFile("backend").get().getString("token"));
        con.setDoOutput(true);

        if (body != null) {
            con.getOutputStream().write(body.getBytes());
            con.getOutputStream().flush();
            con.getOutputStream().close();
        }

        int status = con.getResponseCode();
        String response = new String(con.getInputStream().readAllBytes());

        con.disconnect();

        return new Response(status, response);
    }

}
