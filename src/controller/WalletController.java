package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.Wallet;
import util.ShowAlert;
import util.UserSession;
import model.Transaction;
import model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import factory.TransactionFactory;
import factory.WalletFactory;

public class WalletController {

	@FXML
	private Text balanceText, walletBalance;
	@FXML
	private Button addWalletButton, backButton, deleteWalleButton;
	@FXML
	private Label walletName, AddLabel, descLabel;
	@FXML
	private TableView<Transaction> transactionTable;
	@FXML
	private ComboBox<String> walletDropdown;
	@FXML
	private Hyperlink logoutButton;
	@FXML
	private TableColumn<Transaction, Integer> idColumn, categoryColumn;
	@FXML
	private TableColumn<Transaction, String> transactionTypeColumn, descriptionColumn;
	@FXML
	private TableColumn<Transaction, BigDecimal> amountColumn;
	@FXML
	private TableColumn<Transaction, LocalDate> expenseDateColumn;
	@FXML
	private TextField walletNameField;

	private Wallet wallet;

	@FXML
	public void initialize() {
		initializeTable();
		initializeWallet();
		loadTransactionsForSelectedWallet();
	}

	public BigDecimal countTotalWallet() {
		ObservableList<Wallet> wallets = FXCollections.observableArrayList(WalletFactory.getWalletList());
		BigDecimal total = BigDecimal.ZERO;
		for (Wallet w : wallets) {
			total = total.add(w.getBalance());
		}
		return total;
	}

	public void AddDecimals(BigDecimal count) {
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
		walletBalance.setText("Rp. " + numberFormat.format(count));

	}

	public void initializeWallet() {

		User currentUser = UserSession.getInstance().getCurrentUser();
		BigDecimal total = countTotalWallet();
		if (WalletFactory.getWalletList().isEmpty()) {
			WalletFactory.createWallet(currentUser.getUserId(), "Overall Expenditure", "user overall wallet", total);
			// WalletFactory.createWallet(currentUser.getUserId(), "Investment Wallet",
			// "Investments Funds", new BigDecimal("10000.00"));
		}

		ObservableList<Wallet> wallets = FXCollections.observableArrayList(WalletFactory.getWalletList());

		for (Wallet w : wallets) {
			walletDropdown.getItems().add(w.getWalletName());
		}

		if (!wallets.isEmpty()) {
			wallet = wallets.get(0);
			walletDropdown.getSelectionModel().selectFirst();
			update();
			loadTransactionsForSelectedWallet();
		}

		walletDropdown.setOnAction(event -> handleWalletSelection(wallets));
		updateMainWallet();
		loadTransactionsForSelectedWallet();
	}

	public void initializeTable() {
		transactionTypeColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionType()));
		categoryColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty(cellData.getValue().getCategoryName()));
		amountColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
		descriptionColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
		expenseDateColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDate()));
	}

	public void loadTransactionsForSelectedWallet() {
		ObservableList<Transaction> walletTransactions;

		if (wallet != null && "Overall Expenditure".equals(wallet.getWalletName())) {
			LocalDate today = LocalDate.now();
			walletTransactions = FXCollections.observableArrayList(
					TransactionFactory.getTransactionList().stream()
							.filter(t -> t.getDate().isEqual(today))
							.collect(Collectors.toList()));
		} else if (wallet != null) {
			int walletId = wallet.getWalletId();
			walletTransactions = FXCollections.observableArrayList(
					TransactionFactory.getTransactionList().stream()
							.filter(t -> t.getWalletId() == walletId)
							.collect(Collectors.toList()));
		} else {
			walletTransactions = FXCollections.observableArrayList();
		}
		System.out.println("All Transactions: " + TransactionFactory.getTransactionList());
		transactionTable.setItems(walletTransactions);
	}

	public void handleWalletSelection(ObservableList<Wallet> wallets) {

		String selectedWallet = walletDropdown.getSelectionModel().getSelectedItem();

		for (Wallet w : wallets) {
			if (w.getWalletName().equals(selectedWallet)) {

				System.out.println("Selected Wallet ID: " + wallet.getWalletId());
				wallet = w;
				update();
				loadTransactionsForSelectedWallet();

				if ("Overall Expenditure".equals(wallet.getWalletName())) {
					updateMainWallet();
				}
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

	public void updateMainWallet() {
		AddDecimals(countTotalWallet());
	}

	public void update() {
		if (wallet != null) {
			walletName.setText(wallet.getWalletName());
			AddDecimals(wallet.getBalance());
			descLabel.setText(wallet.getDescription());
		}
	}

	public void deleteWalletButtonOnAction(){
		String selectedWalletName = walletDropdown.getValue();
		if(selectedWalletName.equals("Overall Expenditure")){
			ShowAlert.showAlert(Alert.AlertType.INFORMATION, "This wallet can't be deleted", "Delete Confirmation", "This overall expenditure cannot be deleted.");
			return;
		}
		ShowAlert.showAlert(Alert.AlertType.CONFIRMATION,"Delete Wallet", "Delete Wallet", "Are you sure you want to delete this wallet?");
		int walletId = getWalletIdByName(selectedWalletName);
		WalletFactory.getWalletList().removeIf(wallet -> wallet.getWalletId() == walletId);
		TransactionFactory.getTransactionList().removeIf(transaction -> transaction.getWalletId() == walletId);
		setWalletToOverallExpenditure();
	}

	private void setWalletToOverallExpenditure() {
		// Refresh the wallet dropdown with the updated wallet list
		walletDropdown.setItems(FXCollections.observableArrayList(
			WalletFactory.getWalletList().stream()
				.map(Wallet::getWalletName)
				.collect(Collectors.toList())
		));
	
		Wallet overallExpenditureWallet = WalletFactory.getWalletList().stream()
				.filter(wallet -> wallet.getWalletName().equalsIgnoreCase("Overall Expenditure"))
				.findFirst()
				.orElse(null);
	
		if (overallExpenditureWallet != null) {
			walletDropdown.setValue(overallExpenditureWallet.getWalletName());
			wallet = overallExpenditureWallet;
			update();
			loadTransactionsForSelectedWallet();
		} else {
			User currentUser = UserSession.getInstance().getCurrentUser();
			WalletFactory.createWallet(currentUser.getUserId(), "Overall Expenditure", "User overall wallet", BigDecimal.ZERO);
			refreshWalletDropdown();
			setWalletToOverallExpenditure();
		}
	}

	private void refreshWalletDropdown() {
		walletDropdown.setItems(FXCollections.observableArrayList(
			WalletFactory.getWalletList().stream()
				.map(Wallet::getWalletName)
				.collect(Collectors.toList())
		));
	}
	
	

	private int getWalletIdByName(String walletName) {
		return WalletFactory.getWalletList().stream()
				.filter(wallet -> wallet.getWalletName().equals(walletName))
				.map(Wallet::getWalletId)
				.findFirst()
				.orElse(-1);
	}
	

	@FXML
	public void logoutButtonOnAction(ActionEvent event) {
		// Clear the user session
		UserSession.getInstance().clearSession();

		// Load the login page
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			Scene loginScene = new Scene(loader.load());

			// Get the current stage
			Stage currentStage = (Stage) logoutButton.getScene().getWindow();

			currentStage.setScene(loginScene);
			currentStage.setTitle("Login");
			currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			ShowAlert.showAlert(Alert.AlertType.ERROR, "Logout Failed", "An error occurred while logging out.", "Please try again.");
		}
	}
	
}
