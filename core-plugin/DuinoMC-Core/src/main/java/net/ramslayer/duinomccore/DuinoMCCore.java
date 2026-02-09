package net.ramslayer.duinomccore;

import net.ramslayer.duinomccore.backend.BackendClient;
import net.ramslayer.duinomccore.commands.*;
import net.ramslayer.duinomccore.hooks.DucoEconomy;
import org.bukkit.plugin.java.JavaPlugin;

public final class DuinoMCCore extends JavaPlugin {

    private BackendClient backendClient;

    public static DuinoMCCore getInstance() {
        return getPlugin(DuinoMCCore.class);
    }

    public BackendClient getBackendClient() {
        return backendClient;
    }


    @Override
    public void onEnable() {

        backendClient = new BackendClient("http://127.0.0.1:8000");

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            DucoEconomy.register();
        } else {
            System.out.println("Vault not found!");
        }

        getCommand("duco").setExecutor(new DucoCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("deposit").setExecutor(new DepositCommand());
        getCommand("withdraw").setExecutor(new WithdrawCommand());

        System.out.println("DuinoMC-Core has started");
    }

    @Override
    public void onDisable() {
        System.out.println("DuinoMC-Core has stopped");
    }
}
