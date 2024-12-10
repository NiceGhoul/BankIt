package factory;

import java.math.BigDecimal;
import java.util.ArrayList;

import model.User;
import model.Wallet;

public class WalletFactory {
	private static Wallet wallet;
	private static int walletId = 10;
	private static ArrayList<Wallet> walletList = new ArrayList<>();
//	private static int walletIdCounter = 1;
	
	public static void createWallet(int userId, String userName, String description, BigDecimal balance) {
		String walletName;
		wallet = new Wallet(walletId++, userId, userName, description, balance);
		walletList.add(wallet);
	}
	
	public static ArrayList<Wallet> getWalletList() {
		return walletList;
	}
}
