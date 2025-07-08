package controller;

import dao.RecipeDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Recipe;
import models.User;

import java.util.List;

public class AdminDashboardController {

    @FXML
    private TextArea outputArea;

    @FXML
    private TextField deleteUserField;

    @FXML
    private TextField deleteRecipeField;

    private UserDAO userDAO = new UserDAO();
    private RecipeDAO recipeDAO = new RecipeDAO();

    @FXML
    void handleViewUsers(ActionEvent event) {
        try {
            List<User> users = userDAO.getAllUsers();
            outputArea.clear();
            outputArea.appendText("=== Users ===\n");
            for (User user : users) {
                outputArea.appendText("ID: " + user.getUserId() + ", Name: " + user.getUsername() + ", Role: "
                        + user.getRole() + "\n");
            }
        } catch (java.sql.SQLException e) {
            outputArea.clear();
            outputArea.appendText("Error retrieving users: " + e.getMessage() + "\n");
        }
    }

    @FXML
    void handleDeleteUser(ActionEvent event) {
        int userId = Integer.parseInt(deleteUserField.getText());
        try {
            boolean result = userDAO.deleteUser(userId);
            outputArea.appendText(result ? "User deleted.\n" : "Failed to delete user.\n");
        } catch (java.sql.SQLException e) {
            outputArea.appendText("Error deleting user: " + e.getMessage() + "\n");
        }
    }

    @FXML
    void handleViewRecipes(ActionEvent event) {
        try {
            List<Recipe> recipes = recipeDAO.getAllRecipes();
            outputArea.clear();
            outputArea.appendText("=== Recipes ===\n");
            for (Recipe r : recipes) {
                outputArea.appendText("ID: " + r.getRecipeId() + ", Name: " + r.getName() + "\n");
            }
        } catch (java.sql.SQLException e) {
            outputArea.clear();
            outputArea.appendText("Error retrieving recipes: " + e.getMessage() + "\n");
        }
    }

    @FXML
    void handleDeleteRecipe(ActionEvent event) {
        int recipeId = Integer.parseInt(deleteRecipeField.getText());
        try {
            boolean result = recipeDAO.deleteRecipe(recipeId);
            outputArea.appendText(result ? "Recipe deleted.\n" : "Failed to delete recipe.\n");
        } catch (java.sql.SQLException e) {
            outputArea.appendText("Error deleting recipe: " + e.getMessage() + "\n");
        }
    }
}
