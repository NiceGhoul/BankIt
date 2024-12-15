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
import model.Transaction;
import model.Wallet;
import util.UserSession;
import factory.TransactionFactory;
import factory.WalletFactory;

import util.ShowAlert;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import com.itextpdf.text.*;
// import com.itextpdf.text.pdf.*;

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
	private ComboBox<String> dateComboBox;

	@FXML
	private ComboBox<String> monthComboBox;

	@FXML
	private ComboBox<String> yearComboBox;

	@FXML
	private ComboBox<String> typeComboBox;

	@FXML
	private ComboBox<String> exportComboBox;

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

	private static List<Transaction> transactions2 = TransactionFactory.test();
	@FXML
	private TableColumn<Transaction, Void> actionColumn;

	private List<Transaction> transactions = TransactionFactory.getTransactionList();

	private List<Wallet> wallet = WalletFactory.getWalletList();

	// Initialize method called automatically after FXML is loaded
	
	public static List<Transaction> getTransactions2() {
	    return transactions2;
	}

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

	private void setupExportComboBox() {
		ObservableList<String> exportOptions = FXCollections.observableArrayList("Export to Excel", "Export to PDF");
		exportComboBox.setItems(exportOptions);
		// exportComboBox.setOnAction(event -> handleExportAction());
	}

	// private void handleExportAction() {
	// String selectedOption = exportComboBox.getValue();
	// if ("Export to Excel".equals(selectedOption)) {
	// exportToExcel();
	// } else if ("Export to PDF".equals(selectedOption)) {
	// exportToPDF();
	// }
	// }
	// private void exportToExcel() {
	// FileChooser fileChooser = new FileChooser();
	// fileChooser.setTitle("Save Excel File");
	// fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel
	// Files", "*.xlsx"));
	// Stage stage = (Stage) exportComboBox.getScene().getWindow();
	// java.io.File file = fileChooser.showSaveDialog(stage);

	// if (file != null) {
	// try (Workbook workbook = new XSSFWorkbook()) {
	// Sheet sheet = workbook.createSheet("Transactions");

	// // Create header row
	// Row headerRow = sheet.createRow(0);
	// String[] headers = {"Transaction Type", "Wallet", "Category", "Amount",
	// "Description", "Date"};
	// for (int i = 0; i < headers.length; i++) {
	// Cell cell = headerRow.createCell(i);
	// cell.setCellValue(headers[i]);
	// }

	// // Populate data rows
	// int rowNum = 1;
	// for (Transaction transaction : transactions) {
	// Row row = sheet.createRow(rowNum++);
	// row.createCell(0).setCellValue(transaction.getTransactionType());
	// row.createCell(1).setCellValue(transaction.getWalletName());
	// row.createCell(2).setCellValue(transaction.getCategoryName());
	// row.createCell(3).setCellValue(transaction.getAmount().toString());
	// row.createCell(4).setCellValue(transaction.getDescription());
	// row.createCell(5).setCellValue(transaction.getDate().toString());
	// }

	// // Write the output to a file
	// FileOutputStream fileOut = new FileOutputStream(file);
	// workbook.write(fileOut);
	// fileOut.close();

	// ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Data has been
	// exported to Excel successfully!");

	// } catch (IOException e) {
	// e.printStackTrace();
	// ShowAlert.showAlert(Alert.AlertType.ERROR, "Export Failed", "An error occurred while
	// exporting to Excel.");
	// }
	// }
	// }

	// private void exportToPDF() {
	// FileChooser fileChooser = new FileChooser();
	// fileChooser.setTitle("Save PDF File");
	// fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF
	// Files", "*.pdf"));
	// Stage stage = (Stage) exportComboBox.getScene().getWindow();
	// java.io.File file = fileChooser.showSaveDialog(stage);

	// if (file != null) {
	// Document document = new Document();
	// try {
	// PdfWriter.getInstance(document, new FileOutputStream(file));
	// document.open();

	// // Add Title
	// document.add(new Paragraph("Transaction List\n\n"));

	// // Create table
	// PdfPTable table = new PdfPTable(6);
	// table.addCell("Transaction Type");
	// table.addCell("Wallet");
	// table.addCell("Category");
	// table.addCell("Amount");
	// table.addCell("Description");
	// table.addCell("Date");

	// for (Transaction transaction : transactions) {
	// table.addCell(transaction.getTransactionType());
	// table.addCell(transaction.getWalletName());
	// table.addCell(transaction.getCategoryName());
	// table.addCell(transaction.getAmount().toString());
	// table.addCell(transaction.getDescription());
	// table.addCell(transaction.getDate().toString());
	// }

	// document.add(table);
	// document.close();

	// ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Data has been
	// exported to PDF successfully!");

	// } catch (Exception e) {
	// e.printStackTrace();
	// ShowAlert.showAlert(Alert.AlertType.ERROR, "Export Failed", "An error occurred while
	// exporting to PDF.");
	// }
	// }
	// }

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
	
			// Get the controller and pass the transaction data
			UpdateTransactionController controller = loader.getController();
			controller.setTransactionData(transaction);
	
			// Get current stage and set the new scene
			Stage currentStage = (Stage) transactionTableView.getScene().getWindow();
			currentStage.setScene(updateScene);
			currentStage.setTitle("Update Transaction");
			currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteTransaction(Transaction transaction) {
		transactions.remove(transaction);
		transactionTableView.setItems(FXCollections.observableArrayList(transactions));
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
