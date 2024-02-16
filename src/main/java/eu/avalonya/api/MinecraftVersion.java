package eu.avalonya.api;

import org.bukkit.Server;

public class MinecraftVersion
{

    private String version;

    public MinecraftVersion(Server server)
    {
        this.version = server.getVersion();
    }

    public String getVersion()
    {
        return this.version;
    }

    public boolean isUnitTest()
    {
        return this.version.contains("MockBukkit");
    }
}
