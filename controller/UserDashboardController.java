package controller;

import dao.RecipeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Recipe;
import models.User;

import java.util.List;

public class UserDashboardController {
    private User user;

    @FXML
    private Label welcomeLabel; // Make sure this label exists in your FXML

    public void setUser(User user) {
        this.user = user;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + " 👋");
        }
    }

    @FXML
    private TextField searchField;
    @FXML
    private TextArea outputArea;

    private RecipeDAO recipeDAO = new RecipeDAO();

    @FXML
    void handleCreateRecipe(ActionEvent event) {
        navigateTo("/view/recipe_form.fxml", "Create Recipe", event);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        navigateTo("/view/login.fxml", "Login", event);
    }

    @FXML
    void handleViewRecipes(ActionEvent event) {
        outputArea.clear();
        try {
            List<Recipe> recipes = recipeDAO.getAllRecipes();
            // Example usage of 'user': display the username at the top of the recipe list
            if (user != null) {
                outputArea.appendText("Recipes for " + user.getUsername() + ":\n");
            }
            for (Recipe r : recipes) {
                outputArea.appendText("▶ " + r.getName() + ": " + r.getDescription() + "\n");
            }
        } catch (java.sql.SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve recipes: " + e.getMessage());
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a keyword.");
            return;
        }

        List<Recipe> recipes = recipeDAO.searchByName(keyword);
        outputArea.clear();
        if (recipes.isEmpty()) {
            outputArea.appendText("No recipes found.\n");
        } else {
            for (Recipe r : recipes) {
                outputArea.appendText("▶ " + r.getName() + ": " + r.getDescription() + "\n");
            }
        }
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
