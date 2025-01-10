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

@WebServlet("/send-request")
public class FriendRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder data = new StringBuilder();
        String line;
        try(BufferedReader reader = req.getReader()){
            while((line = reader.readLine()) != null){
                data.append(line);
            }
        }

        JSONObject json = new JSONObject(data.toString());
        String userId = json.getString("uId");
        String friendId = json.getString("fId");
        System.out.println("userId = " + userId);
        System.out.println("friendId = " + friendId);

        resp.setContentType("application/json");
        json = new JSONObject();
        json.put("status", 200);
        json.put("message", "request send");
        try(Writer writer = resp.getWriter()){
            writer.write(json.toString());
        }

        ChatWebSocket.sendFriendRequestNotification(userId ,friendId, DataBase.getNameById(userId));
    }

}
