package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.minecodes.plots.api.plot.PlotApi;
import pl.minecodes.plots.api.plot.PlotServiceApi;

public class RTP_MinePlots implements RegionPluginCheck{
    // NOT TESTED (3.6.6)
    // MinePlots- (v4.0.1)
    // https://builtbybit.com/resources/mineplots.21646/

    private PlotServiceApi plotServiceApi;

    public boolean check(Location loc) {
        boolean result = true;
        if (REGIONPLUGINS.MINEPLOTS.isEnabled())
            try {
                plotServiceApi = Bukkit.getServicesManager().load(PlotServiceApi.class);
                if (plotServiceApi == null)
                    return true;

                PlotApi plot = plotServiceApi.getPlot(loc);
                result = plot == null;
            } catch (Throwable e) {
                // Third-party protection hooks must not make RTP itself fail.
                BetterRTP.getInstance().getLogger().warning(
                        "[MinePlots Respect] Unable to check plot: " + e.getMessage());
            }
        return result;
    }
}
