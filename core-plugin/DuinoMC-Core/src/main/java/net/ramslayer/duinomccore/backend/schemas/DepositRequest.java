package net.ramslayer.duinomccore.backend.schemas;

public class DepositRequest {
    public String sender_minecraft_username;
    public String transaction_hash;

    public DepositRequest(String sender, String hash) {
        this.sender_minecraft_username = sender;
        this.transaction_hash = hash;
    }
}
