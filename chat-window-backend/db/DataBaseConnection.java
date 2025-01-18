package com.base.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/chat";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "naga@123";

    private static Connection connection;

    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // ClassCastException
            connection =  DriverManager.getConnection(URL, USERNAME, PASSWORD); // SQL Exception
        } catch (Exception e){
            System.out.println("DB connection error...");
        }
        return connection;
    }
}
