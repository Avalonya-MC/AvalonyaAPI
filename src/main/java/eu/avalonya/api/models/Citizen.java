package eu.avalonya.api.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.avalonya.api.items.ItemAccess;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Date;

/**
 * Citizen model class that represents a citizen in the Avalonya api.
 */
@DatabaseTable(tableName = "citizens")
public class Citizen implements ItemAccess {

    @DatabaseField(canBeNull = false, columnName = "uuid", foreign = true, foreignAutoRefresh = true)
    @Getter
    private AvalonyaPlayer player;

    @DatabaseField(defaultValue = "0", dataType = DataType.FLOAT)
    @Getter
    @Setter
    private float money;

    @DatabaseField(columnName= "joined_at")
    @Getter
    @Setter
    private Date joinedAt;

    @DatabaseField(columnName = "town_id", foreign = true, foreignAutoRefresh = true)
    private Town town;

    private ItemStack playerHead;

    @DatabaseField(columnName = "role")
    private int role;

    public Citizen()
    {
        // Required by ORMLite
    }

    public Citizen(AvalonyaPlayer player) {
        this.player = player;
        this.playerHead = new ItemStack(Material.PLAYER_HEAD);

        final SkullMeta itemMeta = (SkullMeta) playerHead.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setOwningPlayer(player.getPlayer());
            itemMeta.displayName(Component.text("§f" + player.getPseudo()));
            playerHead.setItemMeta(itemMeta);
        }
    }

    public boolean isMayor() {
        return town.getMayor().equals(this);
    }

    public void setTown(Town town) {
        this.town = town;
        this.joinedAt = new Date();
    }

    public Town getTown() {
        return town;
    }

    public boolean isPlayer(Player player) {
        final Player p = this.player.getPlayer();

        if (p == null) {
            return false;
        }

        return p.equals(player);
    }

    public void setRole(Role roleId)
    {
        this.role = roleId.ordinal();
    }

    public Role getRole()
    {
        return Role.values()[role];
    }

    @Override
    public ItemStack toItemStack() {
        return playerHead;
    }

    public boolean equals(Citizen citizen) {
        if (citizen == null) {
            return false;
        }

        return citizen.getPlayer().getPseudo().equals(player.getPseudo()) && citizen.getJoinedAt().equals(joinedAt);
    }
}
