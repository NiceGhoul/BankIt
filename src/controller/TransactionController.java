package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Transaction;
import factory.TransactionFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionController {

	@FXML
	private Button AddTransactionButton;

	@FXML
	private ComboBox<String> dateComboBox;

	@FXML
	private ComboBox<String> monthComboBox;

	@FXML
	private ComboBox<String> yearComboBox;

	@FXML
	private ComboBox<String> typeComboBox;

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
	private TableColumn<Transaction, Integer> categoryColumn;

	@FXML
	private TableColumn<Transaction, BigDecimal> amountColumn;

	@FXML
	private TableColumn<Transaction, String> descriptionColumn;

	@FXML
	private TableColumn<Transaction, LocalDate> DateColumn;

	private List<Transaction> transactions = TransactionFactory.test();

	// Initialize method called automatically after FXML is loaded
	@FXML
	private void initialize() {
		setupDateComboBox();
		setupMonthComboBox();
		setupYearComboBox();
		setupTypeComboBox();
		setupTableView();
		filterTransactions(); // Initial filter
	}

	private void setupMonthComboBox() {
		// Populate monthComboBox with month names
		ObservableList<String> months = FXCollections.observableArrayList(
				"January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December");
		monthComboBox.setItems(months);
		monthComboBox.setValue(months.get(LocalDate.now().getMonthValue() - 1));
		monthComboBox.setOnAction(event -> filterTransactions());
	}

	private void setupDateComboBox() {
		ObservableList<String> dates = FXCollections.observableArrayList();
		dates.add(null); // Allow null value
		for (int i = 1; i <= 31; i++) {
			dates.add(String.valueOf(i));
		}
		dateComboBox.setItems(dates);
		dateComboBox.setValue(null);
		dateComboBox.setOnAction(event -> filterTransactions());
	}

	private void setupYearComboBox() {
		// Populate yearComboBox with years from 2020 to current year
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
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCategoryId()));
		amountColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
		descriptionColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
		DateColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDate()));
	}

	// Filter transactions based on ComboBox selection
	private void filterTransactions() {
		String selectedDate = dateComboBox.getValue();
		String selectedMonth = monthComboBox.getValue();
		String selectedYear = yearComboBox.getValue();
		String type = typeComboBox.getValue();

		List<Transaction> filteredList = transactions;

		// Filter by month and year
		if (selectedMonth != null && selectedYear != null) {
			int month = monthComboBox.getItems().indexOf(selectedMonth) + 1;
			int year = Integer.parseInt(selectedYear);
			filteredList = filteredList.stream()
					.filter(t -> t.getDate().getMonthValue() == month && t.getDate().getYear() == year)
					.collect(Collectors.toList());
		}

		// Filter by date if selected
		if (selectedDate != null) {
			int day = Integer.parseInt(selectedDate);
			filteredList = filteredList.stream()
					.filter(t -> t.getDate().getDayOfMonth() == day)
					.collect(Collectors.toList());
		}

		// Filter by transaction type if not "Show All"
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

	// Handle button click to navigate to AddTransactionPage
	@FXML
	public void AddTransactionButtonOnAction(ActionEvent event) {
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
