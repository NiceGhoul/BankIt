package util;

import javafx.scene.control.Alert;

public class ShowAlert {
    public static void showAlert(Alert.AlertType alertType, String title, String Header, String Content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(Header);
		alert.setContentText(Content);
		alert.showAndWait();
	}
}
