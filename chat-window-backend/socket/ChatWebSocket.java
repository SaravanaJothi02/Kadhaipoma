package socket;

import db.DataBase;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/{userId}")
public class ChatWebSocket {

    private static final Map<String, Session> activeUsers = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session){
        activeUsers.put(userId, session);
        DataBase.setOnlineStatus(userId, '1');
        List<String> unreadMessages = DataBase.getBalanceMessages(userId);
        try {
            for(String msg : unreadMessages){
                session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("user " + userId + " connected...");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String[] msg = message.split(":");
        String senderId = msg[0];
        String receiverId = msg[1];
        String text = msg[2];
        if(activeUsers.containsKey(receiverId)){
            DataBase.storeMessage(senderId, receiverId, text, "1");
            Session receiverSession = activeUsers.get(receiverId);
            receiverSession.getBasicRemote().sendText(message);
        } else {
            DataBase.storeMessage(senderId, receiverId, text, "0");
            System.out.println("receiver is offline...");
        }
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId, Session session){
        activeUsers.remove(userId);
        DataBase.setOnlineStatus(userId, '0');
        System.out.println("user " + userId + " disconnected...");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

}
