package search;

import db.DataBase;
import org.json.JSONObject;
import socket.ChatWebSocket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

@WebServlet("/friend-request-accept")
public class RequestAcceptedServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder data = new StringBuilder();
        String line;
        try(BufferedReader reader = req.getReader()){
            while((line = reader.readLine()) != null){
                data.append(line);
            }
        }

        JSONObject requestJson = new JSONObject(data.toString());
        String userId = requestJson.getString("uId");
        String friendId = requestJson.getString("fId");
        String status = requestJson.getString("status");

        JSONObject json = new JSONObject();
        if(status.equals("accept")){
            DataBase.addFriendRequest(userId, friendId, "success");
            DataBase.friendRequestReceiverStatus("send", friendId);
            json.put("status", 200);
            ChatWebSocket.addContact(userId, friendId, DataBase.getNameById(userId));
        } else {
            json.put("status", 202);
            DataBase.addFriendRequest(userId, friendId, "rejected");
            DataBase.friendRequestReceiverStatus("send", friendId);
        }

        try(Writer writer = resp.getWriter()){
            writer.write(json.toString());
        }

    }
}
