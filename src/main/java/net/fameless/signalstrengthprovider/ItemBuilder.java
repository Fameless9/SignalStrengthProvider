package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final SignalStrengthProvider signalStrengthProvider;
    private ItemStack nonStackable;
    private ItemStack stackableSixteen;
    private ItemStack stackable;

    public ItemBuilder(SignalStrengthProvider signalStrengthProvider) {
        this.signalStrengthProvider = signalStrengthProvider;
        initItems();
    }

    public ItemStack buildSignalStrength(int signalStrength, ItemStack containerStack) throws Exception {
        ItemStack containerItemStack = new ItemStack(containerStack);
        BlockStateMeta containerMeta = (BlockStateMeta) containerItemStack.getItemMeta();
        BlockState containerState = containerMeta.getBlockState();
        Container container = (Container) containerState;

        List<String> lores = signalStrengthProvider.getConfig().getStringList("item.lore");
        List<Component> loreComponents = new ArrayList<>();

        if (!lores.isEmpty()) {
            for (String s : lores) {
                String toMineDown = s.replace("${strength}", String.valueOf(signalStrength));
                loreComponents.add(new MineDown(toMineDown).toComponent());
            }
        }

        containerMeta.lore(loreComponents);

        String name = signalStrengthProvider.getConfig().get("item.name") != null ?
                signalStrengthProvider.getConfig().getString("item.name") :
                "&light_purple&" + (signalStrength);

        if (name != null) {
            String nameToMineDown = name.replace("${strength}", String.valueOf(signalStrength));
            containerMeta.displayName(new MineDown(nameToMineDown).toComponent());
        }

        double requiredItems = Math.max(signalStrength, Math.ceil(((double) container.getInventory().getSize() * 64 / 14) * (signalStrength - 1)));

        for (int i = 0; i < container.getInventory().getSize(); i++) {
            int size = container.getInventory().getSize();

            int amount;
            if (size == 3 || size == 5) {
                amount = (int) Math.min(127, Math.floor(requiredItems / 64));
            } else if (size > 9) {
                amount = (int) Math.min(127, Math.ceil(requiredItems / 64));
            } else if (container.getInventory().getSize() == 9) {
                amount = (int) Math.min(127, Math.round(requiredItems / 64));
            } else {
                amount = 0;
            }

            container.getInventory().setItem(i, nonStackable);
            container.getInventory().getItem(i).setAmount(amount);
            requiredItems -= amount * 64;
        }

        if (isEqualStrength(signalStrength, container.getInventory())) {
            containerMeta.setBlockState(container);
            containerItemStack.setItemMeta(containerMeta);
            return containerItemStack;
        }

        for (int i = 0; i < container.getInventory().getSize(); i++) {
            if (container.getInventory().getItem(i) != null) continue;
            int size = container.getInventory().getSize();

            int amount;
            if (size == 3 || size == 5) {
                amount = (int) Math.min(127, Math.floor(requiredItems / 4));
            } else if (size == 27) {
                amount = (int) Math.min(127, Math.ceil(requiredItems / 4));
            } else if (container.getInventory().getSize() == 9) {
                amount = (int) Math.min(127, Math.round(requiredItems / 4));
            } else {
                amount = 0;
            }

            container.getInventory().setItem(i, stackableSixteen);
            container.getInventory().getItem(i).setAmount(amount);
            requiredItems -= amount * 4;
        }

        if (isEqualStrength(signalStrength, container.getInventory())) {
            containerMeta.setBlockState(container);
            containerItemStack.setItemMeta(containerMeta);
            return containerItemStack;
        }

        for (int i = 0; i < container.getInventory().getSize() && requiredItems > 0; i++) {
            if (container.getInventory().getItem(i) != null) continue;
            int amount = (int) Math.min(127, requiredItems);
            container.getInventory().setItem(i, stackable);
            container.getInventory().getItem(i).setAmount(amount);
            requiredItems -= amount;
        }

        if (isEqualStrength(signalStrength, container.getInventory())) {
            containerMeta.setBlockState(container);
            containerItemStack.setItemMeta(containerMeta);
            return containerItemStack;
        }

        throw new Exception();
    }

    public ItemStack buildItem(ItemStack item, boolean applyFlags, Component name, List<Component> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.displayName(name);

        if (applyFlags) {
            meta.addItemFlags(ItemFlag.values());
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isEqualStrength(int signalStrength, Inventory container) {
        if (container.isEmpty() && signalStrength == 0) {
            return true;
        }
        if (container.isEmpty() && signalStrength != 0) {
            return false;
        }

        double fullness = 0;

        for (int i = 0; i < container.getSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack == null) continue;
            fullness += (double) stack.getAmount() / stack.getType().getMaxStackSize();
        }

        return signalStrength == Math.floor(1 + (fullness / container.getSize()) * 14);
    }

    private void initItems() {
        Material nonStackableType = Material.matchMaterial(signalStrengthProvider.getConfig().getString("item.non-stackable.type", "MINECART"));
        Component nonStackableName = new MineDown(signalStrengthProvider.getConfig().getString("item.non-stackable.name", "")).toComponent();
        List<String> loreStringListNonStackable = signalStrengthProvider.getConfig().getStringList("item.non-stackable.lore");
        List<Component> loreComponentListNonStackable = new ArrayList<>();

        for (String s : loreStringListNonStackable) {
            loreComponentListNonStackable.add(new MineDown(s).toComponent());
        }

        if (nonStackableType == null) {
            nonStackableType = Material.MINECART;
        } else if (nonStackableType.getMaxStackSize() != 1) {
            nonStackableType = Material.BARREL;
            Bukkit.getLogger().severe("Non-stackable-item type in lang.yml is invalid!");
        }

        ItemStack nonStackableItemStack = new ItemStack(nonStackableType);
        ItemMeta nonStackableMeta = nonStackableItemStack.getItemMeta();

        nonStackableMeta.lore(loreComponentListNonStackable);
        nonStackableMeta.displayName(nonStackableName);
        nonStackableItemStack.setItemMeta(nonStackableMeta);
        this.nonStackable = nonStackableItemStack;

        Material sixteenStackableType = Material.matchMaterial(signalStrengthProvider.getConfig().getString("item.stackable-sixteen.type", "ENDER_PEARL"));
        Component sixteenStackableName = new MineDown(signalStrengthProvider.getConfig().getString("item.stackable-sixteen.name", "")).toComponent();
        List<String> loreStringListSixteenStackable = signalStrengthProvider.getConfig().getStringList("item.stackable-sixteen.lore");
        List<Component> loreComponentListSixteenStackable = new ArrayList<>();

        for (String s : loreStringListSixteenStackable) {
            loreComponentListSixteenStackable.add(new MineDown(s).toComponent());
        }

        if (sixteenStackableType == null) {
            sixteenStackableType = Material.ENDER_PEARL;
        } else if (sixteenStackableType.getMaxStackSize() != 16) {
            sixteenStackableType = Material.ENDER_PEARL;
            Bukkit.getLogger().severe("Sixteen-stackable-item type in lang.yml is invalid!");
        }

        ItemStack sixteenStackableItemStack = new ItemStack(sixteenStackableType);
        ItemMeta sixteenStackableMeta = sixteenStackableItemStack.getItemMeta();
        sixteenStackableMeta.lore(loreComponentListSixteenStackable);
        sixteenStackableMeta.displayName(sixteenStackableName);
        sixteenStackableItemStack.setItemMeta(sixteenStackableMeta);
        this.stackableSixteen = sixteenStackableItemStack;

        Material stackableType = Material.matchMaterial(signalStrengthProvider.getConfig().getString("item.stackable.type", "MINECART"));
        Component stackableName = new MineDown(signalStrengthProvider.getConfig().getString("item.stackable.name", "")).toComponent();
        List<String> loreStringListStackable = signalStrengthProvider.getConfig().getStringList("item.stackable.lore");
        List<Component> loreComponentListStackable = new ArrayList<>();

        for (String s : loreStringListStackable) {
            loreComponentListStackable.add(new MineDown(s).toComponent());
        }

        if (stackableType == null) {
            stackableType = Material.MINECART;
        } else if (stackableType.getMaxStackSize() != 1) {
            stackableType = Material.WHITE_CONCRETE;
            Bukkit.getLogger().severe("Stackable-item type in lang.yml is invalid!");
        }

        ItemStack stackableItemStack = new ItemStack(stackableType);
        ItemMeta stackableMeta = stackableItemStack.getItemMeta();
        stackableMeta.lore(loreComponentListStackable);
        stackableMeta.displayName(stackableName);
        stackableItemStack.setItemMeta(stackableMeta);
        this.stackable = stackableItemStack;
    }
}
