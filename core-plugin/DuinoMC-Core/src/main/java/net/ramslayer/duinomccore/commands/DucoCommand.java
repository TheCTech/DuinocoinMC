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

public class DucoCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2){
            return false;
        }

        String subcommandName = args[0];
        String playerName = args[1];

        if (!(subcommandName.equals("check") || subcommandName.equals("give") || subcommandName.equals("take"))) {
            return false;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED + "Player " + playerName + " has never played on the server.");
                    return;
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!VaultHook.hasEconomy()) {
                            sender.sendMessage(ChatColor.RED + "Vault missing or it didn't connect to economy.");
                            return;
                        }

                        if (subcommandName.equals("check")) {
                            double balance = VaultHook.getBalance(target);

                            sender.sendMessage(ChatColor.GOLD + target.getName() + "'s balance: " + VaultHook.formatCurrencySymbol(balance));
                        } else {
                            double amount;
                            try {
                                amount = Double.parseDouble(args.length >= 3 ? args[2] : "");
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.RED + "The amount must be a valid decimal number.");
                                return;
                            }

                            if (subcommandName.equals("give")) {
                                String error = VaultHook.deposit(target, amount);

                                if (error != null && !error.isEmpty()) {
                                    sender.sendMessage(ChatColor.RED + "Error: " + error);
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + "Gave " + VaultHook.formatCurrencySymbol(amount) + " to " + target.getName());
                                }
                            } else {
                                String error = VaultHook.withdraw(target, amount);

                                if (error != null && !error.isEmpty()) {
                                    sender.sendMessage(ChatColor.RED + "Error: " + error);
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + "Took " + VaultHook.formatCurrencySymbol(amount) + " from " + target.getName());
                                }
                            }
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
            return Arrays.asList("check", "give", "take").stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        } else if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        } else if (args.length == 3 && ("give".equals(args[0]) || "take".equals(args[0]))) {
            return Arrays.asList("1", "100", "1000").stream().filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }
}
