package factory;

import java.math.BigDecimal;
import model.Wallet;

public class WalletFactory {
	
//	private static int walletIdCounter = 1;
	public static Wallet createWallet(int userId, String userName, BigDecimal balance) {
		String walletName = userName + "'s Wallet";
        int walletId = userId + 10;
		 return new Wallet(walletId, userId, walletName, balance);
	}
	
}
