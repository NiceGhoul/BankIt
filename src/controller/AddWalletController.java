package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Expense;
import model.User;
import util.ShowAlert;
import util.UserSession;

import java.io.IOException;
import java.math.BigDecimal;
import factory.WalletFactory;

public class AddWalletController {

	@FXML
	private Text balanceText, walletBalance;
	@FXML
	private Button addWalletButton, backButton;
	@FXML
	private Label walletName, AddLabel, successLabel, descLabel;
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

		String walletName = walletNameField.getText();
		String balanceText = walletBalanceField.getText();
		String description = walletDesc.getText();

		if (walletName.isEmpty() || balanceText.isEmpty()) {
			System.out.println("Please fill out all required fields.");
			return;
		}

		if (description.isEmpty()) {
			description = " ";
		}
		WalletFactory.createWallet(currentUser.getUserId(), walletName, description, new BigDecimal(balanceText));
		ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Add Wallet is Successful", "Wallet is Created", "The Wallet was created successfully!");
		GoToWallet();

	}

	@FXML
	public void GoToWallet() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Wallet.fxml"));
			Scene wallet = new Scene(loader.load());

			Stage stage = (Stage) backButton.getScene().getWindow();

			stage.setScene(wallet);
			stage.setTitle("Wallet");
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
