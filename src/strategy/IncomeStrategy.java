package strategy;

import java.math.BigDecimal;

public class IncomeStrategy implements TransactionTypeStrategy {
    @Override
    public String getTransactionType() {
        return "Income";
    }

    @Override
    public BigDecimal updateBalance(BigDecimal walletBalance, BigDecimal transactionAmount) {
        return walletBalance.add(transactionAmount);
    }

    @Override
    public BigDecimal reverseBalance(BigDecimal walletBalance, BigDecimal transactionAmount) {
        return walletBalance.subtract(transactionAmount);
    }
}
