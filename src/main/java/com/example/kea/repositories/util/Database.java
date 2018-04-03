package com.example.kea.repositories.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final static String USERNAME = "kea1";
    private final static String PASSWORD = "Dc8aH-BE_5Ih";
    private final static String CONNSTRING = "jdbc:mysql://den1.mysql3.gear.host/kea1";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNSTRING, USERNAME, PASSWORD);
    }
}
