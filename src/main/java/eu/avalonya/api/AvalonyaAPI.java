package eu.avalonya.api;

import com.google.gson.Gson;
import eu.avalonya.api.command.DemoCommand;
import eu.avalonya.api.inventory.History;
import eu.avalonya.api.repository.AvaPlayerRepository;
import eu.avalonya.api.repository.TownRepository;
import eu.avalonya.api.utils.CustomConfigFile;
import eu.avalonya.api.utils.PermissionManager;
import lombok.Getter;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AvalonyaAPI extends JavaPlugin
{

    @Getter
    private static AvalonyaAPI instance;
    @Getter
    private static final Gson gson = new Gson();

    @Override
    public void onEnable()
    {
        instance = this;

        new CustomConfigFile(AvalonyaAPI.getInstance(), "backend.yml", "backend");
        new CustomConfigFile(AvalonyaAPI.getInstance(), "permissions.yml", "permissions");

        new DemoCommand().register(this);

        PermissionManager.loadPermissionsFromConfigFileToCache();
        
        Bukkit.getPluginManager().registerEvents(new History(), this);

        try {
            FastInvManager.register(this);
        } catch (Exception ignored) {}
    }

}