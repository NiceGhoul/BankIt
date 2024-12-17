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

	private boolean needsRecalculation = false;

	@FXML
	public void initialize() {
		initializeTable();
		initializeWallet();
		setWalletToOverallExpenditure(); 
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

		Wallet overallExpenditureWallet = WalletFactory.getWalletList().stream()
				.filter(wallet -> wallet.getWalletName().equalsIgnoreCase("Overall Expenditure"))
				.findFirst()
				.orElse(null);

		if (overallExpenditureWallet == null) {
			WalletFactory.createWallet(currentUser.getUserId(), "Overall Expenditure", "User overall wallet",
					BigDecimal.ZERO);
			needsRecalculation = true;
		}

		ObservableList<Wallet> wallets = FXCollections.observableArrayList(WalletFactory.getWalletList());

		walletDropdown.setItems(FXCollections.observableArrayList(wallets.stream()
				.map(Wallet::getWalletName)
				.collect(Collectors.toList())));

		if (!wallets.isEmpty()) {
			wallet = wallets.get(0);
			walletDropdown.getSelectionModel().selectFirst();
			update();
			loadTransactionsForSelectedWallet();
		}

		walletDropdown.setOnAction(event -> handleWalletSelection(wallets));

		if (needsRecalculation) {
			setWalletToOverallExpenditure();
		}
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
				if ("Overall Expenditure".equals(wallet.getWalletName())) {
					needsRecalculation = true;
					setWalletToOverallExpenditure();
				} else {
					update();
					loadTransactionsForSelectedWallet();
				}
				break;
			}
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

	public void deleteWalletButtonOnAction() {
		String selectedWalletName = walletDropdown.getValue();

		if (selectedWalletName == null) {
			ShowAlert.showAlert(Alert.AlertType.WARNING, "No Wallet Selected", "Delete Wallet",
					"Please select a wallet to delete.");
			return;
		}

		if (selectedWalletName.equals("Overall Expenditure")) {
			ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Cannot Delete", "Delete Confirmation",
					"This overall expenditure cannot be deleted.");
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Wallet");
		alert.setHeaderText("Delete Wallet");
		alert.setContentText("Are you sure you want to delete the wallet: " + selectedWalletName + "?");

		alert.showAndWait().ifPresent(response -> {
			if (response == javafx.scene.control.ButtonType.OK) {
				int walletId = getWalletIdByName(selectedWalletName);

				boolean walletRemoved = WalletFactory.getWalletList()
						.removeIf(wallet -> wallet.getWalletId() == walletId);

				if (walletRemoved) {
					System.out.println("Wallet deleted successfully: " + selectedWalletName);

					TransactionFactory.getTransactionList()
							.removeIf(transaction -> transaction.getWalletId() == walletId);
					System.out.println("Remaining Transactions: " + TransactionFactory.getTransactionList());

					needsRecalculation = true;
					setWalletToOverallExpenditure(); 
					refreshWalletDropdown(); 

					if (!WalletFactory.getWalletList().isEmpty()) {
						walletDropdown.getSelectionModel().selectFirst();
						wallet = WalletFactory.getWalletList().get(0);
						update();
						loadTransactionsForSelectedWallet();
					}
				} else {
					ShowAlert.showAlert(Alert.AlertType.ERROR, "Delete Failed", "Error",
							"Failed to delete the wallet.");
				}
			}
		});
	}

	private void setWalletToOverallExpenditure() {
		System.out.println("Recalculating Overall Expenditure...");

		List<Wallet> wallets = WalletFactory.getWalletList();
		wallets.forEach(w -> System.out.println("Wallet: " + w.getWalletName() + ", Balance: " + w.getBalance()));

		Wallet overallExpenditureWallet = wallets.stream()
				.filter(wallet -> wallet.getWalletName().equalsIgnoreCase("Overall Expenditure"))
				.findFirst()
				.orElse(null);

		if (overallExpenditureWallet != null) {
			BigDecimal totalBalance = wallets.stream()
					.filter(wallet -> !wallet.getWalletName().equalsIgnoreCase("Overall Expenditure"))
					.map(Wallet::getBalance)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			System.out.println("Total Balance: " + totalBalance);

			overallExpenditureWallet.setBalance(totalBalance);
			System.out.println("Updated Overall Expenditure Balance: " + overallExpenditureWallet.getBalance());

			walletDropdown.setValue(overallExpenditureWallet.getWalletName());
			wallet = overallExpenditureWallet;

			update();
			loadTransactionsForSelectedWallet();
		}
	}

	@FXML
	public void GoToAddWalletScene() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddWallet.fxml"));
			Scene addWalletScene = new Scene(loader.load());

			Stage stage = (Stage) addWalletButton.getScene().getWindow();
			stage.setScene(addWalletScene);
			stage.setTitle("Add New Wallet");

			stage.showAndWait(); 

			System.out.println("Returning from Add Wallet scene. Triggering recalculation.");
			setWalletToOverallExpenditure();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void refreshWalletDropdown() {
		walletDropdown.setItems(FXCollections.observableArrayList(
				WalletFactory.getWalletList().stream()
						.map(Wallet::getWalletName)
						.collect(Collectors.toList())));
		setWalletToOverallExpenditure();
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
		UserSession.getInstance().clearSession();

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			Scene loginScene = new Scene(loader.load());

			Stage currentStage = (Stage) logoutButton.getScene().getWindow();

			currentStage.setScene(loginScene);
			currentStage.setTitle("Login");
			currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			ShowAlert.showAlert(Alert.AlertType.ERROR, "Logout Failed", "An error occurred while logging out.",
					"Please try again.");
		}
	}

}
