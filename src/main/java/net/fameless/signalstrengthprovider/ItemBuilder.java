package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
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

        List<String> lores = signalStrengthProvider.getLanguageFile().getStringList("item.lore");
        List<Component> loreComponents = new ArrayList<>();

        for (String s : lores) {
            String toMineDown = s.replace("${strength}", String.valueOf(currentStrength - 1));
            loreComponents.add(new MineDown(toMineDown).toComponent());
        }

        barrelMeta.lore(loreComponents);

        String name = signalStrengthProvider.getLanguageFile().contains("item.name") ?
                signalStrengthProvider.getLanguageFile().getString("item.name") :
                "&light_purple&" + (currentStrength - 1);

        assert name != null;
        String nameToMineDown = name.replace("${strength}", String.valueOf(currentStrength - 1));
        barrelMeta.displayName(new MineDown(nameToMineDown).toComponent());

        int slot = 0, i = 0;

        while (i < minecarts) {
            if (slot > 26) break;
            ItemStack stack = barrel.getInventory().getItem(slot);
            if (stack == null) {
                barrel.getInventory().setItem(slot, new ItemStack(Material.MINECART));
            } else {
                if (!stack.getType().equals(Material.MINECART)) {
                    slot++;
                    continue;
                }
                stack.setAmount(stack.getAmount() + 1);
                if (stack.getAmount() == 127) slot++;
            }
            i++;
        }

        slot = 0;
        i = 0;

        while (i < items) {
            if (slot > 26) break;
            ItemStack stack= barrel.getInventory().getItem(slot);
            if (stack == null) {
                barrel.getInventory().setItem(slot, new ItemStack(Material.WHITE_CONCRETE));
            } else {
                if (!stack.getType().equals(Material.WHITE_CONCRETE)) {
                    slot++;
                    continue;
                }
                stack.setAmount(stack.getAmount() + 1);
                if (stack.getAmount() == 127) slot++;
            }
            i++;
        }

        barrelMeta.setBlockState(barrel);
        barrelItem.setItemMeta(barrelMeta);
        return barrelItem;
    }
}
