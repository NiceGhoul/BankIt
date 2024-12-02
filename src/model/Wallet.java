package model;

import java.math.BigDecimal;

public class Wallet {
    private int walletId;
    private int userId;
    private String walletName;
    private BigDecimal balance;

    public Wallet(int walletId, int userId, String walletName, BigDecimal balance) {
        this.walletId = walletId;
        this.userId = userId;
        this.walletName = walletName;
        this.balance = balance;
    }

    // Getters and Setters
    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
