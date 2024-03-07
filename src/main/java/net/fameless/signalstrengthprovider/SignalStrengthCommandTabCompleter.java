package net.fameless.signalstrengthprovider;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SignalStrengthCommandTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> matches = new ArrayList<>();
            matches.add("max");
            matches.add("min");
            return StringUtil.copyPartialMatches(args[0], matches, new ArrayList<>());
        }
        return null;
    }
}
