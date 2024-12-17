package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Expense;
import model.Income;
import model.Transaction;
import model.Wallet;
import util.UserSession;
import factory.TransactionFactory;
import factory.WalletFactory;

import util.ShowAlert;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionController {

	@FXML
	private Button AddTransactionButton;

	@FXML
	private Button exportButton;

	@FXML
	private ComboBox<String> dateComboBox;

	@FXML
	private ComboBox<String> monthComboBox;

	@FXML
	private ComboBox<String> yearComboBox;

	@FXML
	private ComboBox<String> typeComboBox;


	@FXML
	private Hyperlink logoutButton;

	@FXML
	private Text incomeText;

	@FXML
	private Text expenseText;

	@FXML
	private Text totalText;

	@FXML
	private TableView<Transaction> transactionTableView;

	@FXML
	private TableColumn<Transaction, String> transactionTypeColumn;

	@FXML
	private TableColumn<Transaction, String> categoryColumn;

	@FXML
	private TableColumn<Transaction, BigDecimal> amountColumn;

	@FXML
	private TableColumn<Transaction, String> descriptionColumn;

	@FXML
	private TableColumn<Transaction, String> walletColumn;

	@FXML
	private TableColumn<Transaction, LocalDate> DateColumn;
	@FXML
	private TableColumn<Transaction, Void> actionColumn;

	private List<Transaction> transactions = TransactionFactory.getTransactionList();

	private List<Wallet> wallet = WalletFactory.getWalletList();

	@FXML
	private void initialize() {
		setupDateComboBox();
		setupMonthComboBox();
		setupYearComboBox();
		setupTypeComboBox();
		setupTableView();
		filterTransactions(); 
	}

	private void setupMonthComboBox() {
		ObservableList<String> months = FXCollections.observableArrayList(
				"January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December");
		monthComboBox.setItems(months);
		monthComboBox.setValue(months.get(LocalDate.now().getMonthValue() - 1));
		monthComboBox.setOnAction(event -> filterTransactions());
	}

	@FXML
	private void exportButtonOnAction(){
		exportToExcel();
	}

	private void exportToExcel() {
        
    }


	private void setupDateComboBox() {
		ObservableList<String> dates = FXCollections.observableArrayList();
		dates.add(null);
		for (int i = 1; i <= 31; i++) {
			dates.add(String.valueOf(i));
		}
		dateComboBox.setItems(dates);
		dateComboBox.setValue(null);
		dateComboBox.setOnAction(event -> filterTransactions());
	}

	private void setupYearComboBox() {
		ObservableList<String> years = FXCollections.observableArrayList();
		int currentYear = LocalDate.now().getYear();
		for (int year = 2020; year <= currentYear; year++) {
			years.add(String.valueOf(year));
		}
		yearComboBox.setItems(years);
		yearComboBox.setValue(String.valueOf(currentYear));
		yearComboBox.setOnAction(event -> filterTransactions());
	}

	private void setupTypeComboBox() {
		typeComboBox.setItems(FXCollections.observableArrayList("Income", "Expense",
				"Show All"));
		typeComboBox.setValue("Show All");
		typeComboBox.setOnAction(event -> filterTransactions());
	}

	private void setupTableView() {
		transactionTypeColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionType()));
		categoryColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty(cellData.getValue().getCategoryName()));
		amountColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
		descriptionColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
		DateColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDate()));
		walletColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getWalletName()));
		Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>> cellFactory = param -> new TableCell<>() {
			private final Button updateButton = new Button("Update");
			private final Button deleteButton = new Button("Delete");
			private final HBox buttonsBox = new HBox(10, updateButton, deleteButton);

			{
				deleteButton.setOnAction(event -> {
					Transaction transaction = getTableView().getItems().get(getIndex());
					deleteTransaction(transaction);
				});
				updateButton.setOnAction(event -> {
					Transaction transaction = getTableView().getItems().get(getIndex());
					updateTransaction(transaction);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(buttonsBox);
				}
			}
		};
		actionColumn.setCellFactory(cellFactory);
	}

	private void updateTransaction(Transaction transaction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateTransaction.fxml"));
			Scene updateScene = new Scene(loader.load());

			UpdateTransactionController controller = loader.getController();
			controller.setTransactionData(transaction);

			Stage currentStage = (Stage) transactionTableView.getScene().getWindow();
			currentStage.setScene(updateScene);
			currentStage.setTitle("Update Transaction");
			currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Wallet getWalletById(int walletId) {
		return wallet.stream()
				.filter(wallet -> wallet.getWalletId() == walletId)
				.findFirst()
				.orElse(null);
	}

	private void deleteTransaction(Transaction transaction) {
		ShowAlert.showAlert(Alert.AlertType.CONFIRMATION, "Delete Transaction", "Delete Transaction",
				"Are you sure you want to delete this transaction");
		Wallet wallet = getWalletById(transaction.getWalletId());
		if (transaction instanceof Income) {
			wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
		} else if (transaction instanceof Expense) {
			wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
		}
		transactions.remove(transaction);
		transactionTableView.setItems(FXCollections.observableArrayList(transactions));

	}

	private void filterTransactions() {
		String selectedDate = dateComboBox.getValue();
		String selectedMonth = monthComboBox.getValue();
		String selectedYear = yearComboBox.getValue();
		String type = typeComboBox.getValue();

		List<Transaction> filteredList = transactions;

		if (selectedMonth != null && selectedYear != null) {
			int month = monthComboBox.getItems().indexOf(selectedMonth) + 1;
			int year = Integer.parseInt(selectedYear);
			filteredList = filteredList.stream()
					.filter(t -> t.getDate().getMonthValue() == month && t.getDate().getYear() == year)
					.collect(Collectors.toList());
		}

		if (selectedDate != null) {
			int day = Integer.parseInt(selectedDate);
			filteredList = filteredList.stream()
					.filter(t -> t.getDate().getDayOfMonth() == day)
					.collect(Collectors.toList());
		}

		if (type != null && !type.equalsIgnoreCase("Show All")) {
			filteredList = filteredList.stream()
					.filter(t -> t.getTransactionType().equalsIgnoreCase(type))
					.collect(Collectors.toList());
		}

		updateTableView(filteredList);
		updateTotals(filteredList);

	}

	private void updateTableView(List<Transaction> filteredList) {
		transactionTableView.setItems(FXCollections.observableArrayList(filteredList));
	}

	private void updateTotals(List<Transaction> filteredList) {
		BigDecimal totalIncome = filteredList.stream()
				.filter(t -> t.getTransactionType().equalsIgnoreCase("Income"))
				.map(Transaction::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalExpense = filteredList.stream()
				.filter(t -> t.getTransactionType().equalsIgnoreCase("Expense"))
				.map(Transaction::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal netTotal = totalIncome.subtract(totalExpense);

		incomeText.setText(totalIncome.toString());
		expenseText.setText(totalExpense.toString());
		totalText.setText(netTotal.toString());
	}

	@FXML
	public void AddTransactionButtonOnAction(ActionEvent event) {
		int size = wallet.size();
		if (size <= 1) {
			ShowAlert.showAlert(Alert.AlertType.WARNING, "Add Wallet", "Insufficient Wallets",
					"Please add more wallets before creating a transaction.");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTransaction.fxml"));
			Scene registerScene = new Scene(loader.load());

			Stage currentStage = (Stage) AddTransactionButton.getScene().getWindow();

			currentStage.setScene(registerScene);
			currentStage.setTitle("Register");
			currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
