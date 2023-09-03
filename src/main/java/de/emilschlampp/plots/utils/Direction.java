package de.emilschlampp.plots.utils;

import org.bukkit.Location;

public enum Direction {
    X, NX, Z, NZ, HERE;
    public static Direction getRelative(Location a, Location b) {
        if(a.getBlockX() == b.getBlockX()) {
            if(a.getBlockZ() > b.getBlockZ()) {
                return NZ;
            }
            if(a.getBlockZ() < b.getBlockZ()) {
                return Z;
            }
            if(a.getBlockZ() == b.getBlockZ()) {
                return HERE;
            }
        }
        if(a.getBlockZ() == b.getBlockZ()) {
            if(a.getBlockX() > b.getBlockX()) {
                return NX;
            }
            if(a.getBlockX() < b.getBlockX()) {
                return X;
            }
            if(a.getBlockX() == b.getBlockX()) {
                return HERE;
            }
        }
        return HERE;
    }
}
