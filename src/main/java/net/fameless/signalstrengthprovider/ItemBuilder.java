package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
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
        ItemStack itemStack = new ItemStack(Material.BARREL);
        BlockStateMeta containerMeta = (BlockStateMeta) itemStack.getItemMeta();
        BlockState containerState = containerMeta.getBlockState();
        Container container = (Container) containerState;

        double requiredItems = Math.max(signalStrength, Math.ceil(((double) (container.getInventory().getSize() * 64) / 14) * (signalStrength - 1)));
        int minecarts = (int) Math.ceil(requiredItems / 64);

        List<String> lores = signalStrengthProvider.getLanguageFile().getStringList("item.lore");
        List<Component> loreComponents = new ArrayList<>();

        for (String s : lores) {
            String toMineDown = s.replace("${strength}", String.valueOf(signalStrength));
            loreComponents.add(new MineDown(toMineDown).toComponent());
        }

        containerMeta.lore(loreComponents);

        String name = signalStrengthProvider.getLanguageFile().get("item.name") != null ?
                signalStrengthProvider.getLanguageFile().getString("item.name") :
                "&light_purple&" + (signalStrength);

        String nameToMineDown = name.replace("${strength}", String.valueOf(signalStrength));
        containerMeta.displayName(new MineDown(nameToMineDown).toComponent());

        int slot = 0, i = 0;

        while (i < minecarts) {
            if (slot > container.getInventory().getSize() - 1) break;
            ItemStack stack = container.getInventory().getItem(slot);
            if (stack == null) {
                container.getInventory().setItem(slot, new ItemStack(Material.MINECART));
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

        containerMeta.setBlockState(container);
        itemStack.setItemMeta(containerMeta);
        return itemStack;
    }
}
