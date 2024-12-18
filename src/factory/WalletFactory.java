package factory;

import java.math.BigDecimal;
import java.util.ArrayList;

import model.Wallet;

public class WalletFactory {
	private static Wallet wallet;
	private static int walletId = 1;
	private static ArrayList<Wallet> walletList = new ArrayList<>();
	
	public static void createWallet(int userId, String userName, String description, BigDecimal balance) {
		wallet = new Wallet(walletId++, userId, userName, description, balance);
		walletList.add(wallet);
	}
	
	public static ArrayList<Wallet> getWalletList() {
		return walletList;
	}
} 
