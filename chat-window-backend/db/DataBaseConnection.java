package com.base.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/chat";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "naga@123";

    private static Connection connection;

    public static Connection getConnection() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver"); // ClassCastException
        return DriverManager.getConnection(URL, USERNAME, PASSWORD); // SQL Exception
    }
}
