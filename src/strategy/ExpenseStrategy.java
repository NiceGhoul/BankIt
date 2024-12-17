package strategy;

import java.math.BigDecimal;

public class ExpenseStrategy implements TransactionTypeStrategy {
    @Override
    public String getTransactionType() {
        return "Expense";
    }

    @Override
    public BigDecimal updateBalance(BigDecimal walletBalance, BigDecimal transactionAmount) {
        return walletBalance.subtract(transactionAmount);
    }

    @Override
    public BigDecimal reverseBalance(BigDecimal walletBalance, BigDecimal transactionAmount) {
        return walletBalance.add(transactionAmount);
    }
}
