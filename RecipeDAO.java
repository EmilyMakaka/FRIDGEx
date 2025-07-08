package dao;

import models.Recipe;
import utils.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    public int addRecipe(Recipe recipe) throws SQLException {
        String sql = "INSERT INTO Recipe (recipeName,recipeDescription, userId) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDescription());
            stmt.setInt(3, recipe.getUserId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                recipe.setRecipeId(id);
                return id;
            }
        }
        return -1;
    }

    public Recipe getRecipeById(int recipeId) throws SQLException {
        String sql = "SELECT * FROM Recipe WHERE recipeId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Recipe(
                        rs.getInt("recipeId"),
                        rs.getString("recipeName"),
                        rs.getString("recipeDescription"),
                        rs.getInt("userId"));
            }
        }
        return null;
    }

    public List<Recipe> getRecipesByUserId(int userId) throws SQLException {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT * FROM Recipe WHERE userId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Recipe(
                        rs.getInt("recipeId"),
                        rs.getString("recipeName"),
                        rs.getString("recipeDescription"),
                        rs.getInt("userId")));
            }
        }
        return list;
    }

    public boolean updateRecipe(Recipe recipe) throws SQLException {
        String sql = "UPDATE Recipe SET recipeName = ?, recipeDescription = ? WHERE recipeId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDescription());
            stmt.setInt(3, recipe.getRecipeId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public boolean deleteRecipe(int recipeId) throws SQLException {
        // First delete all ingredients from the mapping table
        String deleteIngredients = "DELETE FROM Ingredient_Recipe WHERE recipeId = ?";
        String deleteRecipe = "DELETE FROM Recipe WHERE recipeId = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // transaction

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteIngredients);
                    PreparedStatement stmt2 = conn.prepareStatement(deleteRecipe)) {

                stmt1.setInt(1, recipeId);
                stmt1.executeUpdate();

                stmt2.setInt(1, recipeId);
                stmt2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT * FROM Recipe";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Recipe(
                        rs.getInt("recipeId"),
                        rs.getString("recipeName"),
                        rs.getString("recipeDescription"),
                        rs.getInt("userId")));
            }
        }
        return list;
    }

    public boolean insertRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipes (recipeName, description, ingredients, cooking_time, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDescription());
            // Convert ingredients list to a comma-separated string
            String ingredientsStr = String.join(",",
                    recipe.getIngredients().stream().map(Object::toString).toArray(String[]::new));
            stmt.setString(3, ingredientsStr);
            stmt.setInt(4, recipe.getCookingTime());
            stmt.setInt(5, recipe.getUserId());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        recipe.setRecipeId(generatedId);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Recipe> searchByName(String keyword) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM Recipe WHERE recipeName LIKE ?";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("recipeId"),
                        rs.getString("recipeName"),
                        rs.getString("recipeDescription"),
                        rs.getInt("userId"));
                recipes.add(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

}
