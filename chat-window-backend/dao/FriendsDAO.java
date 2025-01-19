package com.base.dao;

import com.base.model.Friend;
import com.base.model.FriendsStatus;
import com.base.model.Message;
import com.base.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendsDAO {
    private Connection connection;
    private MessageDAO messageDAO;
    private UserDAO userDAO;

    public FriendsDAO(Connection connection) {
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
        this.messageDAO = new MessageDAO(connection);
    }

    public List<String> getAllFriendsId(String userId) {
        List<String> allFriends = new ArrayList<>();
        String query = "SELECT user_id, friend_id FROM friends WHERE status = 'accepted' AND user_id = ? OR friend_id = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, userId);
            ps.setString(2, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String friendId = rs.getString("friend_id");
                allFriends.add(friendId.equals(userId) ? rs.getString("user_id") : friendId);
            }
        } catch (SQLException e){
            System.out.println("getAllFriends exception : "+e.getMessage());
        }
        return allFriends;
    }

    public HashMap<User, Message> getContactList(String userId) {
        HashMap<User, Message> contactListWithLastMessage = new HashMap<>();
        String query = "SELECT user_id, friend_id FROM friends WHERE status = 'accepted' AND user_id = ? OR friend_id = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, userId);
            ps.setString(1, userId);
            ps.setString(2, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String friendId = rs.getString("user_id").equals(userId) ?
                        rs.getString("friend_id") : rs.getString("user_id");
//                HashMap<User, Message> contactWithLastMessage = new HashMap<>();
//                contactWithLastMessage.put(
//                        userDAO.getUserById(friendId).get(),
//                        messageDAO.getLastMessage(friendId, userId).get()
//                );
                contactListWithLastMessage.put(userDAO.getUserById(friendId).get(),
                        messageDAO.getLastMessage(friendId, userId).isPresent() ? messageDAO.getLastMessage(friendId, userId).get() : null);
            }
        } catch(SQLException e){
            System.out.println("getContactList exception :"+e.getMessage());
        }
        return contactListWithLastMessage;
    }
}
