package controller;

import dao.RecipeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Recipe;

public class RecipeFormController {

    @FXML
    private TextField recipeNameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Label messageLabel;

    private RecipeDAO recipeDAO = new RecipeDAO();

    @FXML
    void handleAddRecipe(ActionEvent event) {
        String name = recipeNameField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields are required.");
            return;
        }

        Recipe recipe = new Recipe(name, description, 1); // 🔥 Set user_id dynamically in the future
        try {
            int result = recipeDAO.addRecipe(recipe);

            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Recipe added successfully.");
                recipeNameField.clear();
                descriptionField.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add recipe.");
            }
        } catch (java.sql.SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the recipe.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        navigateTo("/view/user_dashboard.fxml", "User Dashboard", event);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateTo(String fxml, String title, ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(fxml)));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
