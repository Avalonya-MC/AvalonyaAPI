package eu.avalonya.api.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.sql.Cache;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Date;

@DatabaseTable(tableName = "mariage")
@Getter
@Setter
public class Mariage
{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "sender_id", foreign = true, foreignAutoRefresh = true)
    private Citizen sender;

    @DatabaseField(columnName = "receiver_id", foreign = true, foreignAutoRefresh = true)
    private Citizen receiver;

    @DatabaseField(columnName= "created_at")
    private Date createdAt;

    private BukkitTask bukkitTask;

    public Mariage()
    {
        this.bukkitTask = this.createRunnable();
    }

    public BukkitTask createRunnable()
    {
        return new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Cache.mariagesRequest.remove(Mariage.this);
                if (Bukkit.getPlayer(Mariage.this.getSender().getPlayer().getPlayer().getName()) != null)
                    Mariage.this.getSender().getPlayer().getPlayer().sendMessage("§c" + Mariage.this.getSender().getPlayer().getPlayer().getName() + " n'a pas accepté à temps votre demande en mariage.");
            }
        }.runTaskLater(AvalonyaAPI.getInstance(), 20 * 60);
    }

}
