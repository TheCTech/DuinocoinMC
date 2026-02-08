package net.ramslayer.duinomccore.hooks;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.ramslayer.duinomccore.DuinoMCCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DucoEconomy extends AbstractEconomy {

    private Map<String, Integer> balances = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "DuinoMCCore";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return String.valueOf((int) amount) + " " + this.currencyNameSingular();
    }

    @Override
    public String currencyNamePlural() {
        return "DUCO";
    }

    @Override
    public String currencyNameSingular() {
        return "DUCO";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return this.hasAccountByName(playerName);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return this.hasAccountByName(playerName);
    }

    @Override
    public double getBalance(String playerName) {
        return this.getBalanceByName(playerName);
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return getBalanceByName(playerName);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return this.hasBalanceByName(playerName, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return this.hasBalanceByName(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return this.withdrawPlayerByName(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return this.withdrawPlayerByName(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return this.depositPlayerByName(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return this.depositPlayerByName(playerName, amount);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented!");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    public static void register() {
        Bukkit.getServicesManager().register(Economy.class, new DucoEconomy(), DuinoMCCore.getInstance(), ServicePriority.High);
    }

    private boolean hasAccountByName(String playerName) {
        return this.balances.containsKey(playerName);
    }

    private double getBalanceByName(String playerName) {
        return this.balances.getOrDefault(playerName, 0);
    }

    private boolean hasBalanceByName(String playerName, double amount) {
        return this.getBalanceByName(playerName) >= amount;
    }

    private EconomyResponse withdrawPlayerByName(String playerName, double amount) {
        if (amount < 0)
            return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");

        if (!has(playerName, amount)) {
            return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }

        this.balances.put(playerName, (int) (this.getBalanceByName(playerName) - amount));

        return new EconomyResponse(amount, this.getBalanceByName(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    private EconomyResponse depositPlayerByName(String playerName, double amount) {
        if (amount < 0)
            return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");

        this.balances.put(playerName, (int) (this.getBalanceByName(playerName) + amount));

        return new EconomyResponse(amount, this.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }
}
