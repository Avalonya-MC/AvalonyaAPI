package eu.avalonya.api.http;

import eu.avalonya.api.utils.ConfigFilesManager;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.SneakyThrows;

public class Backend {

    public final static String BACKEND_URL = ConfigFilesManager.getFile("backend").get().getString("url");

    public static String get(Endpoint endpoint, String body)
    {
        return request("GET", endpoint, body);
    }

    public static String post(Endpoint endpoint, String body)
    {
        return request("POST", endpoint, body);
    }

    public static String put(Endpoint endpoint, String body)
    {
        return request("PUT", endpoint, body);
    }

    public static String delete(Endpoint endpoint, String body)
    {
        return request("DELETE", endpoint, body);
    }

    @SneakyThrows
    public static String request(String method, Endpoint endpoint, String body)
    {
        URL url = new URL(BACKEND_URL + endpoint.getPath());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        con.getOutputStream().write(body.getBytes());
        con.getOutputStream().flush();
        con.getOutputStream().close();

        String response = con.getResponseMessage();

        con.disconnect();

        return response;
    }

}
