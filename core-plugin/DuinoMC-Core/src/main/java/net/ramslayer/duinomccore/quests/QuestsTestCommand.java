package net.ramslayer.duinomccore.quests;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class QuestsTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            QuestsManager.getInstance().generateQuests();
        }

        if (QuestsManager.getInstance().getTodayQuests().isEmpty()) {
            sender.sendMessage("Quests not created yet");
            return true;
        }

        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(0).getQuest().getDifficulty().toString());
        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(1).getQuest().getDifficulty().toString());
        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(2).getQuest().getDifficulty().toString());
        return true;
    }
}