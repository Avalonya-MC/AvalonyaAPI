package eu.avalonya.api.utils;

import eu.avalonya.api.AvalonyaAPI;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordLogger
{

    private final String webhook;

    private final String JSON = """
        {
            "content": "",
            "embeds": [
                {
                    "title": "%function%",
                    "description": "%stack_trace%",
                    "color": "%color%"
                }],
            "username": "%username%",
            "avatar_url": "%avatar_url%"
        }""";

    public DiscordLogger(String webhookUrl)
    {
        this.webhook = webhookUrl;
    }

    public void sendMessage(String message)
    {
        try
        {
            String[] lines = message.split("\\n");

            StringBuilder chunk = new StringBuilder();
            for (String line : lines)
            {
                if ((chunk.length() + line.length()) > 2000)
                {
                    sendSingleMessage(chunk.toString());
                    chunk.setLength(0);
                }
                chunk.append(line).append("\n");
            }

            if (!chunk.isEmpty())
            {
                sendSingleMessage(chunk.toString());
            }
        }
        catch (Exception e)
        {
            // ...
        }
    }

    public void sendSingleMessage(String message) throws Exception
    {
        URL url = new URL(this.webhook);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        FileConfiguration conf = ConfigFilesManager.getFile("discord").get();

        String jsonCustom = this.replaceValues("fonction",
                message,
                conf.getString("color"),
                conf.getString("username"),
                conf.getString("logo"));

        try (OutputStream os = connection.getOutputStream())
        {
            byte[] input = jsonCustom.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode);
        System.out.println("Response message:" + connection.getResponseMessage());
        if (responseCode != 200)

        connection.disconnect();
    }

    public String format(String message)
    {
        String newMessage = message.replace("\"", "'");
        newMessage = newMessage.replace("\r", "");
        newMessage = newMessage.replace("\n", "\\n");
        newMessage = newMessage.replace("\t", "\\t");
        return newMessage;
    }

    public String replaceValues(String functionName, String stackTrace, String color, String username, String avatarUrl)
    {
        String newJson = this.JSON;
        newJson = newJson.replace("%function%", functionName);
        newJson = newJson.replace("%stack_trace%", stackTrace);
        newJson = newJson.replace("%color%", color);
        newJson = newJson.replace("%username%", username);
        newJson = newJson.replace("%avatar_url%", avatarUrl);
        System.out.println(newJson);
        return newJson;
    }

}
