package dao;

import models.IngredientRecipe;
import utils.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class IngredientRecipeDAO {
    public void addIngredientToRecipe(IngredientRecipe ir) throws SQLException {
        String sql = "INSERT INTO Ingredient_Recipe (recipeId, ingredientId, quantity, unitId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ir.getRecipeId());
            stmt.setInt(2, ir.getIngredientId());
            stmt.setDouble(3, ir.getQuantity());
            stmt.setInt(4, ir.getUnitId());

            stmt.executeUpdate();
        }
    }

    public void getIngredientsByRecipeId(int recipeId) throws SQLException {
        String sql = """
                    SELECT i.ingredientName, ir.quantity, u.unitName AS unit
                    FROM Ingredient_Recipe ir
                    JOIN Ingredient i ON ir.ingredientId = i.ingredientId
                    JOIN Unit u ON ir.unitId = u.unitId
                    WHERE ir.recipeId = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.printf("%.2f %s of %s%n",
                        rs.getDouble("quantity"),
                        rs.getString("unit"),
                        rs.getString("ingredientName"));
            }
        }
    }

    public boolean updateIngredientInRecipe(IngredientRecipe ir) throws SQLException {
        String sql = "UPDATE Ingredient_Recipe SET quantity = ?, unitId = ? WHERE recipeId = ? AND ingredientId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, ir.getQuantity());
            stmt.setInt(2, ir.getUnitId());
            stmt.setInt(3, ir.getRecipeId());
            stmt.setInt(4, ir.getIngredientId());

            return stmt.executeUpdate() > 0;
        }
    }

}
