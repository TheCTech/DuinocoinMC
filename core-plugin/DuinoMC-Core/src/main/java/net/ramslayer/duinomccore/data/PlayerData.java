package net.ramslayer.duinomccore.data;

import net.ramslayer.duinomccore.DuinoMCCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private static final Map<UUID, PlayerData> loadedPlayers = new HashMap<>();
    private static final File playerDataFolder = new File(DuinoMCCore.getInstance().getDataFolder(), "playerdata");

    static {
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }

    private final UUID uuid;
    private double balance;
    private Map<String, Integer> questsProgress = new HashMap<>();
    private Map<String, Boolean> questsClaimed = new HashMap<>();
    private String lastQuestsUpdate;

    private PlayerData(UUID uuid) {
        this.uuid = uuid;
        load();
    }

    public static PlayerData get(UUID uuid) {
        return loadedPlayers.computeIfAbsent(uuid, PlayerData::new);
    }

    // ### GET & SET ###

    // Balance

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        save();
    }

    // Quests

    public Integer getQuestProgress(String id) {
        return questsProgress.getOrDefault(id, 0);
    }

    public void setQuestProgress(String id, Integer value) {
        questsProgress.put(id, value);
    }

    public void resetQuestsProgress() {
        questsProgress = new HashMap<>();
        questsClaimed = new HashMap<>();
        save();
    }

    public Boolean isQuestClaimed(String id) {
        return questsClaimed.getOrDefault(id, false);
    }

    public void claimQuest(String id) {
        questsClaimed.put(id, true);
        save();
    }

    // ### Main logic ###

    private void load() {
        File file = new File(playerDataFolder, uuid + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.balance = config.getInt("balance", 0);

        loadQuests(config);
    }

    public void save() {
        File file = new File(playerDataFolder, uuid + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        config.set("balance", balance);

        saveQuests(config);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unload(UUID uuid) {
        PlayerData data = loadedPlayers.remove(uuid);
        if (data != null) {
            data.save();
        }
    }

    public static void saveAll() {
        for (PlayerData data : loadedPlayers.values()) {
            data.save();
        }
    }

    // ### Helpers ###
    private void loadQuests(YamlConfiguration config) {
        if (config.isConfigurationSection("quests.progress")) {
            for (String key : config.getConfigurationSection("quests.progress").getKeys(false)) {
                questsProgress.put(key, config.getInt("quests.progress." + key));
            }
        }

        if (config.isConfigurationSection("quests.claimed")) {
            for (String key : config.getConfigurationSection("quests.claimed").getKeys(false)) {
                questsClaimed.put(key, config.getBoolean("quests.claimed." + key));
            }
        }

        lastQuestsUpdate = config.getString("quests.last_update", "1970-01-01");

        // If quests progress is from another day
        if  (!LocalDate.parse(lastQuestsUpdate).equals(LocalDate.now())) {
            resetQuestsProgress();
            lastQuestsUpdate = LocalDate.now().toString();
        }
    }

    private void saveQuests(YamlConfiguration config) {
        for (Map.Entry<String, Integer> entry : questsProgress.entrySet()) {
            config.set("quests.progress." + entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Boolean> entry : questsClaimed.entrySet()) {
            config.set("quests.claimed." + entry.getKey(), entry.getValue());
        }

        config.set("quests.last_update", lastQuestsUpdate);
    }
}
