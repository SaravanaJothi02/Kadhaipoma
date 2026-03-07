package com.base.chat.socket;

import com.base.dao.MessageDAO;
import com.base.dao.UserDAO;
import com.base.db.DataBase;
import com.base.db.DataBaseConnection;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/{userId}")
public class ChatWebSocket {

    private static final Map<String, Session> activeUsers = new ConcurrentHashMap<>();

    public static void sendFriendRequestNotification(String userId, String friendId, String name) throws IOException {
        if(activeUsers.containsKey(friendId)){
            activeUsers.get(friendId).getBasicRemote().sendText("friend request:"+userId+":"+name);
        }
    }

    public static void addContact(String userId, String friendId, String name) throws IOException {
        if(activeUsers.containsKey(friendId)){
            activeUsers.get(friendId).getBasicRemote().sendText("add contact:"+userId+":"+name);
        }
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session){
        activeUsers.put(userId, session);
        try {
            UserDAO userDAO = new UserDAO(DataBaseConnection.getConnection());
            userDAO.setOnlineStatus(userId, true);
            MessageDAO messageDAO = new MessageDAO(DataBaseConnection.getConnection());
            messageDAO.setMessageDelivered(userId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("onOpen exception : "+e.getMessage());
        }
        /*
//        List<String> balanceNotification = DataBase.getBalanceNotification(userId);
        try {
            for (String notification : balanceNotification){
//                System.out.print(notification+" ");
                session.getBasicRemote().sendText(notification);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
        
        System.out.println("user " + userId + " connected...");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String[] msg = message.split(":");
        String senderId = msg[0];
        String receiverId = msg[1];
        String text = msg[2];
        try {
            MessageDAO messageDAO = new MessageDAO(DataBaseConnection.getConnection());
            if(activeUsers.containsKey(receiverId)){
                messageDAO.storeMessage(senderId, receiverId, text, "delivered");
                Session receiverSession = activeUsers.get(receiverId);
                receiverSession.getBasicRemote().sendText(message);
            } else {
                messageDAO.storeMessage(senderId, receiverId, text, "sent");
                System.out.println("receiver is offline...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("onMessage exception : "+e.getMessage());
        }
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId, Session session){
        activeUsers.remove(userId, session);
        try {
            UserDAO userDAO = new UserDAO(DataBaseConnection.getConnection());
            userDAO.setOnlineStatus(userId, false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("onClose exception : "+e.getMessage());
        }
        System.out.println("user " + userId + " disconnected...");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

}
