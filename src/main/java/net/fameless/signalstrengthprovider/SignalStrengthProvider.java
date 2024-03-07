package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SignalStrengthProvider extends JavaPlugin {

    private YamlConfiguration languageFile;
    private Component prefix = null;

    @Override
    public void onLoad() {
        saveResource("lang.yml", false);

        File langYML = new File(getDataFolder(), "lang.yml");
        languageFile = YamlConfiguration.loadConfiguration(langYML);
        String prefixString = languageFile.getString("message.prefix") != null ? languageFile.getString("message.prefix") :
                "";

        if (prefixString.isEmpty()) {
            prefix = Component.text("");
        } else {
            prefix = new MineDown(prefixString).toComponent();
        }
    }

    @Override
    public void onEnable() {
        Message message = new Message(this);
        ItemBuilder itemBuilder = new ItemBuilder(this);
        getCommand("signalstrength").setExecutor(new SignalStrengthCommand(this, message, itemBuilder));
        getCommand("signalstrength").setTabCompleter(new SignalStrengthCommandTabCompleter());
    }

    public YamlConfiguration getLanguageFile() { return languageFile; }
    public Component getPrefix() { return prefix; }
}
