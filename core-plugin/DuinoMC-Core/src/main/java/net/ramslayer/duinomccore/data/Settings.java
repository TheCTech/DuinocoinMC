package net.ramslayer.duinomccore.data;

import com.tchristofferson.configupdater.ConfigUpdater;
import net.ramslayer.duinomccore.DuinoMCCore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Settings {

    private final static Settings instance = new Settings();

    private File file;
    private YamlConfiguration config;

    private String placeholder;

    private Settings() {}

    public static Settings getInstance() {
        return instance;
    }

    public void load() {
        file = new File(DuinoMCCore.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            DuinoMCCore.getInstance().saveResource("config.yml", false);
        }

        try {
            ConfigUpdater.update(DuinoMCCore.getInstance(), "config.yml", file, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        placeholder = config.getString("placeholder");
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public void set(String path, Object value) {
        config.set(path, value);
        save();
     }
}
