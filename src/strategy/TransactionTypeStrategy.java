package strategy;

import java.math.BigDecimal;

public interface TransactionTypeStrategy {
    String getTransactionType();
    BigDecimal updateBalance(BigDecimal walletBalance, BigDecimal transactionAmount);
    BigDecimal reverseBalance(BigDecimal walletBalance, BigDecimal transactionAmount);
}
