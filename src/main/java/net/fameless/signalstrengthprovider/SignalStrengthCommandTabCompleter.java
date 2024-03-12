package net.fameless.signalstrengthprovider;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignalStrengthCommandTabCompleter implements TabCompleter {

    private final List<String> containers = new ArrayList<>();

    public SignalStrengthCommandTabCompleter() {
        for (Material type : Material.values()) {
            if (!(type.equals(Material.BARREL) || type.equals(Material.BLAST_FURNACE) || type.equals(Material.BREWING_STAND) ||
                    type.equals(Material.CHEST) || type.equals(Material.DISPENSER) || type.equals(Material.DROPPER) ||
                    type.equals(Material.FURNACE) || type.equals(Material.HOPPER) || type.name().contains("SHULKER_BOX") ||
                    type.equals(Material.SMOKER) || type.equals(Material.TRAPPED_CHEST))) {
                continue;
            }
            containers.add(type.name());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Collections.singleton("max"), new ArrayList<>());
        }
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], containers, new ArrayList<>());
        }
        return null;
    }
}
