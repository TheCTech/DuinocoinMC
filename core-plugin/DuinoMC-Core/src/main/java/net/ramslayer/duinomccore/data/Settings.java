package net.ramslayer.duinomccore.data;

import com.tchristofferson.configupdater.ConfigUpdater;
import net.ramslayer.duinomccore.DuinoMCCore;
import net.ramslayer.duinomccore.quests.util.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Settings {

    private final static Settings instance = new Settings();

    private File file;
    private YamlConfiguration config;

    private ArrayList<Quest> quests;

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

        quests = parseQuests();
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

     // ### Quests ###


    public List<Quest> getQuests() {
        return Collections.unmodifiableList(quests);
    }

    private ArrayList<Quest> parseQuests() {
        ArrayList<Map<?, ?>> unparsedQuests = new ArrayList<>();

        var easy = config.getMapList("quests.easy");
        var medium = config.getMapList("quests.medium");
        var hard = config.getMapList("quests.hard");

        unparsedQuests.addAll(easy);
        unparsedQuests.addAll(medium);
        unparsedQuests.addAll(hard);

        ArrayList<Quest> quests = new ArrayList<>();

        for (int i = 0; i < unparsedQuests.size();i++) {
            Map<?, ?> quest = unparsedQuests.get(i);

            int min = (int) quest.get("min");
            int max = (int) quest.get("max");
            int reward = (int) quest.get("reward");
            QuestDifficulty difficulty = i < easy.size() ? QuestDifficulty.EASY :
                            i < easy.size() + medium.size() ? QuestDifficulty.MEDIUM :
                            QuestDifficulty.HARD;


            switch (QuestType.valueOf((String) quest.get("type"))) {
                case KILL_MOB -> quests.add(new KillMobQuest(
                        "d"+i,
                        difficulty,
                        EntityType.valueOf((String) quest.get("entity")),
                        min,
                        max,
                        reward
                ));
                case MINE_BLOCK -> quests.add(new MineBlockQuest(
                        "d"+i,
                        difficulty,
                        Material.valueOf((String) quest.get("material")),
                        min,
                        max,
                        reward
                ));
            }
        }
        return quests;
     }
}
