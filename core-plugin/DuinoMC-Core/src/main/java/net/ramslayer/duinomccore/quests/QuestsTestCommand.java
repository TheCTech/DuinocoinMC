package net.ramslayer.duinomccore.quests;

import net.ramslayer.duinomccore.data.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class QuestsTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID uuid = ((Player)sender).getUniqueId();

        if (args.length > 0) {
            QuestsManager.getInstance().generateQuests();
        }

        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(0).getQuest().toString());
        sender.sendMessage(QuestsManager.getInstance().getTodayQuests().get(0).getDate().toString());
        return true;
    }
}