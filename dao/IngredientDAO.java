package dao;

import models.Ingredient;
import utils.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;

public class IngredientDAO {
    public int addIngredient(Ingredient ingredient) throws SQLException {
        String sql = "INSERT INTO Ingredient (ingredientName) VALUES (?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ingredient.getName());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                ingredient.setIngredientId(id);
                return id;
            }
        }
        return -1;
    }

    public Ingredient getIngredientById(int ingredientId) throws SQLException {
        String sql = "SELECT * FROM Ingredient WHERE ingredientId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ingredientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ingredient(
                        rs.getInt("ingredientId"),
                        rs.getString("ingredientName"));
            }
        }
        return null;
    }
}
