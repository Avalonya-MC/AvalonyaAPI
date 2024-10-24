package eu.avalonya.api.models.towny;

import eu.avalonya.api.items.ItemStackAdapter;
import eu.avalonya.api.models.AbstractModel;
import eu.avalonya.api.models.towny.enums.TownPermission;
import eu.avalonya.api.repository.CitizenRepository;
import eu.avalonya.api.repository.PlotRepository;
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

import java.util.*;

/**
 * Town model class that represents a town in the Avalonya api.
 */
@Getter
@Setter
@NoArgsConstructor
public class Town extends AbstractModel implements ItemStackAdapter {

    private int id;
    private String name;
    private String politicalStatus = "";
    private float money = 0.0f;
    private float taxes = 0.0f;
    private int permissions = 0;
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

    public PlotRepository getPlots() {
        return new PlotRepository(List.of(this.name));
    }

    /**
     * @param player le joueur essayant de revendiquer le chunk
     * @param chunk le chunk visé
     */
    public void claim(Player player, Chunk chunk) {
        // TODO:
        //  - Verifier si le joueur a la permission pour faire ceci
        //  - Verifier si le chunk n'est pas deja claim

        Plot plot = new Plot(chunk, this);

        this.addPlot(plot);
    }

    /**
     * @param chunk le chunk à supprimer
     */
    public void unclaim(Player player, Chunk chunk) {
        // TODO:
        //  - Verifier si le joueur a la permission pour faire ceci
        //  - Verifier que le chunk est bien claim par la ville

        Plot plot = this.getPlots().get(String.format("%d-%d", chunk.getX(), chunk.getZ())); // TODO : Faire les changement necessaire pour que l'id d'un plot soit "x-y"

        if (plot != null) {
            this.getPlots().delete(plot);
        }
    }

    public boolean hasPermission(TownPermission permission) {
        return (this.permissions & (1 << permission.ordinal())) != 0;
    }

    public void addPermission(TownPermission permission) {
        this.permissions |= (1 << permission.ordinal());
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

    public Citizen getMayor() {
        return this.getCitizens().all().getFirst(); // TODO: Changer le comportement pour prendre le citoyens qui possede le role de maire
    }

    @Deprecated
    public void addPlot(Plot plot) {
        this.getPlots().save(plot);
    }

    public static Town of(Player player) {
        return null;
    }
}
