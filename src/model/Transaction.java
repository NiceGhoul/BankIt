package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaction {
    private int id;
    private int userId;
    private int walletId;
    private int categoryId;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    // Constructor
    public Transaction(int id, int userId, int walletId, int categoryId, BigDecimal amount, String description, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.walletId = walletId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // Getters and Setters
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

    // Abstract method to be implemented by subclasses
    public abstract String getTransactionType();
}
