package lt.wayout.minecraft.plugin.wayengine.util;

import org.bukkit.Location;

public class LocationUtils {

    private LocationUtils() {}

    public static double distance2D(Location startLoc, Location endLoc) {
        return Math.sqrt(LocationUtils.distance2DSquared(startLoc, endLoc));
    }

    public static double distance2DSquared(Location startLoc, Location endLoc) {
        if (startLoc == null || endLoc == null || startLoc.getWorld() != endLoc.getWorld())
            throw new IllegalArgumentException("Failed to calculate 2D distance between two locations! Bukkit World object references cannot be null or non equal!");
        return Math.pow(Math.abs(endLoc.getX() - startLoc.getX()), 2D) + Math.pow(Math.abs(endLoc.getZ() - startLoc.getZ()), 2D);
    }
}
