package eu.avalonya.api;

import org.bukkit.plugin.java.JavaPlugin;

public class AvalonyaAPI extends JavaPlugin
{

    private static AvalonyaAPI instance;

    @Override
    public void onEnable()
    {
        instance = this;
    }

    @Override
    public void onDisable()
    {

    }

    public static AvalonyaAPI getInstance()
    {
        return instance;
    }

}