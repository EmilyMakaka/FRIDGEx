package controller;

import dao.UserDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import java.net.URL;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Run DB logic on a background thread
        new Thread(() -> {
            try {
                User user = userDAO.login(email, password);

                if (user != null) {
                    System.out.println("✅ Login successful: " + user.getUsername());

                    // Choose the dashboard FXML path based on user role
                    String path;
                    if (user.getRole() != null && user.getRole().equalsIgnoreCase("admin")) {
                        path = "/view/admin_dashboard.fxml";
                    } else {
                        path = "/view/user_dashboard.fxml";
                    }

                    URL fxmlUrl = getClass().getResource(path);
                    if (fxmlUrl == null) {
                        System.err.println("❌ FXML not found at: " + path);
                        Platform.runLater(
                                () -> showAlert("Error", "Dashboard screen is missing. Check your FXML path."));
                        return;
                    }

                    // Load scene on JavaFX Application Thread
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(fxmlUrl);
                            Scene dashboardScene = new Scene(loader.load());

                            Object controller = loader.getController();

                            // Pass user to dashboard controller if possible
                            if (controller instanceof UserDashboardController) {
                                ((UserDashboardController) controller).setUser(user);
                            }

                            Stage stage = (Stage) emailField.getScene().getWindow();
                            stage.setScene(dashboardScene);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showAlert("Error", "Failed to load dashboard.");
                        }
                    });

                } else {
                    Platform.runLater(() -> showAlert("Login Failed", "Invalid email or password."));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Error", "Could not connect to database."));
            }
        }).start();
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            Scene registerScene = new Scene(loader.load());

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(registerScene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open register screen.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
