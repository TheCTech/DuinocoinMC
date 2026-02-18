package net.ramslayer.duinomccore.inventories;

import net.ramslayer.duinomccore.quests.QuestsManager;
import net.ramslayer.duinomccore.quests.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class QuestMenu implements InventoryHolder {

    private final Inventory inventory;

    public QuestMenu() {
        inventory = Bukkit.createInventory(this, 45, "Quest menu");

        ItemStack item;

        ItemStack glassPane = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", new ArrayList<>());

        // Upper row
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, glassPane);
        }

        // Bottom row
        for (int i = 36; i < 45; i++) {
            inventory.setItem(i, glassPane);
        }

        // Right and left panels
        for (int row = 1; row < 4; row++){
            for (int i = 0; i < 3; i++) {
                inventory.setItem(row*9+i, glassPane);
                inventory.setItem(row*9+i+6, glassPane);
            }
        }

        for (DailyQuest dQuest : QuestsManager.getInstance().getTodayQuests()) {
            Quest quest = dQuest.getQuest();
            item = createItem(Material.PAPER,
                    quest.getType().toString(),
                    List.of(String.valueOf(quest.getId()),
                            String.valueOf(quest.getDifficulty()),
                            String.valueOf(quest.getReward()),
                            String.valueOf(quest.getType().equals(QuestType.MINE_BLOCK) ? ((MineBlockQuest) quest).getMaterial() : ((KillMobQuest) quest).getEntity()),
                            String.valueOf(quest.getRequiredAmount()
                            )));

            inventory.setItem(inventory.firstEmpty(), item);
        }
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
