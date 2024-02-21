package eu.avalonya.api;

import org.bukkit.Server;

public class MinecraftVersion
{

    private static String version = null;

    public MinecraftVersion(Server server)
    {
        version = server.getVersion();
    }

    public boolean isUnitVersion()
    {
        return version != null && version.contains("MockBukkit");
    }
}
