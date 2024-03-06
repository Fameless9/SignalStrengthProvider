package net.fameless.signalstrengthprovider;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final SignalStrengthProvider signalStrengthProvider;

    public ItemBuilder(SignalStrengthProvider signalStrengthProvider) {
        this.signalStrengthProvider = signalStrengthProvider;
    }

    public ItemStack buildSignalStrength(int signalStrength) {
        ItemStack barrelItem = new ItemStack(Material.BARREL);
        BlockStateMeta barrelMeta = (BlockStateMeta) barrelItem.getItemMeta();
        Barrel barrel = (Barrel) barrelMeta.getBlockState();
        barrel.update();

        List<String> lores = signalStrengthProvider.getLanguageFile().getStringList("item.lore");
        List<Component> loreComponents = new ArrayList<>();

        for (String s : lores) {
            String toMiniMessage = s.replace("${strength}", String.valueOf(signalStrength));
            loreComponents.add(MiniMessage.miniMessage().deserialize(toMiniMessage));
        }

        barrelMeta.lore(loreComponents);

        String name = signalStrengthProvider.getLanguageFile().contains("item.name") ?
                signalStrengthProvider.getLanguageFile().getString("item.name") :
                "<purple>" + signalStrength;

        assert name != null;
        String nameToMiniMessage = name.replace("${strength}", String.valueOf(signalStrength));
        Component displayNameComponent = MiniMessage.miniMessage().deserialize(nameToMiniMessage);
        barrelMeta.displayName(displayNameComponent);

        int currentStrength = 0;
        int minecarts = 0;
        int items = 1;

        while (currentStrength <= signalStrength) {
            if (Math.floor(1 + ((double) minecarts / 27) * 14) < signalStrength) {
                minecarts++;
            } else if (Math.floor(1 + ((double) (items / 64) / 27) * 14) < signalStrength) {
                items++;
            }
            currentStrength = (int) (Math.floor(1 + ((double) (items / 64) / 27) * 14) + Math.floor(1 + ((double) minecarts / 27) * 14));
        }

        Bukkit.getLogger().info("Minecarts: " + minecarts);
        Bukkit.getLogger().info("Items: " + items);

        int slot = 0;

        for (int i = 0; i < minecarts; i++) {
            ItemStack stack = barrel.getInventory().getItem(slot);
            if (stack == null) {
                barrel.getInventory().setItem(slot, new ItemStack(Material.MINECART));
            } else {
                if (!stack.getType().equals(Material.MINECART)) {
                    slot++;
                    continue;
                }
                stack.setAmount(stack.getAmount() + 1);
                if (stack.getAmount() == 64) slot++;
            }
        }

        slot = 0;

        for (int i = 0; i < items; i++) {
            ItemStack stack = barrel.getInventory().getItem(slot);
            if (stack == null) {
                barrel.getInventory().setItem(slot, new ItemStack(Material.WHITE_CONCRETE));
            } else {
                if (!stack.getType().equals(Material.WHITE_CONCRETE)) {
                    slot++;
                    continue;
                }
                stack.setAmount(stack.getAmount() + 1);
                if (stack.getAmount() == 64) slot++;
            }
        }

        barrelMeta.setBlockState(barrel);
        barrelItem.setItemMeta(barrelMeta);
        return barrelItem;
    }
}
