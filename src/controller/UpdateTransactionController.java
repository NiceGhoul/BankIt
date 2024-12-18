package controller;

import factory.CategoryFactory;
import factory.TransactionFactory;
import factory.WalletFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Category;
import model.Transaction;
import model.Wallet;
import strategy.ExpenseStrategy;
import strategy.IncomeStrategy;
import strategy.TransactionTypeStrategy;
import util.ShowAlert;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class UpdateTransactionController {

    @FXML
    private ComboBox<String> walletComboBox;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private RadioButton incomeRadioButton;

    @FXML
    private RadioButton expenseRadioButton;

    @FXML
    private ToggleGroup transactionTypeToggleGroup;

    @FXML
    private TextField amountTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Label successLabel;

    @FXML
    private Button updateTransactionButton;

    @FXML
    private Button backButton;

    private Transaction currentTransaction;
    private List<Wallet> walletList = WalletFactory.getWalletList();
    private List<Category> categoryList = CategoryFactory.getCategoryList();

    @FXML
    public void initialize() {
        setupWalletComboBox();
        setupCategoryComboBox();
    }

    public void setTransactionData(Transaction transaction) {
        this.currentTransaction = transaction;

        walletComboBox.setValue(getWalletNameById(transaction.getWalletId()));
        categoryComboBox.setValue(getCategoryNameById(transaction.getCategoryId()));
        amountTextField.setText(transaction.getAmount().toString());
        descriptionTextField.setText(transaction.getDescription());
        datePicker.setValue(transaction.getDate());

        if (transaction.getTransactionType().equalsIgnoreCase("Income")) {
            incomeRadioButton.setSelected(true);
        } else {
            expenseRadioButton.setSelected(true);
        }
    }

    private void setupWalletComboBox() {
        ObservableList<String> walletNames = FXCollections.observableArrayList();
        for (Wallet wallet : walletList) {
            walletNames.add(wallet.getWalletName());
        }
        walletComboBox.setItems(walletNames);
    }

    private void setupCategoryComboBox() {
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        for (Category category : categoryList) {
            categoryNames.add(category.getCategoryName());
        }
        categoryComboBox.setItems(categoryNames);
    }

    private String getWalletNameById(int walletId) {
        return walletList.stream()
                .filter(wallet -> wallet.getWalletId() == walletId)
                .map(Wallet::getWalletName)
                .findFirst()
                .orElse(null);
    }

    private Wallet getWalletById(int walletId) {
        return walletList.stream()
                .filter(wallet -> wallet.getWalletId() == walletId)
                .findFirst()
                .orElse(null);
    }

    private String getCategoryNameById(int categoryId) {
        return categoryList.stream()
                .filter(category -> category.getCategoryId() == categoryId)
                .map(Category::getCategoryName)
                .findFirst()
                .orElse(null);
    }

    @FXML
    public void updateTransactionButtonOnAction(ActionEvent event) {
        try {
            String walletName = walletComboBox.getValue();
            String categoryName = categoryComboBox.getValue();
            String amountText = amountTextField.getText();
            String description = descriptionTextField.getText();
            LocalDate date = datePicker.getValue();
            String transactionType = incomeRadioButton.isSelected() ? "Income" : "Expense";

            if (walletName == null || categoryName == null || amountText.isEmpty() || description.isEmpty()
                    || date == null) {
                successLabel.setText("Please fill all the fields!");
                return;
            }

            BigDecimal amount = new BigDecimal(amountText);

            int newWalletId = getWalletIdByName(walletName);
            Wallet newWallet = getWalletById(newWalletId);
            int categoryId = getCategoryIdByName(categoryName);

            if (newWalletId == -1 || categoryId == -1) {
                successLabel.setText("Invalid wallet or category selection!");
                return;
            }

            Wallet oldWallet = getWalletById(currentTransaction.getWalletId());
            BigDecimal currentAmount = currentTransaction.getAmount();

            oldWallet.setBalance(currentTransaction.getTransactionTypeStrategy().reverseBalance(oldWallet.getBalance(),
                    currentAmount));

            TransactionTypeStrategy newStrategy = transactionType.equals("Income") ? new IncomeStrategy()
                    : new ExpenseStrategy();
            currentTransaction.setTransactionTypeStrategy(newStrategy);

            currentTransaction.setWalletId(newWalletId);
            currentTransaction.setCategoryId(categoryId);
            currentTransaction.setAmount(amount);
            currentTransaction.setDescription(description);
            currentTransaction.setDate(date);

            newWallet.setBalance(newStrategy.updateBalance(newWallet.getBalance(), amount));
            updateTransactionInDataSource(currentTransaction);

            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Update Transaction Successful", "Transaction Updated",
                    "The transaction was updated successfully!");

            navigateToTransactionPage();

        } catch (NumberFormatException e) {
            successLabel.setText("Invalid amount format!");
        }
    }

    private void updateTransactionInDataSource(Transaction updatedTransaction) {
        List<Transaction> transactions = TransactionFactory.getTransactionList();
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == updatedTransaction.getId()) {
                transactions.set(i, updatedTransaction);
                break;
            }
        }
    }

    private void navigateToTransactionPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Transaction.fxml"));
            Scene transactionScene = new Scene(loader.load());

            Stage currentStage = (Stage) updateTransactionButton.getScene().getWindow();

            currentStage.setScene(transactionScene);
            currentStage.setTitle("Transactions");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            successLabel.setText("Error loading the transaction page.");
        }
    }

    private int getWalletIdByName(String walletName) {
        return walletList.stream()
                .filter(wallet -> wallet.getWalletName().equals(walletName))
                .map(Wallet::getWalletId)
                .findFirst()
                .orElse(-1);
    }

    private int getCategoryIdByName(String categoryName) {
        return categoryList.stream()
                .filter(category -> category.getCategoryName().equals(categoryName))
                .map(Category::getCategoryId)
                .findFirst()
                .orElse(-1);
    }

    @FXML
    public void BackButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Transaction.fxml"));
            Scene transactionScene = new Scene(loader.load());

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.setScene(transactionScene);
            currentStage.setTitle("Transactions");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
