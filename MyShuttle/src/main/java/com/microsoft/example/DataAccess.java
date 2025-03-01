package com.microsoft.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.microsoft.example.models.*;

public class DataAccess {
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://35.209.100.48:3306/myshuttle?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Muneer@788";

    private static Connection theConnection;
    
    private static PreparedStatement LOGIN;
    private static PreparedStatement REGISTER;
    private static PreparedStatement FARES;
    private static PreparedStatement GETTOTAL;

    static {
        try {
            // Bootstrap driver
            Class.forName(DB_DRIVER);

            String conStr = System.getenv("MYSQLCONNSTR_MyShuttleDb");
            if (conStr == null || conStr.trim().length() == 0) {
                theConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            } else {
                theConnection = DriverManager.getConnection(conStr);
            }

            // Prepare SQL statements
            LOGIN = theConnection.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
            REGISTER = theConnection.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
            FARES = theConnection.prepareStatement("SELECT * FROM fares WHERE emp_id=?");
            GETTOTAL = theConnection.prepareStatement("SELECT SUM(fare_charge) AS totalfare, SUM(driver_fee) AS totaldriverfee FROM fares WHERE emp_id=?");

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex.toString());
        }
    }

    /**
     * User registration method
     */
    public static boolean registerUser(String username, String email, String password) {
        try {
            REGISTER.clearParameters();
            REGISTER.setString(1, username);
            REGISTER.setString(2, email);
            REGISTER.setString(3, password);  // TODO: Store hashed password in production
            
            int rowsInserted = REGISTER.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        }
    }

    /**
     * Simple check to see if login succeeds, without returning the User model
     */
    public static boolean loginSuccessful(String email, String password) {
        return (login(email, password) != null);
    }

    /**
     * Retrieve user by email and password
     */
    public static User login(String email, String password) {
        try {
            LOGIN.clearParameters();
            LOGIN.setString(1, email);
            LOGIN.setString(2, password);
            
            try (ResultSet rs = LOGIN.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"));
                }
                return null;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return null;
        }
    }

    /**
     * Return all the fares for a given Employee's ID #
     */    
    public static List<Fare> employeeFares(int empID) {
        try {
            FARES.clearParameters();
            FARES.setInt(1, empID);
            
            try (ResultSet rs = FARES.executeQuery()) {
                List<Fare> results = new ArrayList<>(20);
                while (rs.next()) {
                    results.add(new Fare(rs.getInt("id"), rs.getInt("emp_id"),
                        rs.getString("pickup"), rs.getString("dropoff"),
                        rs.getTimestamp("start"), rs.getTimestamp("end"),
                        rs.getInt("fare_charge"), rs.getInt("driver_fee"),
                        rs.getInt("passenger_rating"), rs.getInt("driver_rating")));
                }
                return results;
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return Collections.emptyList();
        }
    }
}
