package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.DBConnection;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;


public class LoginController implements Initializable{
	@FXML
	private Label loginMessageLabel;
	@FXML
	private ImageView loginImageView;
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField enterPasswordField;
	
	public void loginButtonOnAction(ActionEvent event) {
		if(usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false) {
			validateLogin();
		}else {
			loginMessageLabel.setText("Please enter Username and Password.");
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		File loginImageFIle = new File("images/10048463.jpg");
		Image loginImage = new Image(loginImageFIle.toURI().toString());
		loginImageView.setImage(loginImage);
		
	}
	
	public void validateLogin() {
		DBConnection dbConnection = DBConnection.getInstance();
        Connection con = dbConnection.getConnection();
        
        String verifyLogin = "SELECT count(1) From users WHERE username = '" + usernameTextField.getText()+"' AND password = '" + enterPasswordField.getText()+"'";
        try {
			Statement statement = con.createStatement();
			ResultSet queryResult = statement.executeQuery(verifyLogin);
			while(queryResult.next()) {
				if(queryResult.getInt(1) == 1) {
					loginMessageLabel.setText("Congratulation!");
				}else {
					loginMessageLabel.setText("Invalid Login please try again");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			e.getCause();
		}
	}
}
