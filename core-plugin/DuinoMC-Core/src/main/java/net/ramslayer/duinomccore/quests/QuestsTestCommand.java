package net.ramslayer.duinomccore.quests;

import net.ramslayer.duinomccore.data.PlayerData;
import net.ramslayer.duinomccore.inventories.QuestMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestsTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            QuestsManager.getInstance().generateQuests();
        } else if (args.length == 2) {
            if (sender instanceof Player player) {
                PlayerData.get(player.getUniqueId()).setQuestProgress(args[0], Integer.valueOf(args[1]));
            }
        }

        if (QuestsManager.getInstance().getTodayQuests().isEmpty()) {
            sender.sendMessage("Quests not created yet");
            return true;
        }

        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(0).getQuest().getDifficulty().toString());
        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(1).getQuest().getDifficulty().toString());
        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(2).getQuest().getDifficulty().toString());

        if (sender instanceof Player player) {
            player.openInventory(new QuestMenu().getInventory());
        }

        return true;
    }
}