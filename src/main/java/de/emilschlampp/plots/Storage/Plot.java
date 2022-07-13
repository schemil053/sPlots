package de.emilschlampp.plots.Storage;

import de.emilschlampp.plots.utils.math_sys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Plot {
    public Plot(Integer x, Integer z, UUID owner, List<UUID> trusted, List<Flag> flags) {
        this.x = x;
        this.z = z;
        this.owner = owner;
        this.trusted = trusted;
        this.flags = flags;
    }

    public void clear() {
        math_sys.clearPlot(id(), false);
    }

    public void delete() {
        math_sys.clearPlot(id(), true);
        StorageMain.removePlot(id());
    }

    public Flag getFlag(String name) {
        for(Flag flag : getFlags()) {
            if (flag.getName() == null) {
                continue;
            }
            if(flag.getName().equalsIgnoreCase(name)) {
                return flag;
            }
        }
        return null;
    }

    public boolean isBooleanFlagSet(String name) {
        boolean a = false;
        for(Flag flag : getFlags()) {
            if(flag.getName() == null) {
                continue;
            }
            if(flag.getName().equalsIgnoreCase(name)) {
                if(flag.getValue() == null) {
                    continue;
                }
                if(flag.getValue().equals("true") && flag.getType().equals(Flag.FlagType.BOOLEAN)) {
                    a = true;
                }
            }
        }
        return a;
    }

    public boolean canBuild(Player player) {
        return isBooleanFlagSet("buildall") || canDoAdmin(player) || player.hasPermission("splots.admin") || trusted.contains(player.getUniqueId()) || player.getUniqueId().equals(owner);
    }

    public boolean canDoAdmin(Player player) {
        return player.hasPermission("splots.admin") || player.getUniqueId().equals(owner);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Integer getX() {
        return x;
    }

    public Integer getZ() {
        return z;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getTrusted() {
        return trusted;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }

    public void setTrusted(List<UUID> trusted) {
        this.trusted = trusted;
    }

    public String id() {
        return x+";"+z;
    }

    public void save() {
        StorageMain.savePlot(this);
    }

    public Location getTPLocation() {
        return math_sys.getTeleportLocation(id());
    }

    public void setClaimBorder() {
        math_sys.setWall(id(), Bukkit.createBlockData(math_sys.WALLBLOCKCLAIMED));
        math_sys.setRand(id(), Bukkit.createBlockData(math_sys.BORDERBLOCKCLAIMED));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plot plot = (Plot) o;
        return Objects.equals(x, plot.x) && Objects.equals(z, plot.z) && Objects.equals(owner, plot.owner) && Objects.equals(trusted, plot.trusted) && Objects.equals(flags, plot.flags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, owner, trusted, flags);
    }

    @Override
    public String toString() {
        return "Plot{" +
                "x=" + x +
                ", z=" + z +
                ", owner=" + owner +
                ", trusted=" + trusted +
                ", flags=" + flags +
                '}';
    }

    private Integer x;
    private Integer z;
    private UUID owner;
    private List<UUID> trusted;
    private List<Flag> flags;


    public static class Flag {
        private String name;
        private String value;
        private FlagType type;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public FlagType getType() {
            return type;
        }

        public void setType(FlagType type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Flag flag = (Flag) o;
            return Objects.equals(name, flag.name) && type == flag.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value, type);
        }

        public Flag(String name, String value, FlagType type) {
            this.name = name;
            this.value = value;
            this.type = type;
        }

        public Flag(String name, FlagType type) {
            this.name = name;
            this.type = type;
        }

        public Flag useclone() {
            return new Flag(name, type);
        }

        public Flag(String des) {
            String[] a = des.split("&F§F&");
            if(a.length == 3) {
                this.name = a[0];
                this.value = a[1];
                this.type = FlagType.valueOf(a[2]);
            }
            if(a.length == 2) {
                this.name = a[0];
                this.type = FlagType.valueOf(a[1]);
            }
        }

        public String toSimpleString() {
            return this.name+": "+this.value;
        }

        @Override
        public String toString() {
            if(value == null) {
                return name+"&F§F&"+type.name();
            }
            return name+"&F§F&"+value+"&F§F&"+type.name();
        }

        public boolean canVal(String value) {
            if(this.type.equals(FlagType.STRING) || this.type.equals(FlagType.ELIST)) {
                return true;
            }
            if(this.type.equals(FlagType.BOOLEAN)) {
                return Arrays.asList("true", "false").contains(value);
            }
            if(this.type.equals(FlagType.INTEGER)) {
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (Exception exception) {
                    return false;
                }
            }
            return false;
        }

        public static enum FlagType {
            BOOLEAN, STRING, INTEGER, ELIST;
        }
    }
}
