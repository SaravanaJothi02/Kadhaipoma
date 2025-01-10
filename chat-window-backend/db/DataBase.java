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
            System.out.println("setOnlineStatus exception : "+ e.getMessage());
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
            System.out.println("storeMessage exception : "+ e.getMessage());
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
            System.out.println("getBalanceMessages exception : "+ e.getMessage());
        }
        return msg;

    }

    public static boolean isUserExist(String userName) {
        getConnection();
        try{
            String query = "SELECT * FROM user WHERE user_name = '"+userName+"';";
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("user exist exception : "+ e.getMessage());
        }
        return false;
    }

    public static String addUser(String userName, String password) {
        getConnection();
        try{
            String query = "INSERT INTO user (user_name, password, status) VALUES(?,?,'0')";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,userName);
            ps.setString(2, password);
            ps.executeUpdate();
            query = "SELECT user_id FROM user WHERE user_name = ?;";
            ps = con.prepareStatement(query);
            ps.setString(1, userName);
            ResultSet resultSet = ps.executeQuery();
            String id = "";
            while(resultSet.next()){
                id = resultSet.getString("user_id");
            }
            return id;
        } catch (SQLException e) {
            System.out.println("addUser exception : "+ e.getMessage());
        }
        return null;
    }

    public static String isValidUser(String userName, String password) {
        getConnection();
        try{
            String query = "SELECT * FROM user WHERE user_name = ? AND password = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            String id = "-1";
            while(resultSet.next()){
                id = resultSet.getString("user_id");
            }
            return id;
        } catch (SQLException e) {
            System.out.println("isValidUser exception : "+e.getMessage());
        }
        return "-1";
    }

    public static List<String[]> getContactList(String userId) {
        getConnection();
        try{
            String query = "SELECT map.friend_id, user_name FROM\n" +
                           "    (\n" +
                           "        SELECT user.user_id, contact.friend_id\n" +
                           "        FROM user\n" +
                           "        JOIN contact ON user.user_id = contact.user_id\n" +
                           "        WHERE user.user_id = ?\n" +
                           "    ) as map\n" +
                           "JOIN user ON map.friend_id = user.user_id;\n";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet resultSet = ps.executeQuery();
            List<String[]> contactList = new ArrayList<>();
            while(resultSet.next()){
                contactList.add(new String[]{resultSet.getString("friend_id"), resultSet.getString("user_name")});
            }
            return contactList;
        } catch (SQLException e) {
            System.out.println("getContactList exception : "+e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<String[]> getUserList(String key) {
        getConnection();
        try{
            String query = "SELECT user_id, user_name FROM user WHERE user_name LIKE '"+key+"%' LIMIT 10";
            PreparedStatement ps = con.prepareStatement(query);
            List<String[]> userList = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                userList.add(new String[]{rs.getString("user_id"), rs.getString("user_name")});
            }
            return userList;
        } catch(SQLException e){
            System.out.println("getUserList exception : "+e.getMessage());
        }
        return new ArrayList<>();
    }

    public static void addFriendRequest(String userId, String friendId, String status) {
        getConnection();
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO request (user_id, friend_id, status, receive_status) VALUES(?,?,?,'not-send')");
            ps.setString(1, userId);
            ps.setString(2, friendId);
            ps.setString(3, status);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("addFriend exception : "+e.getMessage());
        }
    }

    public static String getNameById(String friendId) {
        getConnection();
        try{
            String query = "SELECT user_name FROM user WHERE user_id ="+friendId+";";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs.next() ? rs.getString("user_name") : null;
        }catch (SQLException e) {
            System.out.println("getNameById exception : "+e.getMessage());
        }
        return null;
    }

    public static List<String> getBalanceNotification(String userId) {
        getConnection();
        List<String> notifications = new ArrayList<>();
        try{
            String query = "SELECT request.user_id as friend_id, user_name\n" +
                           "FROM request\n" +
                           "JOIN user ON request.user_id = user.user_id\n" +
                           "WHERE request.receive_status = 'not-send' AND request.friend_id = ?;\n";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                notifications.add("friend request:"+rs.getString("friend_id")+":"+rs.getString("user_name"));
            }
        } catch (Exception e) {
            System.out.println("getBalanceNotification exception : " + e.getMessage());
        }
        return notifications;
    }

    public static void friendRequestReceiverStatus(String status, String friend_id) {
        getConnection();
        try{
            con.createStatement().executeUpdate("UPDATE request SET receive_status = '"+status+"' WHERE friend_id = '"+friend_id+"'");
        } catch (Exception e) {
            System.out.println("friendRequestReceiverStatus exception :" +e.getMessage());
        }
    }

    public static void addContact(String userId, String friendId) {
        getConnection();
        try{
            String query = "INSERT INTO contact VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ps.setString(2, friendId);
            ps.executeUpdate();
            ps = con.prepareStatement(query);
            ps.setString(1, friendId);
            ps.setString(2, userId);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("addContact exception : "+e.getMessage());
        }
    }
}
