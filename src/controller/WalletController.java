package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import main.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Wallet;
import observer.walletObserver;
import model.Expense;
import model.User;

import java.math.BigDecimal;
import java.util.List;
import factory.WalletFactory;

public class WalletController implements walletObserver{
	
	 	@FXML 
	 	private Text balanceText;
	 	@FXML 
	 	private Label walletName;
	 	@FXML 
	 	private Text walletBalance;
	    @FXML 
	    private TableView<Expense> transactionTable;
	    @FXML 
	    private TableColumn<Expense, Integer> idColumn;
	    @FXML 
	    private TableColumn<Expense, String> expenseTypeColumn;
	    @FXML 
	    private TableColumn<Expense, String> categoryColumn;
	    @FXML 
	    private TableColumn<Expense, BigDecimal> amountColumn;
	    @FXML 
	    private TableColumn<Expense, String> descriptionColumn;
	    @FXML 
	    private TableColumn<Expense, String> expenseDateColumn;    
	    private Wallet wallet;
	    
	    @FXML
	    public void initialize() {
	        initializeWallet();
	        intializeTable();
	    }
	    
	    public void initializeWallet() {
	    	User currentUser = UserSession.getInstance().getCurrentUser();
	    	
	    	int userId = currentUser.getUserId();
	    	String userName = currentUser.getUsername();
	 
	    	wallet = WalletFactory.createWallet(userId, userName, new BigDecimal(0) );
	    	
	    	walletName.setText(wallet.getWalletName());
	    	walletBalance.setText(wallet.getBalance().toPlainString());
	    	
	    	update();
	    }
	    
	    private void intializeTable() {
	    	
	    }
	    
	    
	    @Override
	    public void update() {
	        walletBalance.setText("Rp. " + wallet.getBalance().toPlainString() + ",-");
	    }
}
