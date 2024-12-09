package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Wallet;
import observer.walletObserver;
import model.Expense;
import model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import factory.WalletFactory;

public class WalletController implements walletObserver{
	
	 	@FXML 
	 	private Text balanceText, walletBalance;
	 	@FXML 
	 	private Button addWalletButton, backButton;
	 	@FXML 
	 	private Label walletName, AddLabel;
	    @FXML 
	    private TableView<Expense> transactionTable;
	    @FXML
	    private ComboBox<String> walletDropdown;
	    @FXML 
	    private TableColumn<Expense, Integer> idColumn;
	    @FXML 
	    private TableColumn<Expense, String> expenseTypeColumn, categoryColumn, descriptionColumn, expenseDateColumn;
	    @FXML 
	    private TableColumn<Expense, BigDecimal> amountColumn;
	    @FXML
	    private TextField walletNameField;
	    
	    private Wallet wallet;
	    
	    @FXML
	    public void initialize() {
	    	
	    	System.out.println("walletDropdown: " + walletDropdown);
	        initializeWallet();
	    }
	    
	    public void initializeWallet() {
	    	User currentUser = UserSession.getInstance().getCurrentUser();
	    	
	    	if (WalletFactory.getWalletList().isEmpty()) {
	            WalletFactory.createWallet(currentUser.getUserId(), currentUser.getUsername(), "Initial Wallet", new BigDecimal("0"));
	        }
	    	
	    	ObservableList<Wallet> wallets = FXCollections.observableArrayList(WalletFactory.getWalletList());
	    	
	    	for (Wallet w : wallets) {
	            walletDropdown.getItems().add(w.getWalletName());
	        }
	    	
	    	if (!wallets.isEmpty()) {
	            wallet = wallets.get(0);
	            walletDropdown.getSelectionModel().selectFirst();
	            update();
	        }
	    	
	    	walletDropdown.setOnAction(event -> handleWalletSelection(wallets));
	    }
	    
	    public void handleWalletSelection(ObservableList<Wallet> wallets) {
	    	 String selectedWallet = walletDropdown.getSelectionModel().getSelectedItem();
	    	 
	    	 for(Wallet w : wallets) {
	    		 if (w.getWalletName().equals(selectedWallet)) {
	    	            wallet = w;
	    	            update(); // Update the UI with the new wallet details
	    	            break;
	    	        }
	    	 }
	    }
	    
	    
	    @FXML
	    public void GoToAddWalletScene() {
	    	
	    	try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddWallet.fxml"));
	            Scene addWallet = new Scene(loader.load());


	            Stage stage = (Stage) addWalletButton.getScene().getWindow();

	            stage.setScene(addWallet);
	            stage.setTitle("Add New Wallet");
	            stage.show();

	        } catch (IOException e) {
	        	
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    public void GoToWallet() {
	    	try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Wallet.fxml"));
	            Parent wallet = loader.load();


	            Stage stage = (Stage) backButton.getScene().getWindow();

	            stage.setScene(new Scene(wallet));
	            stage.setTitle("Wallet");
	            stage.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    	
	    }
	        
	    @Override
	    public void update() {
	    	if(wallet != null) {
	    		walletName.setText(wallet.getWalletName());
		        walletBalance.setText("Rp. " + wallet.getBalance().toPlainString());
	    	}
	    }
}
