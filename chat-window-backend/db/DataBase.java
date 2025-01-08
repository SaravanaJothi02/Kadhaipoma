package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String URL = "jdbc:mysql://localhost:3306/chat";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "naga@123";

    private static Connection con;

    public static Connection getConnection(){
        if(con == null){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                return con;
            } catch (Exception e) {
                System.out.println("DB connection error....");
            }
        }
        return con;
    }

    public static void setOnlineStatus(String id, char status){
        getConnection();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE user SET status = '"+status+"' WHERE user_id = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void storeMessage(String senderId, String receiverId, String text, String status) {
        getConnection();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO message (sender_id, receiver_id, text, status) VALUES (?,?,?,?)");
            preparedStatement.setString(1, senderId);
            preparedStatement.setString(2, receiverId);
            preparedStatement.setString(3, text);
            preparedStatement.setString(4, status);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getBalanceMessages(String userId) {
        getConnection();
        List<String> msg = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT sender_id, receiver_id, text FROM message WHERE receiver_id = " + userId + " AND status = '0';"
            );
            while (resultSet.next()) {
                msg.add(resultSet.getString("sender_id") + ":" + resultSet.getString("receiver_id") + ":" + resultSet.getString("text"));
            }
            statement.executeUpdate(
                    "UPDATE message SET status = '1' WHERE receiver_id = " + userId + " AND status = '0';"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return msg;

    }

    public static void isUserExist(String userName) {

    }
}
