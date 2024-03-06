package net.fameless.signalstrengthprovider;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SignalStrengthProvider extends JavaPlugin {

    private YamlConfiguration languageFile;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("lang.yml", false);

        File langYML = new File(getDataFolder(), "lang.yml");
        languageFile = YamlConfiguration.loadConfiguration(langYML);
    }

    @Override
    public void onEnable() {
        Message message = new Message(this);
        ItemBuilder itemBuilder = new ItemBuilder(this);
        getCommand("signalstrength").setExecutor(new SignalStrengthCommand(this, message, itemBuilder));
    }

    public YamlConfiguration getLanguageFile() {
        return languageFile;
    }
}
