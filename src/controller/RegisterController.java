package controller;

import java.io.IOException;

import factory.UserFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.ShowAlert;
public class RegisterController{

	@FXML
	private ImageView ImageView;
	@FXML
	private Label RegisterMessageLabel;
	@FXML
	private TextField usernameRegisterTextField;
	@FXML
	private TextField emailRegisterTextField;
	@FXML
	private PasswordField enterRegisterPasswordField;
	@FXML
	private PasswordField confirmRegisterPasswordField;
	@FXML
    private Button RegisterButton;
	@FXML
	private Hyperlink LoginButton;
	

	@FXML
    public void registerButtonOnAction(ActionEvent event) {
        String username = usernameRegisterTextField.getText();
        String email = emailRegisterTextField.getText();
        String password = enterRegisterPasswordField.getText();
        String confirmPassword = confirmRegisterPasswordField.getText();

        if (!username.isBlank() && !email.isBlank() && !password.isBlank() && !confirmPassword.isBlank()) {
            if (password.equals(confirmPassword)) {
                System.out.println("Passwords match. Creating user...");
                UserFactory.createUser(username, password, email, confirmPassword);
                System.out.println("User added. Redirecting to login...");
				ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Register is Successful", "Register is SuccesFull", "The Register is successfull!\nRedirecting to login page");
                redirectToLogin();
            } else {
                System.out.println("Passwords do not match.");
				RegisterMessageLabel.setText("Password do not match.");
				
            }
        } else {
            System.out.println("Fields are not filled.");
			RegisterMessageLabel.setText("Please fill all the fields.");
        }
    }
	private void redirectToLogin() {
	    try {
	        // Load the login FXML
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
	        Scene loginScene = new Scene(loader.load());

	        // Get the current stage (the main window)
	        Stage currentStage = (Stage) RegisterButton.getScene().getWindow();

	        // Set the new scene (login screen)
	        currentStage.setScene(loginScene);

	        // Optionally, you can change the window title
	        currentStage.setTitle("Login");

	        // Show the login window
	        currentStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Handle the exception (e.g., show an error message to the user)
	    }
	}
	public void LoginButtonOnAction() {
		try {
			UserFactory.createUser("bertrand", "12345", "bertrand@gmail.com", "12345");
	        // Load the Login FXML
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
	        Scene loginScene = new Scene(loader.load());

	        // Get the current stage (the main window)
	        Stage currentStage = (Stage) LoginButton.getScene().getWindow();

	        // Set the new scene (login screen)
	        currentStage.setScene(loginScene);

	        // Optionally, you can change the window title
	        currentStage.setTitle("Login");

	        // Show the login window
	        currentStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Handle the exception (e.g., show an error message to the user)
	    }
	}
}
