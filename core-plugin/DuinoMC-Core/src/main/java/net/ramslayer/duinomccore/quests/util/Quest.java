package net.ramslayer.duinomccore.quests.util;


import net.milkbowl.vault.economy.Economy;
import net.ramslayer.duinomccore.data.PlayerData;
import net.ramslayer.duinomccore.hooks.VaultHook;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.UUID;

public abstract class Quest {

    protected final String id;
    protected final QuestDifficulty difficulty;
    protected final int amount_required;
    protected final int reward;

    public Quest(String id, QuestDifficulty difficulty, int min_amount, int max_amount, int reward) {
        this.id = id;
        this.difficulty = difficulty;
        this.amount_required = (int)(Math.random() * (max_amount-min_amount) + min_amount);
        this.reward = reward;
    }

    public abstract QuestType getType();

    public abstract boolean matches(Event event);

    public String getId() {
        return id;
    }

    public QuestDifficulty getDifficulty() {
        return difficulty;
    }

    public int getRequiredAmount() {
        return amount_required;
    }

    public int getReward() {
        return reward;
    }

    public boolean isReadyToClaim(UUID playerUUID) {return PlayerData.get(playerUUID).getQuestProgress(id) >= amount_required;}

    public void claimQuest(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (!isReadyToClaim(playerUUID)) return; // Additional failsafe

        PlayerData.get(playerUUID).claimQuest(id);

        VaultHook.deposit(player, reward);
    }
}
