package net.ramslayer.duinomccore.commands;

import net.ramslayer.duinomccore.DuinoMCCore;
import net.ramslayer.duinomccore.backend.BackendClient;
import net.ramslayer.duinomccore.backend.schemas.DepositApproval;
import net.ramslayer.duinomccore.backend.schemas.DepositRequest;
import net.ramslayer.duinomccore.backend.schemas.WithdrawRequest;
import net.ramslayer.duinomccore.backend.schemas.WithdrawalConfirmation;
import net.ramslayer.duinomccore.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Only players can run this command!");
            return true;
        }

        if (args.length != 2) {
            return false;
        }

        String ducoUsername = args[0];

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "The amount must be a valid decimal number.");
            return true;
        }


        BackendClient backend = DuinoMCCore.getInstance().getBackendClient();
        WithdrawRequest withdrawRequest = new WithdrawRequest(sender.getName(), ducoUsername, amount);

        backend.sendWithdrawal(withdrawRequest)
                .thenAccept(withdrawalConfirmation -> {
                    Bukkit.getScheduler().runTask(DuinoMCCore.getInstance(), () -> {
                        handleWithdrawal((Player) sender, amount, withdrawalConfirmation);
                    });
                })
                .exceptionally(ex -> {
                    Bukkit.getScheduler().runTask(DuinoMCCore.getInstance(), () -> {
                        sender.sendMessage(ChatColor.RED + "Backend error: " + ex.getMessage());
                    });
                    ex.printStackTrace();
                    return null;
                });

        return true;
    }

    private void handleWithdrawal(Player player, double amount, WithdrawalConfirmation withdrawalConfirmation) {
        if (withdrawalConfirmation.success) {
            VaultHook.withdraw(player, amount);
        }

        player.sendMessage(
                (withdrawalConfirmation.success ? ChatColor.GOLD : ChatColor.RED)
                        + withdrawalConfirmation.message
        );
    }
}
