package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotAvailableGUI implements InventoryHolder, Listener {

    private final SignalStrengthProvider signalStrengthProvider;
    private final ItemBuilder builder;

    public NotAvailableGUI(SignalStrengthProvider signalStrengthProvider, ItemBuilder itemBuilder) {
        this.signalStrengthProvider = signalStrengthProvider;
        this.builder = itemBuilder;
    }

    public Inventory getNotAvailableInventory(int originSignalStrength, ItemStack containerStack) {

        String typeLeftString = signalStrengthProvider.getConfig().getString("inventory.items.not-available.left.type", "RED_STAINED_GLASS_PANE");
        Material typeLeft = Material.matchMaterial(typeLeftString);

        String nameLeft = signalStrengthProvider.getConfig().getString("inventory.items.not-available.left.name",
                "&red&Signal strength less than than ${origin} is not available.");
        String finalNameLeft = nameLeft.replace("${origin}", String.valueOf(originSignalStrength));

        List<String> loreListLeft = signalStrengthProvider.getConfig().getStringList("inventory.items.not-available.left.lore");
        List<Component> loreComponentsLeft = new ArrayList<>();

        for (String s : loreListLeft) {
            loreComponentsLeft.add(new MineDown(s).toComponent());
        }

        Container container = (Container) ((BlockStateMeta) containerStack.getItemMeta()).getBlockState();
        int max = 1 + (container.getInventory().getSize() * 127 / container.getInventory().getSize()) * 14;

        int indexLeft = originSignalStrength;
        int indexRight = originSignalStrength;

        ItemStack stackLeft = null;
        ItemStack stackRight = null;

        while (stackLeft == null || stackRight == null) {
            if (stackLeft == null && indexLeft > 0) {
                try {
                    stackLeft = builder.buildSignalStrength(indexLeft, containerStack);
                } catch (Exception e) {
                    indexLeft--;
                }
            }

            if (stackRight == null && indexRight <= max) {
                try {
                    stackRight = builder.buildSignalStrength(indexRight, containerStack);
                } catch (Exception e) {
                    indexRight++;
                }
            }

            if (stackLeft != null && stackRight != null) {
                break;
            }

            if (indexLeft <= 0 && indexRight > max) {
                break;
            }
        }

        if (stackLeft == null) {
            stackLeft = builder.buildItem(new ItemStack(typeLeft != null ? typeLeft : Material.RED_STAINED_GLASS_PANE), true,
                    new MineDown(finalNameLeft).toComponent(), loreComponentsLeft);
        }

        String typeRightString = signalStrengthProvider.getConfig().getString("inventory.items.not-available.right.type", "RED_STAINED_GLASS_PANE");
        Material typeRight = Material.matchMaterial(typeRightString);

        String nameRight = signalStrengthProvider.getConfig().getString("inventory.items.not-available.right.name",
                "&red&Signal strength higher than ${origin} is not available.");
        String finalNameRight = nameRight.replace("${origin}", String.valueOf(originSignalStrength));

        List<String> loreListRight = signalStrengthProvider.getConfig().getStringList("inventory.items.not-available.right.lore");
        List<Component> loreComponentsRight = new ArrayList<>();

        for (String s : loreListRight) {
            loreComponentsRight.add(new MineDown(s).toComponent());
        }

        if (stackRight == null) {
            stackRight = builder.buildItem(new ItemStack(typeRight != null ? typeRight : Material.RED_STAINED_GLASS_PANE), true,
                    new MineDown(finalNameRight).toComponent(), loreComponentsRight);
        }

        Inventory inventory = Bukkit.createInventory(this, 9, new MineDown(
                signalStrengthProvider.getConfig().getString("inventory.title", "&red&Select a different signal strength.")).toComponent());

        inventory.setItem(3, stackLeft);
        inventory.setItem(5, stackRight);

        return inventory;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof NotAvailableGUI)) return;
        event.setCancelled(true);
        switch (event.getSlot()) {
            case 3: {
                ItemStack stack = event.getInventory().getItem(3);
                if (stack == null || stack.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                    event.getWhoClicked().closeInventory();
                    return;
                }
                event.getWhoClicked().getInventory().addItem(stack);
                event.getWhoClicked().closeInventory();
                break;
            }
            case 5: {
                ItemStack stack = event.getInventory().getItem(5);
                if (stack == null || stack.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                    event.getWhoClicked().closeInventory();
                    return;
                }
                event.getWhoClicked().getInventory().addItem(stack);
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return getNotAvailableInventory(0, new ItemStack(Material.BARREL));
    }
}
