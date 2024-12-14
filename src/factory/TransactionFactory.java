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
    public static ArrayList<Transaction> test(){
        transactions.add(new Income(1, 1, 201, 301, new BigDecimal("500.00"), "Salary Payment", LocalDate.now()));
        transactions.add(new Expense(2, 1, 202, 302, new BigDecimal("150.00"), "Grocery Shopping", LocalDate.now()));
        transactions.add(new Income(3, 1, 203, 303, new BigDecimal("300.00"), "Freelance Work", LocalDate.now().minusDays(1)));
        // dummy wallet data's
//        transactions.add(new Expense(4, 2, 204, 304, new BigDecimal("100.00"), "Investment Funds 1", LocalDate.now().minusDays(5)));
//        transactions.add(new Expense(5, 2, 202, 302, new BigDecimal("400.00"), "Investment Funds 2", LocalDate.now().minusDays(3)));
//        transactions.add(new Income(6, 2, 204, 301, new BigDecimal("750.00"), "Investment Funds 3", LocalDate.now()));
        return transactions;
    }

}
