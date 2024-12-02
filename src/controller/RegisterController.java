package controller;

import java.io.IOException;

import factory.UserFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import observer.UserObserver;

public class RegisterController implements UserObserver{

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
                redirectToLogin();
            } else {
                System.out.println("Passwords do not match.");
                onUser(false, "Password do not match.");
            }
        } else {
            System.out.println("Fields are not filled.");
            onUser(false, "Please fill all the fields.");
        }
    }
	@Override
	public void onUser(boolean success, String message) {
        // Update the message label in the UI
		RegisterMessageLabel.setText(message);
	    
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
