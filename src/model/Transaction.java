package model;

import java.math.BigDecimal;
import java.time.LocalDate;

import factory.CategoryFactory;
import factory.WalletFactory;

public abstract class Transaction {
    private int id;
    private int userId;
    private int walletId;
    private int categoryId;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    public Transaction(int id, int userId, int walletId, int categoryId, BigDecimal amount, String description, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.walletId = walletId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getCategoryName() {
        return CategoryFactory.getCategoryList().stream()
                .filter(category -> category.getCategoryId() == this.categoryId)
                .map(Category::getCategoryName)
                .findFirst()
                .orElse("Unknown Category");
    }

    public String getWalletName() {
        return WalletFactory.getWalletList().stream()
                .filter(wallet -> wallet.getWalletId() == this.walletId)
                .map(Wallet::getWalletName)
                .findFirst()
                .orElse("Unknown Wallet");
    }

    public abstract String getTransactionType();
}
