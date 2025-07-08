package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.User;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        // Populate roles in ComboBox
        roleComboBox.getItems().addAll("user", "admin");
        roleComboBox.getSelectionModel().selectFirst(); // default selection
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Missing Fields", "Please fill in all fields.");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        // Mock confirmation process on a new thread
        new Thread(() -> {
            try {
                Platform.runLater(() -> showLoadingDialog("Sending confirmation email... 📧"));

                Thread.sleep(2000); // Simulate delay for realism

                int userId = userDAO.addUser(user);

                Platform.runLater(() -> {
                    hideLoadingDialog();

                    if (userId > 0) {
                        showAlert("Success", "✅ Account created!\n\nWe've sent a confirmation to " + email);
                        switchScene("/view/login.fxml", event);
                    } else {
                        showAlert("Error", "Account creation failed.");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    hideLoadingDialog();
                    showAlert("Database Error", "Could not complete registration.");
                });
            }
        }).start();
    }

    private Dialog<Void> loadingDialog;

    private void showLoadingDialog(String message) {
        loadingDialog = new Dialog<>();
        loadingDialog.setTitle("Please Wait");
        loadingDialog.setHeaderText(null);

        Label content = new Label(message);
        content.setStyle("-fx-font-size: 14px;");
        loadingDialog.getDialogPane().setContent(content);

        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        loadingDialog.initOwner(usernameField.getScene().getWindow());
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.close();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        switchScene("/view/welcome.fxml", event);
    }

    private void switchScene(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not open screen: " + fxmlPath);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}