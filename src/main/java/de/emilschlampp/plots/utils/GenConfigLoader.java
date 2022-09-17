package de.emilschlampp.plots.utils;

import de.emilschlampp.plots.Plots;
import de.emilschlampp.plots.commands.PlotMainCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GenConfigLoader {
    private static File file = new File(Plots.instance.getDataFolder(), "gendata.yml");
    public static YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    public static boolean issetup() {
        return yamlConfiguration.getBoolean("setup", true);
    }

    public static void startSetup() {
        Setup.setAllblocks();
        Plots.instance.getCommand("plot").setTabCompleter(new Setup());
        Plots.instance.getCommand("plot").setExecutor(new Setup());
    }

    public static void loadData() {
        try {
            math_sys.pw = yamlConfiguration.getInt("pw", 50);
            math_sys.pheight = yamlConfiguration.getInt("pheight", 80);
            math_sys.rw = yamlConfiguration.getInt("rw", 7);
            math_sys.plotworld = yamlConfiguration.getString("plotworld", "plots");
            math_sys.PLOTFLOORTOP = Material.valueOf(yamlConfiguration.getString("PLOTFLOORTOP", Material.GRASS_BLOCK.name()));
            math_sys.PLOTFLOOR = Material.valueOf(yamlConfiguration.getString("PLOTFLOOR", Material.DIRT.name()));
            math_sys.STREET = Material.valueOf(yamlConfiguration.getString("STREET", Material.STONE.name()));
            math_sys.WALLBLOCK = Material.valueOf(yamlConfiguration.getString("WALLBLOCK", Material.STONE.name()));
            math_sys.WALLBLOCKCLAIMED = Material.valueOf(yamlConfiguration.getString("WALLBLOCKCLAIMED", Material.STONE.name()));
            math_sys.BORDERBLOCK = Material.valueOf(yamlConfiguration.getString("BORDERBLOCK", Material.STONE_SLAB.name()));
            math_sys.BORDERBLOCKCLAIMED = Material.valueOf(yamlConfiguration.getString("BORDERBLOCKCLAIMED", Material.BRICK_SLAB.name()));
        } catch (Exception exception) {

        }
    }


    private static class Setup implements TabExecutor {

        private static Map<String, Material> allblocks = new HashMap<>();

        public static void setAllblocks() {
            allblocks = new HashMap<>();
            for(Material m : Material.values()) {
                if(m.isBlock()) {
                    allblocks.put(m.getKey().getKey(), m);
                    allblocks.put(m.getKey().getNamespace()+":"+m.getKey().getKey(), m);
                }
            }
        }

        private static int step = 0;

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if(!sender.hasPermission("splots.admin")) {
                sender.sendMessage("§6Das Plugin befindet sich im Setup!");
                return true;
            }
            if(args.length == 1) {
                if (args[0].equals("r")) {
                    step = 0;
                    sender.sendMessage("§cAbgebrochen.");
                    return true;
                }
            }
            if(step == 0) {
                sender.sendMessage("§6Woraus sollen die Plots bestehen? Standard: "+math_sys.PLOTFLOOR.getKey().getKey());
                sender.sendMessage("§a/plot <material>");
                step++;
                return true;
            }
            if(step == 1) {
                if(args.length == 1) {
                    if(allblocks.get(args[0]) == null) {
                        sender.sendMessage("§cUngültiger Block!");
                        return true;
                    }
                    yamlConfiguration.set("PLOTFLOOR", allblocks.get(args[0]).name());
                    sender.sendMessage("§6Was soll der oberste Block von den Plots sein? Standard: "+math_sys.PLOTFLOORTOP.getKey().getKey());
                    step++;
                } else {
                    sender.sendMessage("§6Woraus sollen die Plots bestehen?");
                    sender.sendMessage("§a/plot <material>");
                }
                return true;
            }
            if(step == 2) {
                if(args.length == 1) {
                    if(allblocks.get(args[0]) == null) {
                        sender.sendMessage("§cUngültiger Block!");
                        return true;
                    }
                    yamlConfiguration.set("PLOTFLOORTOP", allblocks.get(args[0]).name());
                    sender.sendMessage("§6Woraus sollen die Straßen bestehen? Standard: "+math_sys.STREET.getKey().getKey());
                    step++;
                } else {
                    sender.sendMessage("§6Was soll der oberste Block von den Plots sein?");
                    sender.sendMessage("§a/plot <material>");
                }
                return true;
            }
            if(step == 3) {
                if(args.length == 1) {
                    if(allblocks.get(args[0]) == null) {
                        sender.sendMessage("§cUngültiger Block!");
                        return true;
                    }
                    yamlConfiguration.set("STREET", allblocks.get(args[0]).name());
                    sender.sendMessage("§6Woraus sollen die Claim-Ränder bestehen? Standard: "+math_sys.BORDERBLOCKCLAIMED.getKey().getKey());
                    step++;
                } else {
                    sender.sendMessage("§6Woraus sollen die Straßen bestehen?");
                    sender.sendMessage("§a/plot <material>");
                }
                return true;
            }
            if(step == 4) {
                if(args.length == 1) {
                    if(allblocks.get(args[0]) == null) {
                        sender.sendMessage("§cUngültiger Block!");
                        return true;
                    }
                    yamlConfiguration.set("BORDERBLOCKCLAIMED", allblocks.get(args[0]).name());
                    sender.sendMessage("§6Woraus sollen die normalen Ränder bestehen? Standard: "+math_sys.BORDERBLOCK.getKey().getKey());
                    step++;
                } else {
                    sender.sendMessage("§6Woraus sollen die Claim-Ränder bestehen?");
                    sender.sendMessage("§a/plot <material>");
                }
                return true;
            }
            if(step == 5) {
                if(args.length == 1) {
                    if(allblocks.get(args[0]) == null) {
                        sender.sendMessage("§cUngültiger Block!");
                        return true;
                    }
                    yamlConfiguration.set("BORDERBLOCK", allblocks.get(args[0]).name());
                    sender.sendMessage("§6Woraus sollen die Claim-Wände bestehen? Standard: "+math_sys.WALLBLOCKCLAIMED.getKey().getKey());
                    step++;
                } else {
                    sender.sendMessage("§6Woraus sollen die normalen Ränder bestehen?");
                    sender.sendMessage("§a/plot <material>");
                }
                return true;
            }
            if(step == 6) {
                if(args.length == 1) {
                    if(allblocks.get(args[0]) == null) {
                        sender.sendMessage("§cUngültiger Block!");
                        return true;
                    }
                    yamlConfiguration.set("WALLBLOCKCLAIMED", allblocks.get(args[0]).name());
                    sender.sendMessage("§6Woraus sollen die normalen Wände bestehen? Standard: "+math_sys.WALLBLOCK.getKey().getKey());
                    step++;
                } else {
                    sender.sendMessage("§6Woraus sollen die Claim-Wände bestehen?");
                    sender.sendMessage("§a/plot <material>");
                }
                return true;
            }
            if(step == 7) {
                if(args.length == 1) {
                    if(allblocks.get(args[0]) == null) {
                        sender.sendMessage("§cUngültiger Block!");
                        return true;
                    }
                    yamlConfiguration.set("WALLBLOCK", allblocks.get(args[0]).name());
                    sender.sendMessage("§6Wie breit soll die Straße sein? Standard: "+math_sys.rw);
                    step++;
                } else {
                    sender.sendMessage("§6Woraus sollen die normalen Wände bestehen?");
                    sender.sendMessage("§a/plot <material>");
                }
                return true;
            }
            if(step == 8) {
                if(args.length == 1) {
                    try {
                        yamlConfiguration.set("rw", Integer.parseInt(args[0]));
                        sender.sendMessage("§6Wie breit sollen die Plots sein? Standard: "+math_sys.pw);
                        step++;
                    } catch (Exception e) {
                        sender.sendMessage("§cUngültige Zahl!");
                    }

                } else {
                    sender.sendMessage("§6Wie breit soll die Straße sein?");
                    sender.sendMessage("§a/plot <int>");
                }
                return true;
            }
            if(step == 9) {
                if(args.length == 1) {
                    try {
                        yamlConfiguration.set("pw", Integer.parseInt(args[0]));
                        sender.sendMessage("§6Wie hoch soll die Straße und die Plots sein? Standard: "+math_sys.pheight);
                        step++;
                    } catch (Exception e) {
                        sender.sendMessage("§cUngültige Zahl!");
                    }

                } else {
                    sender.sendMessage("§6Wie breit sollen die Plots sein?");
                    sender.sendMessage("§a/plot <int>");
                }
                return true;
            }
            if(step == 10) {
                if(args.length == 1) {
                    try {
                        yamlConfiguration.set("pheight", Integer.parseInt(args[0]));
                        sender.sendMessage("§6Wie soll die Welt heißen? Standard: "+math_sys.plotworld);
                        step++;
                    } catch (Exception e) {
                        sender.sendMessage("§cUngültige Zahl!");
                    }

                } else {
                    sender.sendMessage("§6Wie hoch soll die Straße und die Plots sein?");
                    sender.sendMessage("§a/plot <int>");
                }
                return true;
            }
            if(step == 11) {
                if(args.length == 1) {
                    step = 0;
                    yamlConfiguration.set("plotworld", args[0]);
                    yamlConfiguration.set("setup", false);
                    try {
                        yamlConfiguration.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        sender.sendMessage("§cBeim Speichern trat ein Problem auf: "+e.getMessage());
                        return true;
                    }
                    sender.sendMessage("§aErfolgreich!");
                    Plots.instance.onEnable();
                } else {
                    sender.sendMessage("§6Wie hoch soll die Welt heißen?");
                    sender.sendMessage("§a/plot <name>");
                }
                return true;
            }
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            if(step < 8) {
                return PlotMainCommand.copyMatches(allblocks.keySet().stream().collect(Collectors.toList()), args[args.length-1]);
            }
            return new ArrayList<>();
        }
    }
}
