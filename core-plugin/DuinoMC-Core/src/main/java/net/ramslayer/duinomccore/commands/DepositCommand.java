package net.ramslayer.duinomccore.commands;

import net.ramslayer.duinomccore.DuinoMCCore;
import net.ramslayer.duinomccore.backend.BackendClient;
import net.ramslayer.duinomccore.backend.schemas.DepositApproval;
import net.ramslayer.duinomccore.backend.schemas.DepositRequest;
import net.ramslayer.duinomccore.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Only players can run this command!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.GOLD + "Send DUCO you want to deposit to \"CTech\", remember to put your minecraft username inside the memo.\nThan use /deposit <transaction_hash>");
            return true;
        }

        String hash = args[0];

        BackendClient backend = DuinoMCCore.getInstance().getBackendClient();
        DepositRequest deposit = new DepositRequest(sender.getName(), hash);

        backend.sendDeposit(deposit)
                .thenAccept(approval -> {
                    Bukkit.getScheduler().runTask(DuinoMCCore.getInstance(), () -> {
                        creditDeposit((Player) sender, approval);
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

    private void creditDeposit(Player player, DepositApproval approval) {
        if (approval.success) {
            VaultHook.deposit(player, approval.amount);
        }

        player.sendMessage(
                (approval.success ? ChatColor.GOLD : ChatColor.RED)
                + approval.message
        );
    }
}
