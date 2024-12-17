package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Transaction;
import model.User;
import util.ShowAlert;
import util.UserSession;
import factory.WalletFactory;

import java.io.IOException;
import java.math.BigDecimal;

public class AddWalletController {

    @FXML
    private Text balanceText, walletBalance;

    @FXML
    private Button addWalletButton, backButton;

    @FXML
    private Label walletName, addLabel, successLabel, descLabel;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn, categoryColumn, descriptionColumn, dateColumn;

    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;

    @FXML
    private TextField walletNameField, walletBalanceField;

    @FXML
    private TextArea walletDesc;

    @FXML
    public void initialize() {
        System.out.println("Showing Add Wallet Scene");
    }

    @FXML
    public void createNewWallet() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        String walletName = walletNameField.getText().trim();
        String balanceText = walletBalanceField.getText().trim();
        String description = walletDesc.getText().trim();

        if (!validateInput(walletName, balanceText)) {
            return;
        }

        if (description.isEmpty()) {
            description = "No description provided.";
        }

        try {
            BigDecimal balance = new BigDecimal(balanceText);
            WalletFactory.createWallet(currentUser.getUserId(), walletName, description, balance);
            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Add Wallet Successful", "Wallet Created", "The wallet was created successfully!");
            GoToWallet();
        } catch (NumberFormatException e) {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Balance", "Please enter a valid number for the balance.");
        }
    }

    @FXML
    public void GoToWallet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Wallet.fxml"));
            Scene walletScene = new Scene(loader.load());

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(walletScene);
            stage.setTitle("Wallet");
            stage.show();
        } catch (IOException e) {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to Load Wallet Page", "An error occurred while navigating to the wallet page.");
            e.printStackTrace();
        }
    }
    private boolean validateInput(String walletName, String balanceText) {
        if (walletName.isEmpty() || balanceText.isEmpty()) {
            ShowAlert.showAlert(Alert.AlertType.WARNING, "Input Error", "Missing Fields", "Please fill out all required fields.");
            return false;
        }
        return true;
    }
}
