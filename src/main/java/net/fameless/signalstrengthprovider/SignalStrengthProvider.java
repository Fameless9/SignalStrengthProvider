package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SignalStrengthProvider extends JavaPlugin {

    private YamlConfiguration languageFile;
    private Component prefix;

    @Override
    public void onLoad() {
        saveResource("lang.yml", false);

        File langYML = new File(getDataFolder(), "lang.yml");
        languageFile = YamlConfiguration.loadConfiguration(langYML);
        prefix = new MineDown(languageFile.getString("message.prefix")).toComponent();
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
