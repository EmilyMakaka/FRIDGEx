package dao;

import models.Unit;
import utils.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class UnitDAO {
    public int addUnit(Unit unit) throws SQLException {
        String sql = "INSERT INTO Unit (unitName, abbreviation) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, unit.getName());
            stmt.setString(2, unit.getAbbreviation());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                unit.setUnitId(id);
                return id;
            }
        }
        return -1;
    }

    public Unit getUnitById(int unitId) throws SQLException {
        String sql = "SELECT * FROM Unit WHERE unitId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, unitId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Unit(
                        rs.getInt("unitId"),
                        rs.getString("unitName"),
                        rs.getString("abbreviation"));
            }
        }
        return null;
    }

    public Unit getUnitByNameAndAbbreviation(String unitName, String abbreviation) throws SQLException {
        String sql = "SELECT * FROM Unit WHERE unitName = ? AND abbreviation = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, unitName);
            stmt.setString(2, abbreviation);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Unit(
                        rs.getInt("unitId"),
                        rs.getString("unitName"),
                        rs.getString("abbreviation"));
            }
        }
        return null; // Not found
    }

    // Update a unit by its ID
    public boolean updateUnit(Unit unit) throws SQLException {
        String sql = "UPDATE Unit SET name = ?, abbreviation = ? WHERE unitId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, unit.getName());
            stmt.setString(2, unit.getAbbreviation());
            stmt.setInt(3, unit.getUnitId());
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete a unit by its ID
    public boolean deleteUnit(int unitId) throws SQLException {
        String sql = "DELETE FROM Unit WHERE unitId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, unitId);
            return stmt.executeUpdate() > 0;
        }
    }

}