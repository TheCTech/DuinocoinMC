package net.ramslayer.duinomccore.listeners;

import net.ramslayer.duinomccore.data.PlayerData;
import net.ramslayer.duinomccore.hooks.VaultHook;
import net.ramslayer.duinomccore.quests.QuestsManager;
import net.ramslayer.duinomccore.quests.util.Quest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class QuestMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("Quest menu") && e.getView().getTopInventory().equals(e.getClickedInventory())) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();
            UUID playerUUID = player.getUniqueId();

            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                return;
            }

            int questIndex = e.getSlot() - 12;

            Quest quest = QuestsManager.getInstance().getTodayQuests().get(questIndex).getQuest();

            if (!quest.isReadyToClaim(playerUUID)) {
                player.sendMessage(ChatColor.RED + "Quest requirements not met!");
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
            } else if (PlayerData.get(playerUUID).isQuestClaimed(quest.getId())) {
                player.sendMessage(ChatColor.RED + "Quest already claimed!");
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
            } else {
                quest.claimQuest(player);

                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                player.sendMessage(ChatColor.GREEN + "Quest claimed, " + VaultHook.formatCurrencySymbol(quest.getReward()) + " received!");
            }
        }
    }
}
