package eu.avalonya.api.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.avalonya.api.items.ItemAccess;
import eu.avalonya.api.models.dao.PlotDao;
import eu.avalonya.api.repository.CitizenRepository;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.sql.SQLException;
import java.util.*;

/**
 * Town model class that represents a town in the Avalonya api.
 */
@Getter
@Setter
@NoArgsConstructor
public class Town extends AbstractModel implements ItemAccess {

    private int id;
    private String name;
    private String politicalStatus = "";
    private float money = 0.0f;
    private float taxes = 0.0f;
    private boolean taxesEnabled = false;
    private boolean spawnHostileMob = false;
    private boolean fireSpread = false;
    private boolean explosions = false;
    private boolean publicTown = false;
    private boolean friendlyFire = false;
    private String spawnLocation;
    private Date createdAt;

    public CitizenRepository getCitizens() {
        return new CitizenRepository(List.of(this.name));
    }

    public float deposit(float amount) {
        return money += amount;
    }

    public float withdraw(float amount) {
        return money -= amount;
    }

    public float increaseTaxes(float amount) {
        this.taxes += amount;

        return taxes;
    }

    public float decreaseTaxes(float amount) {
        this.taxes -= amount;

        return taxes;
    }

    public List<Plot> getPlots()
    {
        try {
            return PlotDao.getPlots(this);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: Utiliser le logger
        }

        return new ArrayList<>();
    }

    /**
     * Ajoute un chunk à la ville ou change le propriétaire du chunk si il est déjà revendiqué.
     * @param chunk
     */
    public void addPlot(Chunk chunk)
    {
        try
        {
            if (PlotDao.isClaimed(chunk))
            {
                Plot plot = PlotDao.getPlot(chunk);

                if (plot.getTown().equals(this))
                {
                    return;
                }

                plot.setTown(this);
                PlotDao.update(plot);
            }
            else
            {
                PlotDao.create(this, chunk);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); // TODO: Utiliser le logger
        }
    }

    /**
     * Action que peut effectuer une ville sur un chunk.
     * Ainsi une sertraine action peut être effectuée sur un chunk.
     * @param player le joueur essayant de revendiquer le chunk
     * @param chunk le chunk visé
     */
    public void claim(Player player, Chunk chunk)
    {
        try
        {
            if (PlotDao.isClaimed(chunk))
            {
                player.sendMessage(Component.text("§cCe chunk est déjà revendiqué."));
                return;
            }

            addPlot(chunk);
        }
        catch (SQLException e)
        {
            e.printStackTrace(); // TODO: Utiliser le logger
        }
    }

    /**
     * Supprime un chunk de la ville.
     * @param chunk le chunk à supprimer
     */
    public void unclaim(Player player, Chunk chunk)
    {
        try
        {
            if (!PlotDao.hasTown(chunk, this))
            {
                player.sendMessage(Component.text("§cCe chunk n'appartient pas à votre ville."));
                return;
            }

            PlotDao.delete(chunk);
        }
        catch (SQLException e)
        {
            e.printStackTrace(); // TODO: Utiliser le logger
        }
    }

    public boolean hasTaxesEnabled() {
        return taxesEnabled;
    }

    public boolean hasSpawnHostileMob() {
        return spawnHostileMob;
    }

    public boolean hasFireSpread() {
        return fireSpread;
    }

    public boolean hasExplosions() {
        return explosions;
    }

    public void setPublic(boolean publicTown) {
        this.publicTown = publicTown;
    }

    public boolean isPublic() {
        return publicTown;
    }

    public boolean hasFriendlyFire() {
        return friendlyFire;
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(Material.WHITE_BANNER);
        final BannerMeta meta = (BannerMeta) item.getItemMeta();

        //meta.setPatterns(bannerPatterns);
        meta.displayName(Component.text("§f§l" + name + " §7Flag"));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Pair<String, String> getId() {
        return Pair.of("town_name", this.name);
    }

    @Override
    public Map<String, String> getRepositoryAttributes() {
        return Map.of(
                "town_name", this.name
        );
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "name", this.name
        );
    }
}
