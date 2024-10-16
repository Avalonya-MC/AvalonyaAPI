package eu.avalonya.api.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.avalonya.api.items.ItemAccess;
import it.unimi.dsi.fastutil.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Date;
import java.util.Map;

/**
 * Citizen model class that represents a citizen in the Avalonya api.
 */
@Getter
@Setter
public class Citizen extends AbstractModel {

    private String uuid;
    private Town town;
    private long joinedAt;

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

}
