package factory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Expense;
import model.Income;
import model.Transaction;

public class TransactionFactory {
    private static int transactionIdCounter = 1;
    private static ArrayList<Transaction> transactions = new ArrayList<>();
    public static void createTransaction(String type, int userId, int walletId, int categoryId, BigDecimal amount, String description, LocalDate date) {
        if ("Income".equalsIgnoreCase(type)) {
            Income income =  new Income(transactionIdCounter++, userId, walletId, categoryId, amount, description, date);
            transactions.add(income);
        } else if ("Expense".equalsIgnoreCase(type)) {
            Expense expense =  new Expense(transactionIdCounter++, userId, walletId, categoryId, amount, description, date);
            transactions.add(expense);
        } else {
            throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
    }
    public static ArrayList<Transaction> getTransactionList() {
        return transactions;
    }
}
