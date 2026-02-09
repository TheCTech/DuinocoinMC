package net.ramslayer.duinomccore.commands;

import net.ramslayer.duinomccore.DuinoMCCore;
import net.ramslayer.duinomccore.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PayCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Only players can run this command!");
            return true;
        }

        if (args.length != 2) {
            return false;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "The amount must be a valid decimal number.");
            return true;
        }

        double balance = VaultHook.getBalance((Player) sender);
        if (balance < amount) {
            sender.sendMessage(ChatColor.RED + "The amount must not be greater than your balance.");
            return true;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED + "Player " + args[0] + " has never played on the server.");
                    return;
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        VaultHook.withdraw((Player) sender, amount);
                        VaultHook.deposit(target, amount);

                        sender.sendMessage(ChatColor.GOLD + "Sent " + VaultHook.formatCurrencySymbol(amount) + " to " + target.getName());

                        if (target.isOnline()) {
                            target.getPlayer().sendMessage(ChatColor.GOLD + "Received " + VaultHook.formatCurrencySymbol(amount) + " from " + sender.getName());
                        }
                    }
                }.runTask(DuinoMCCore.getInstance());
            }
        }.runTaskAsynchronously(DuinoMCCore.getInstance());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.asList("1", "100", "1000").stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }
}