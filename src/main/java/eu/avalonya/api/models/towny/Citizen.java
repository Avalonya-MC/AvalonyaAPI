package eu.avalonya.api.models.towny;

import eu.avalonya.api.items.ItemStackAdapter;
import eu.avalonya.api.models.AbstractModel;
import eu.avalonya.api.models.AvaPlayer;
import eu.avalonya.api.repository.CitizenRepository;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.Map;

/**
 * Citizen model class that represents a citizen in the Avalonya api.
 */
@Getter
@Setter
@NoArgsConstructor
public class Citizen extends AbstractModel implements ItemStackAdapter {

    private String uuid;
    private Town town;
    private long joinedAt;

    // Obsolete properties
    private int id;
    private AvaPlayer player;
    private float money;
    private int role;
    private ItemStack playerHead;

    public Citizen(String uuid, Town town, long joinedAt) {
        this.uuid = uuid;
        this.town = town;
        this.joinedAt = joinedAt;
    }

    @Override
    public Pair<String, String> getId() {
        return Pair.of("citizen_id", uuid);
    }

    @Override
    public Map<String, String> getRepositoryAttributes() {
        return Map.of(
                "name", town.getName()
        );
    }

    public static Citizen deserialize(Map<String, Object> map) {
        final Citizen citizen = new Citizen();

        citizen.setUuid((String) map.get("uuid"));
        citizen.setJoinedAt(Double.valueOf((double) map.get("joined_at")).longValue());

        return citizen;
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "uuid", this.uuid,
                "joined_at", joinedAt
        );
    }

    @Override
    public ItemStack toItemStack() {
        return null;
    }

    public Date getJoinedAt() {
        return new Date(joinedAt);
    }

    public boolean isMayor() {
        return this.getTown().getMayor().getId().value().equals(this.uuid);
    }

    public static Citizen from(AvaPlayer player) {
        return CitizenRepository.findById(player.getUuid());
    }

    public static Citizen from(Player player) {
        return CitizenRepository.findById(player.getUniqueId().toString());
    }
}
