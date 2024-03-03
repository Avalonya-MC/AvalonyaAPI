package eu.avalonya.api.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordLogger
{

    private final String webhook;

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

    private void sendSingleMessage(String message) throws Exception
    {
        URL url = new URL(this.webhook);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonMessage = "{\"content\":\"" + format(message) + "\"}";

        try (OutputStream os = connection.getOutputStream())
        {
            byte[] input = jsonMessage.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode);
        System.out.println("Response message:" + connection.getResponseMessage());

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

}
