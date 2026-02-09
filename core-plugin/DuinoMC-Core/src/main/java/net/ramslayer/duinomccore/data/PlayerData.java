package net.ramslayer.duinomccore.data;

import net.ramslayer.duinomccore.DuinoMCCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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

    private PlayerData(UUID uuid) {
        this.uuid = uuid;
        load();
    }

    public static PlayerData get(UUID uuid) {
        return loadedPlayers.computeIfAbsent(uuid, PlayerData::new);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        save();
    }

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
    }

    public void save() {
        File file = new File(playerDataFolder, uuid + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        config.set("balance", balance);

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
}
