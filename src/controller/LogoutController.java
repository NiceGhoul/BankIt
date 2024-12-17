package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import util.ShowAlert;
import util.UserSession;

public class LogoutController {
    @FXML
    private Hyperlink logoutButton;
    @FXML
	public void logoutButtonOnAction(ActionEvent event) {
		UserSession.getInstance().clearSession();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
			Scene loginScene = new Scene(loader.load());

			Stage currentStage = (Stage)logoutButton.getScene().getWindow();

			currentStage.setScene(loginScene);
			currentStage.setTitle("Login");
			currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			ShowAlert.showAlert(Alert.AlertType.ERROR, "Logout Failed", "An error occurred while logging out.", "Please try again.");
		}
	}
	
}
