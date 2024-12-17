package factory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Transaction;
import strategy.IncomeStrategy;
import strategy.ExpenseStrategy;
import strategy.TransactionTypeStrategy;

public class TransactionFactory {
    private static int transactionIdCounter = 1;
    private static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void createTransaction(String type, int userId, int walletId, int categoryId, BigDecimal amount, String description, LocalDate date) {
        TransactionTypeStrategy strategy;

        if ("Income".equalsIgnoreCase(type)) {
            strategy = new IncomeStrategy();
        } else if ("Expense".equalsIgnoreCase(type)) {
            strategy = new ExpenseStrategy();
        } else {
            throw new IllegalArgumentException("Unknown transaction type: " + type);
        }

        Transaction transaction = new Transaction(transactionIdCounter++, userId, walletId, categoryId, amount, description, date, strategy);

        transactions.add(transaction);
    }

    public static ArrayList<Transaction> getTransactionList() {
        return transactions;
    }
}
