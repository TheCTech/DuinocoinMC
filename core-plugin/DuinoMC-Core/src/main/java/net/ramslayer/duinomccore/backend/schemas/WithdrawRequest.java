package net.ramslayer.duinomccore.backend.schemas;

public class WithdrawRequest {
    public String recipient_minecraft_username;
    public String recipient_username;
    public double amount;

    public WithdrawRequest(String mcUser, String ducoUser, double amt) {
        this.recipient_minecraft_username = mcUser;
        this.recipient_username = ducoUser;
        this.amount = amt;
    }
}