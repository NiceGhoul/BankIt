package adapter;

import model.Expense;
import model.Income;
import model.Transaction;

public class TransactionAdapter {

    public static Income convertExpenseToIncome(Transaction transaction) {
        if (transaction instanceof Expense) {
            return new Income(
                transaction.getId(),
                transaction.getUserId(),
                transaction.getWalletId(),
                transaction.getCategoryId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate()
            );
        } else {
            throw new IllegalArgumentException("Transaction is not an Expense");
        }
    }

    public static Expense convertIncomeToExpense(Transaction transaction) {
        if (transaction instanceof Income) {
            return new Expense(
                transaction.getId(),
                transaction.getUserId(),
                transaction.getWalletId(),
                transaction.getCategoryId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate()
            );
        } else {
            throw new IllegalArgumentException("Transaction is not an Income");
        }
    }
}
