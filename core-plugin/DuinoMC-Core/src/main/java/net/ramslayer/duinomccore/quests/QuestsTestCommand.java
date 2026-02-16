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

        Integer progress = PlayerData.get(uuid).getQuestProgress("d1");

        if (args.length > 0) {
            PlayerData.get(uuid).setQuestProgress("d1", progress + 1);
        }

        sender.sendMessage(String.valueOf(PlayerData.get(uuid).getQuestProgress("d1")));
        return true;
    }
}