package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Transaction;
import factory.TransactionFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionController {

    @FXML
    private Button AddTransactionButton;

    @FXML
    private ComboBox<String> comboBox;

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
    private TableColumn<Transaction, LocalDate> expenseDateColumn;

    private List<Transaction> transactions = TransactionFactory.test(); // Assuming this returns all transactions

    // Initialize method called automatically after FXML is loaded
    @FXML
    public void initialize() {
        setupComboBox();
        setupTableView();
        filterTransactions("Daily"); // Default selection
    }

    private void setupComboBox() {
        comboBox.setItems(FXCollections.observableArrayList("Daily", "Weekly", "Monthly", "Yearly"));
        comboBox.setValue("Daily"); // Set default value
        comboBox.setOnAction(event -> filterTransactions(comboBox.getValue()));
    }

    private void setupTableView() {
        transactionTypeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionType()));
        categoryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCategoryId()));
        amountColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
        descriptionColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        expenseDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDate()));
    }

    // Filter transactions based on ComboBox selection
    private void filterTransactions(String period) {
        LocalDate today = LocalDate.now();
        List<Transaction> filteredList;

        switch (period) {
            case "Daily":
                filteredList = transactions.stream()
                        .filter(t -> t.getDate().equals(today))
                        .collect(Collectors.toList());
                break;
            case "Weekly":
                filteredList = transactions.stream()
                        .filter(t -> t.getDate().isAfter(today.minusDays(7)))
                        .collect(Collectors.toList());
                break;
            case "Monthly":
                filteredList = transactions.stream()
                        .filter(t -> t.getDate().getMonth() == today.getMonth() && t.getDate().getYear() == today.getYear())
                        .collect(Collectors.toList());
                break;
            case "Yearly":
                filteredList = transactions.stream()
                        .filter(t -> t.getDate().getYear() == today.getYear())
                        .collect(Collectors.toList());
                break;
            default:
                filteredList = transactions;
        }

        updateTableView(filteredList);
        updateTotals(filteredList);
    }

    private void updateTableView(List<Transaction> filteredList) {
        ObservableList<Transaction> observableList = FXCollections.observableArrayList(filteredList);
        transactionTableView.setItems(observableList);
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
        // Load the AddTransactionPage (this depends on how your navigation is set up)
        System.out.println("Navigate to AddTransactionPage");
    }
}
