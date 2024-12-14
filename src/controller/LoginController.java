package controller;

import java.io.IOException;
import java.util.ArrayList;

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
import model.User;
import util.ShowAlert;
import util.UserSession;

public class LoginController {
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView ImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink registerButton;

    @FXML
    public void loginButtonOnAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = enterPasswordField.getText();

        // Validate login credentials
        if (!username.isBlank() && !password.isBlank()) {
            User user = validateLogin(username, password);
            if (user != null) {
                UserSession.getInstance().setCurrentUser(user);
                // Successful login, redirect to the main page
                System.out.println(user.getUsername() + "   " + user.getPassword());
                ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Login is Successful", "Login is SuccesFull", "The Login is successfull!\nRedirecting to The Main Page");
                redirectToMainPage();
            } else {
                // Failed login, show an error message
                System.out.println("Wrong username or password.");
                loginMessageLabel.setText("Wrong username or password.");
            }

        } else {
            System.out.println("Fields are not filled.");
            loginMessageLabel.setText("Please fill all the fields.");
        }
    }

    private User validateLogin(String username, String password) {
        ArrayList<User> userList = UserFactory.getUserList(); // Get the registered users list

        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Return true if a match is found
            }
        }
        return null; // Return false if no match is found
    }

    @FXML
    private void redirectToMainPage() {
        try {
            // Load the main page FXML (for example, MainPage.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Wallet.fxml"));
            Scene mainScene = new Scene(loader.load());

            // Get the current stage (login window)
            Stage currentStage = (Stage) loginButton.getScene().getWindow();

            // Set the new scene (main application page)
            currentStage.setScene(mainScene);
            currentStage.setTitle("Wallet");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Failed to load main page.");
            loginMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void registerButtonOnAction() {
        try {
            // Load the Register page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
            Scene registerScene = new Scene(loader.load());

            // Get the current stage (login window)
            Stage currentStage = (Stage) registerButton.getScene().getWindow();

            // Set the new scene (registration page)
            currentStage.setScene(registerScene);
            currentStage.setTitle("Register");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Failed to load register page.");
        }
    }

}
