package net.fameless.signalstrengthprovider;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SignalStrengthCommand implements CommandExecutor {

    private final SignalStrengthProvider signalStrengthProvider;
    private final Message message;
    private final ItemBuilder itemBuilder;

    public SignalStrengthCommand(SignalStrengthProvider signalStrengthProvider, Message message, ItemBuilder itemBuilder) {
        this.signalStrengthProvider = signalStrengthProvider;
        this.message = message;
        this.itemBuilder = itemBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("signalstrength.command.give")) {
            sender.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.NO_PERMISSION, "signalstrength.command.give")));
            return false;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.NOT_A_PLAYER)));
            return false;
        }
        if (args.length == 0) {
            player.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.COMMAND_USAGE)));
            return false;
        }

        int signalStrength;
        if (args[0].equals("max")) {
            signalStrength = 1776;
        } else {
            try {
                signalStrength = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.NOT_A_NUMBER)));
                return false;
            }
        }

        if (signalStrength < 0) {
            signalStrength = signalStrength * (-1);
        }

        if (signalStrength > 1776) {
            player.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.NUMBER_TOO_HIGH, signalStrength)));
            return false;
        }

        player.getInventory().addItem(itemBuilder.buildSignalStrength(signalStrength));
        player.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.RECEIVED, signalStrength)));

        return false;
    }
}
