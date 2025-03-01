package com.microsoft.example;
package com.contoso.myshuttle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.*;
import com.microsoft.example.models.*;

/**
 * DatabaseAccess handles MySQL connections for the MyShuttle application.
 */
public class DataAccess {
    // Updated database name to 'alm'
    private static final String DB_URL = "jdbc:mysql://35.209.100.48:3306/alm?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Muneer@788";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}


    /**
     * Simple check to see if login succeeds, without returning the Employee model.
     */
    public static boolean loginSuccessful(String employeeEmail, String employeePassword) {
        return (login(employeeEmail, employeePassword) != null);
    }

    /**
     * Retrieve an employee by username/email and password
     */
    public static Employee login(String employeeEmail, String employeePassword) {
        String query = "SELECT * FROM employees WHERE username=? AND password=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employeeEmail);
            stmt.setString(2, employeePassword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    /**
     * Return all the fares for a given Employee's ID #
     */
    public static List<Fare> employeeFares(int empID) {
        String query = "SELECT * FROM fares WHERE emp_id=?";
        List<Fare> results = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, empID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Fare(rs.getInt("id"), rs.getInt("emp_id"),
                        rs.getString("pickup"), rs.getString("dropoff"),
                        rs.getTimestamp("start"), rs.getTimestamp("end"),
                        rs.getInt("fare_charge"), rs.getInt("driver_fee"),
                        rs.getInt("passenger_rating"), rs.getInt("driver_rating")));
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return results;
    }

    /**
     * Return total fare for an employee
     */
    public static float getFareTotal(int empID) {
        String query = "SELECT SUM(fare_charge) as totalfare FROM fares WHERE emp_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, empID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalfare") / 100.0f;
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }

    /**
     * Return total driver fee for an employee
     */
    public static float getTotalDriverFee(int empID) {
        String query = "SELECT SUM(driver_fee) as totaldriverfee FROM fares WHERE emp_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, empID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totaldriverfee") / 100.0f;
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }
}
