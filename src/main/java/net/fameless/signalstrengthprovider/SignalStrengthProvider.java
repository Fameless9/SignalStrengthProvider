package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class SignalStrengthProvider extends JavaPlugin {

    private Component prefix = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String prefixString = getConfig().getString("message.prefix") != null ? getConfig().getString("message.prefix") :
                "";

        if (prefixString == null || prefixString.isEmpty()) {
            prefix = Component.text("");
        } else {
            prefix = new MineDown(prefixString).toComponent();
        }

        Message message = new Message(this);
        ItemBuilder itemBuilder = new ItemBuilder(this);
        NotAvailableGUI notAvailableGUI = new NotAvailableGUI(this, itemBuilder);

        getCommand("signalstrength").setExecutor(new SignalStrengthCommand(this, message, itemBuilder, notAvailableGUI));
        getCommand("signalstrength").setTabCompleter(new SignalStrengthCommandTabCompleter());

        getServer().getPluginManager().registerEvents(notAvailableGUI, this);

        new Metrics(this, 21285);
    }

    public Component getPrefix() { return prefix; }
}
