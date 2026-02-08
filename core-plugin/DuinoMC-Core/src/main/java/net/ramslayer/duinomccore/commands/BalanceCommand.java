package net.ramslayer.duinomccore.commands;

import net.ramslayer.duinomccore.hooks.VaultHook;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            double balance = VaultHook.getBalance(player);

            player.sendMessage(ChatColor.GOLD + "Your balance: " + VaultHook.formatCurrencySymbol(balance));
        } else {
            System.out.println("Only players can run this command!");
        }


        return true;
    }
}