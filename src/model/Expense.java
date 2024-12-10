package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Expense extends Transaction {

    public Expense(int id, int userId, int walletId, int categoryId, BigDecimal amount, String description, LocalDate date) {
        super(id, userId, walletId, categoryId, amount, description, date);
    }

    @Override
    public String getTransactionType() {
        return "Expense";
    }
}
