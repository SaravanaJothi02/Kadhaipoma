package com.base.dao;

import com.base.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MessageDAO {
    private Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * messages are set the delivered when the user is online
     * @param userId unique id of user
     */
    public void setMessageDelivered(String userId) {
        String query = "UPDATE messages SET status = 'delivered' WHERE receiver_id = ? AND status = 'send'";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, userId);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println("setMessageDelivered exception : "+e.getMessage());
        }
    }

    /**
     * Store the message into the database
     * @param senderId sender unique id
     * @param receiverId receiver unique id
     * @param text message content
     * @param status message status({@link com.base.model.MessageStatus})
     * @param timeStamp message timestamp
     */
    public void storeMessage(String senderId, String receiverId, String text, String status, String timeStamp) {
        String query = "INSERT INTO messages (sender_id, receiver_id, text, status, created_at) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, senderId);
            ps.setString(2, receiverId);
            ps.setString(3, text);
            ps.setString(4, status);
            ps.setString(5, timeStamp);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("storeMessage exception : "+e.getMessage());
        }
    }

    // sender id -frd & receiver id - me
    public Optional<Message> getLastMessage(String senderId, String receiverId){
        String query = "SELECT * FROM messages WHERE sender_id = ? AND receiver_id = ? ORDER BY created_at DESC LIMIT 1";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, senderId);
            ps.setString(2, receiverId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Message message = new Message(
                        Integer.parseInt(rs.getString("msg_id")),
                        Integer.parseInt(rs.getString("sender_id")),
                        Integer.parseInt(rs.getString("receiver_id")),
                        rs.getString("text"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                return Optional.of(message);
            }
        } catch (SQLException e){
            System.out.println("getLastMessage exception :"+e.getMessage());
        }
        return Optional.empty();
    }

}
