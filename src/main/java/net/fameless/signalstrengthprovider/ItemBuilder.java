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

        List<String> lores = signalStrengthProvider.getLanguageFile().getStringList("item.lore");
        List<Component> loreComponents = new ArrayList<>();

        if (!lores.isEmpty()) {
            for (String s : lores) {
                String toMineDown = s.replace("${strength}", String.valueOf(signalStrength));
                loreComponents.add(new MineDown(toMineDown).toComponent());
            }
        }

        containerMeta.lore(loreComponents);

        String name = signalStrengthProvider.getLanguageFile().get("item.name") != null ?
                signalStrengthProvider.getLanguageFile().getString("item.name") :
                "&light_purple&" + (signalStrength);

        String nameToMineDown = name.replace("${strength}", String.valueOf(signalStrength));
        containerMeta.displayName(new MineDown(nameToMineDown).toComponent());

        int requiredMinecarts = (int) Math.ceil(Math.max(signalStrength, Math.ceil(((double) (container.getInventory().getSize() * 64) / 14) * (signalStrength - 1))) / 64);

        for (int i = 0; i < container.getInventory().getSize(); i++) {
            int amount = Math.min(127, requiredMinecarts);
            container.getInventory().setItem(i, new ItemStack(Material.MINECART));
            container.getInventory().getItem(i).setAmount(amount);
            requiredMinecarts -= amount;
        }

        containerMeta.setBlockState(container);
        itemStack.setItemMeta(containerMeta);
        return itemStack;
    }
}
