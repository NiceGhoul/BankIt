package model;

import java.math.BigDecimal;

public class Wallet {
    private int walletId;
    private int userId;
    private String walletName;
    private String description;
    private BigDecimal balance;

    public Wallet(int walletId, int userId, String walletName, String description, BigDecimal balance) {
        this.walletId = walletId;
        this.userId = userId;
        this.walletName = walletName;
        this.description = description;
        this.balance = balance;
    }

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
