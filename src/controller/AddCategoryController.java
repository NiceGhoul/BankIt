package controller;

import java.io.IOException;

import factory.CategoryFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {
    @FXML
    private Button backButton;

    @FXML
    private TextField categoryTextField;

    @FXML
    private Button createCategoryButton;

    @FXML
    private Label successLabel;

    @FXML
    public void initialize() {
        System.out.println("Showing Add Category Scene");
    }

    public void BackButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTransaction.fxml"));
            Scene registerScene = new Scene(loader.load());

            Stage currentStage = (Stage) backButton.getScene().getWindow();

            currentStage.setScene(registerScene);
            currentStage.setTitle("Add Transaction");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCategoryButtonOnAction(ActionEvent event) {
        String categoryName = categoryTextField.getText();
        if (categoryName == null) {
            successLabel.setText("Please fill the category name!");
            return;
        }
        CategoryFactory.createCategory(categoryName);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Add Category is Successful");
        alert.setHeaderText("Category is Created");
        alert.setContentText("The Category was created successfully!");
        alert.showAndWait();
        navigateToAddTransactionPage();

    }

    public void navigateToAddTransactionPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTransaction.fxml"));
            Scene registerScene = new Scene(loader.load());

            Stage currentStage = (Stage) backButton.getScene().getWindow();

            currentStage.setScene(registerScene);
            currentStage.setTitle("Add Transaction");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
