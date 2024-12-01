package controller;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;



public class RegisterController implements Initializable{
	
	@FXML
	private Label registerMessageLabel;
	@FXML
	private ImageView registerImageView;
	@FXML
	private TextField enterUserName, enterEmail;
	@FXML
	private PasswordField enterPassword, confirmPassword;
	@FXML

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		File registerImageFile = new File("images/10048464.jpg");
		Image registerImage = new Image(registerImageFile.toURI().toString());
		registerImageView.setImage(registerImage);
	}
	
	public void ShowRegistrationForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            Stage secondStage = new Stage();
			Scene scene = new Scene(loader.load());
			secondStage.setScene(scene);
			secondStage.setTitle("Registration");
			secondStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			e.getCause();
		}
	}
	
	public void registerButtonInAction(ActionEvent event) {
		if(usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false) {
			validateLogin();
		}else {
			loginMessageLabel.setText("Please enter Username and Password.");
		}
	}
	
	public void validateRegister() {
		
	}
	

}
