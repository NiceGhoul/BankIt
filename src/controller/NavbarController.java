package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class NavbarController {
	@FXML
	private Hyperlink WalletButton;
	@FXML
	private Hyperlink ExpenseButton;
	@FXML
	private Hyperlink StatsButton;
	
	@FXML
	private void WalletButtonOnAction() {
		try {
            // Load the main page FXML (for example, MainPage.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Wallet.fxml"));
            Scene mainScene = new Scene(loader.load());

            // Get the current stage (login window)
            Stage currentStage = (Stage) WalletButton.getScene().getWindow();

            // Set the new scene (main application page)
            currentStage.setScene(mainScene);
            currentStage.setTitle("Wallet");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	@FXML
	private void ExpenseButtonOnAction() {
		try {
            // Load the main page FXML (for example, MainPage.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Expense.fxml"));
            Scene mainScene = new Scene(loader.load());

            // Get the current stage (login window)
            Stage currentStage = (Stage) ExpenseButton.getScene().getWindow();

            // Set the new scene (main application page)
            currentStage.setScene(mainScene);
            currentStage.setTitle("Expense");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	@FXML
	private void StatsButtonOnAction() {
		try {
            // Load the main page FXML (for example, MainPage.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Stats.fxml"));
            Scene mainScene = new Scene(loader.load());

            // Get the current stage (login window)
            Stage currentStage = (Stage) WalletButton.getScene().getWindow();

            // Set the new scene (main application page)
            currentStage.setScene(mainScene);
            currentStage.setTitle("Stats");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
