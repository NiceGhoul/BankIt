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
	private Hyperlink TransactionButton;
	@FXML
	private Hyperlink StatsButton;
	
	@FXML
	private void WalletButtonOnAction() {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Wallet.fxml"));
            Scene mainScene = new Scene(loader.load());

            Stage currentStage = (Stage) WalletButton.getScene().getWindow();

            currentStage.setScene(mainScene);
            currentStage.setTitle("Wallet");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	@FXML
	private void TransactionButtonOnAction() {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Transaction.fxml"));
            Scene mainScene = new Scene(loader.load());

            Stage currentStage = (Stage) TransactionButton.getScene().getWindow();

            currentStage.setScene(mainScene);
            currentStage.setTitle("Transaction");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	@FXML
	private void StatsButtonOnAction() {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Stats.fxml"));
            Scene mainScene = new Scene(loader.load());

            Stage currentStage = (Stage) WalletButton.getScene().getWindow();

            currentStage.setScene(mainScene);
            currentStage.setTitle("Stats");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
