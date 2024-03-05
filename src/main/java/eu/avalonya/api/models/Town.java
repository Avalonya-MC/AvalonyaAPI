package eu.avalonya.api.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.avalonya.api.exceptions.TownRoleLimiteException;
import eu.avalonya.api.items.ItemAccess;
import eu.avalonya.api.models.dao.PlotDao;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

/**
 * Town model class that represents a town in the Avalonya api.
 */
@DatabaseTable(tableName = "towns")
public class Town implements ItemAccess {

    @DatabaseField(id = true, generatedId = true)
    @Getter
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    @Getter
    @Setter
    private String name;

    @DatabaseField
    @Getter
    @Setter
    private String politicalStatus = "";

    @DatabaseField(defaultValue = "0", dataType = DataType.FLOAT)
    @Getter
    private float money = 0.0f;

    @DatabaseField(defaultValue = "0", dataType = DataType.FLOAT)
    @Getter
    @Setter
    private float taxes = 0.0f;

    @DatabaseField(columnName = "taxes_enabled", defaultValue = "false", dataType = DataType.BOOLEAN)
    @Setter
    private boolean taxesEnabled = false;

    @DatabaseField(columnName = "spawn_hostile_mob", defaultValue = "false", dataType = DataType.BOOLEAN)
    @Setter
    private boolean spawnHostileMob = false;

    @DatabaseField(columnName = "fire_spread", defaultValue = "false", dataType = DataType.BOOLEAN)
    @Setter
    private boolean fireSpread = false;

    @DatabaseField(columnName = "explosions", defaultValue = "false", dataType = DataType.BOOLEAN)
    @Setter
    private boolean explosions = false;

    @DatabaseField(columnName = "public", defaultValue = "false", dataType = DataType.BOOLEAN)
    private boolean publicTown = false;

    @DatabaseField(columnName = "friendly_fire", defaultValue = "false", dataType = DataType.BOOLEAN)
    @Setter
    private boolean friendlyFire = false;

    @Getter
    @Setter
    private Location spawn;

    @Getter
    @Setter
    private Citizen mayor;

    @Getter
    @Setter
    private List<Citizen> citizens = new ArrayList<>();

    @Getter
    @Setter
    private List<Plot> claims = new ArrayList<>();
    private final List<Town> enemies = new ArrayList<>();
    private final List<Town> allies = new ArrayList<>();
    private List<Pattern> bannerPatterns = new ArrayList<>();
    private final List<Role> roles = new ArrayList<>();

    public Town(String name, Citizen mayor) {
        this.name = name;
        this.mayor = mayor;

        citizens.add(mayor);
        try
        {
            claims.add(
                    PlotDao.create(this, mayor.getPlayer().getLocation().getChunk())
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        spawn = createSpawn(mayor.getPlayer().getWorld());
        roles.add(
                new Role("citizen", Role.Color.CITIZEN)
        );
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

    public void addClaim(Chunk chunk) throws SQLException
    {
        claims.add(
                PlotDao.create(this, chunk)
        );
    }

    public void removeClaim(Chunk chunk)
    {
        claims.removeIf(plot -> plot.getX() == chunk.getX() && plot.getZ() == chunk.getZ());
    }

    public boolean hasClaim(Chunk chunk)
    {
        return claims.stream().anyMatch(plot -> plot.getX() == chunk.getX() && plot.getZ() == chunk.getZ());
    }

    public void addCitizen(@NotNull Citizen citizen) {
        citizen.setRole(roles.get(0));
        citizens.add(citizen);
    }

    public void removeCitizen(Citizen citizen) {
        citizens.remove(citizen);
    }

    public Citizen getCitizen(Player player) {
        return getCitizen(player.getUniqueId());
    }

    public Citizen getCitizen(UUID uuid) {
        return citizens.stream().filter(citizen -> citizen.getPlayer().getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public boolean hasCitizen(Player player) {
        return getCitizen(player) != null;
    }

    protected Location createSpawn(World world)
    {
        return claims.get(0).getChunk(world).getBlock(8, 0, 8).getLocation();
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

    public void addEnemy(Town town) {
        enemies.add(town);
    }

    public boolean hasEnemy(Town town) {
        return enemies.contains(town);
    }

    public void removeEnemy(Town town) {
        enemies.remove(town);
    }

    public List<Town> getEnemies() {
        return enemies;
    }

    public void addAlly(Town town) {
        allies.add(town);
    }

    public void removeAlly(Town town) {
        allies.remove(town);
    }

    public boolean hasAlly(Town town) {
        return allies.contains(town);
    }

    public List<Town> getAllies() {
        return allies;
    }

    public void setBannerMeta(List<Pattern> bannerPatterns) {
        this.bannerPatterns = bannerPatterns;
    }

    public List<Pattern> getBannerMeta() {
        return bannerPatterns;
    }

    public void addRole(String name) throws TownRoleLimiteException{

        if (this.roles.size() >= 4) {
            throw new TownRoleLimiteException();
        }
        addRole(new Role(name, Role.Color.values()[this.roles.size()]));
    }

    public void addRole(Role role) throws TownRoleLimiteException {

        if (this.roles.size() >= 4) {
            throw new TownRoleLimiteException();
        }
        this.roles.add(role);
    }

    public Role getRole(Role.Color color) {
        for (Role role : this.roles) {
            if (role.getColor().equalsIgnoreCase(color.getColor())) {
                return role;
            }
        }
        return null;
    }

    public Role getRole(String name) {
        for (Role role : this.roles) {
            if (role.getName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(Material.WHITE_BANNER);
        final BannerMeta meta = (BannerMeta) item.getItemMeta();

        meta.setPatterns(bannerPatterns);
        meta.displayName(Component.text("§f§l" + name + " §7Flag"));
        item.setItemMeta(meta);

        return item;
    }

}
