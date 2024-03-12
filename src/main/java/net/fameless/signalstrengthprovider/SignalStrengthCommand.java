package net.fameless.signalstrengthprovider;

import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

public class SignalStrengthCommand implements CommandExecutor {

    private final SignalStrengthProvider signalStrengthProvider;
    private final Message message;
    private final ItemBuilder itemBuilder;
    private final NotAvailableGUI notAvailableGUI;

    public SignalStrengthCommand(SignalStrengthProvider signalStrengthProvider, Message message, ItemBuilder itemBuilder, NotAvailableGUI notAvailableGUI) {
        this.signalStrengthProvider = signalStrengthProvider;
        this.message = message;
        this.itemBuilder = itemBuilder;
        this.notAvailableGUI = notAvailableGUI;
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

        ItemStack containerStack = new ItemStack(Material.BARREL);
        if (args.length == 2) {
            Material type = Material.matchMaterial(args[1]);
            if (type == null) {
                type = Material.BARREL;
                player.sendMessage(message.getMessage(MessageType.INVALID_CONTAINER));
            } else {
                if (!(type.equals(Material.BARREL) || type.equals(Material.BLAST_FURNACE) || type.equals(Material.BREWING_STAND) ||
                        type.equals(Material.CHEST) || type.equals(Material.DISPENSER) || type.equals(Material.DROPPER) ||
                        type.equals(Material.FURNACE) || type.equals(Material.HOPPER) || type.name().contains("SHULKER_BOX") ||
                        type.equals(Material.SMOKER) || type.equals(Material.TRAPPED_CHEST))) {
                    type = Material.BARREL;
                    player.sendMessage(message.getMessage(MessageType.INVALID_CONTAINER));
                }
            }
            containerStack = new ItemStack(type);
        }

        Container container = (Container) ((BlockStateMeta) containerStack.getItemMeta()).getBlockState();
        int max = 1 + (container.getInventory().getSize() * 127 / container.getInventory().getSize()) * 14;
        int signalStrength;
        if (args[0].equals("max")) {
            signalStrength = max;
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

        if (signalStrength > max) {
            player.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.NUMBER_TOO_HIGH, signalStrength)));
            return false;
        }

        try {
            player.getInventory().addItem(itemBuilder.buildSignalStrength(signalStrength, containerStack));
        } catch (Exception e) {
            player.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.NOT_AVAILABLE, signalStrength)));
            player.openInventory(notAvailableGUI.getNotAvailableInventory(signalStrength, containerStack));
            return false;
        }
        player.sendMessage(signalStrengthProvider.getPrefix().append(message.getMessage(MessageType.RECEIVED, signalStrength)));

        return false;
    }
}
