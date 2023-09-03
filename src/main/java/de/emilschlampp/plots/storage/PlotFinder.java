package de.emilschlampp.plots.storage;

import de.emilschlampp.plots.utils.MathSys;
import de.emilschlampp.plots.utils.ObjectPair;
import org.bukkit.Location;

public class PlotFinder {
    private static boolean a = false;

    public static String findPlot(Location middle) {
        String ab = MathSys.getPlot(middle);
        Location b = middle.clone();
        while (ab.equalsIgnoreCase("road")) {
            ab = MathSys.getPlot(b.add(1, 0, 1));
        }
        if (!StorageMain.hasOwner(ab)) {
            return ab;
        } else {
            ObjectPair<Integer, Integer> o = MathSys.getPlot(ab);
            int px = o.a;
            int pz = o.b;

            int mx = o.a;
            int mz = o.b;
            while (true) {
                a = !a;
                if (a) {
                    px = px + 1;
                    mx = mx - 1;
                } else {
                    pz = pz + 1;
                    mz = mz - 1;
                }
                if (!StorageMain.hasOwner(px + ";" + pz)) {
                    return px + ";" + pz;
                }
                if (!StorageMain.hasOwner(mx + ";" + mz)) {
                    return mx + ";" + mz;
                }
            }
        }
    }
}
