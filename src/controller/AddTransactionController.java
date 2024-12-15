package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import factory.CategoryFactory;
import factory.TransactionFactory;
import factory.WalletFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Category;
import model.User;
import model.Wallet;
import util.ShowAlert;
import util.UserSession;

public class AddTransactionController {
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
    private Button backButton;

    @FXML
    private Button createTransactionButton;

    @FXML
    private Button AddCategoryButton;

    // Sample data for wallets
    private ArrayList<Wallet> walletList = WalletFactory.getWalletList();

    // Sample data for categories
    private ArrayList<Category> categoryList = CategoryFactory.getCategoryList();

    @FXML
    public void initialize() {
        setupCategoryComboBox();
        setupWalletComboBox();
        setupDatePicker();
        setupTransactionTypeToggleGroup();
    }

    private void setupDatePicker() {
        datePicker.setValue(LocalDate.now());
    }

    private void setupTransactionTypeToggleGroup() {
        transactionTypeToggleGroup = new ToggleGroup();
        incomeRadioButton.setToggleGroup(transactionTypeToggleGroup);
        expenseRadioButton.setToggleGroup(transactionTypeToggleGroup);
        incomeRadioButton.setSelected(true);
    }

    public void setupCategoryComboBox() {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getCategoryName());
        }
        ObservableList<String> category = FXCollections.observableArrayList(categoryNames);
        categoryComboBox.setItems(category);
    }

    public void setupWalletComboBox() {
        List<String> walletName = new ArrayList<>();
        for (Wallet wallet : walletList) {
            if (wallet.getWalletName() != "Overall Expenditure") {
                walletName.add(wallet.getWalletName());
            }
        }
        ObservableList<String> wallet = FXCollections.observableArrayList(walletName);
        walletComboBox.setItems(wallet);
    }

    @FXML
    public void createTransactionButtonOnAction(ActionEvent event) {
        String walletName = walletComboBox.getValue();
        String amountText = amountTextField.getText();
        String transactionType = incomeRadioButton.isSelected() ? "Income" : "Expense";
        String categoryName = categoryComboBox.getValue();
        String description = descriptionTextField.getText();
        LocalDate date = datePicker.getValue();
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (walletName == null || amountText.isEmpty() || categoryName == null || description.isEmpty()
                || date == null) {
            successLabel.setText("Please Fill all the Fields");
            return;
        }
        try {
            BigDecimal amount = new BigDecimal(amountText);
            Wallet selectedWallet = null;
            int walletId = -1;
            int categoryId = -1;
            for (Wallet wallet : walletList) {
                if (wallet.getWalletName() == walletName) {
                    selectedWallet = wallet;
                    walletId = wallet.getWalletId();
                    break;
                }
            }
            for (Category category : categoryList) {
                if (category.getCategoryName() == categoryName) {
                    categoryId = category.getCategoryId();
                    break;
                }
            }
            if (walletId == -1 || categoryId == -1) {
                successLabel.setText("Invalid wallet or category selection.");
                return;
            }

            if (transactionType.equalsIgnoreCase("Income")) {
                TransactionFactory.createTransaction(transactionType, currentUser.getUserId(), walletId, categoryId,
                        amount, description, date);
                selectedWallet.setBalance(selectedWallet.getBalance().add(amount));
            } else if (transactionType.equalsIgnoreCase("Expense")) {
                TransactionFactory.createTransaction(transactionType, currentUser.getUserId(), walletId, categoryId,
                        amount, description, date);
                selectedWallet.setBalance(selectedWallet.getBalance().subtract(amount));
            }

            // Display success message
            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Add Transaction is Successful", "Transaction is Created", "The Transaction was created successfully!");

            navigateToTransactionPage();

            // You can add logic to save this transaction to a database or list
        } catch (NumberFormatException e) {
            successLabel.setText("Invalid amount format.");
        }
    }

    private void navigateToTransactionPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Transaction.fxml"));
            Scene transactionScene = new Scene(loader.load());

            Stage currentStage = (Stage) createTransactionButton.getScene().getWindow();

            currentStage.setScene(transactionScene);
            currentStage.setTitle("Transactions");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            successLabel.setText("Error loading the transaction page.");
        }
    }

    @FXML
    public void BackButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Transaction.fxml"));
            Scene registerScene = new Scene(loader.load());

            Stage currentStage = (Stage) backButton.getScene().getWindow();

            currentStage.setScene(registerScene);
            currentStage.setTitle("Transaction");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void AddCategoryButtonOnAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCategory.fxml"));
            Scene registerScene = new Scene(loader.load());

            Stage currentStage = (Stage) AddCategoryButton.getScene().getWindow();

            currentStage.setScene(registerScene);
            currentStage.setTitle("Add Category");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
