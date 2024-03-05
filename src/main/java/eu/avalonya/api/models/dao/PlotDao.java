package eu.avalonya.api.models.dao;

import eu.avalonya.api.models.AvalonyaDatabase;
import eu.avalonya.api.models.Plot;
import eu.avalonya.api.models.Town;
import org.bukkit.Chunk;

import java.sql.SQLException;
import java.util.List;

public class PlotDao
{

    public static Plot create(Town town, Chunk chunk) throws SQLException
    {
        Plot plot = new Plot(town, chunk);

        AvalonyaDatabase.getPlotDao().create(plot);

        return plot;
    }

    public static Plot getPlot(Chunk chunk) throws SQLException
    {
        return AvalonyaDatabase.getPlotDao().queryBuilder().where().eq("x", chunk.getX()).and().eq("z", chunk.getZ()).queryForFirst();
    }

    public static List<Plot> getPlots(Town town) throws SQLException
    {
        return AvalonyaDatabase.getPlotDao().queryForEq("town_id", town.getId());
    }

}
