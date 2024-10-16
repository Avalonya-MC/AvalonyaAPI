package eu.avalonya.api.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Un plot est une parcelle de terrain (un chunk) qui peut être achetée par un joueur,
 * avoir différents types ainsi que des permissions spécifiques.
 *
 * @version 1.0
 * @see eu.avalonya.api.models.PlotType
 */
@DatabaseTable(tableName = "plots")
@Getter
@NoArgsConstructor
public class Plot
{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "town_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    @Setter
    private Town town;

    @DatabaseField(canBeNull = false)
    private int x;

    @DatabaseField(canBeNull = false)
    private int z;

    @DatabaseField
    private String name;

    @DatabaseField(defaultValue = "0")
    private int type;

    @DatabaseField(defaultValue = "0")
    private int permissions;

    @DatabaseField(defaultValue = "false", columnName = "is_outpost")
    @Setter
    private boolean isOutpost;

    public Plot(Chunk chunk, Town town)
    {
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.town = town;
    }

    public Chunk getChunk(World world)
    {
        return world.getChunkAt(x, z);
    }

    public Town getTown()
    {
        return town;
    }

}
