package com.base.chat.contact;

import com.base.dao.MessageDAO;
import com.base.db.DataBaseConnection;
import com.base.model.Message;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/get-message")
public class ContactMessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get-message");
        try {
            List<Message> messageList = getMessages(req);

            List<JSONObject> response = new ArrayList<>();
            for (Message msg : messageList) {
                response.add(
                        new JSONObject()
                                .put("senderId", msg.getSenderId())
                                .put("receiverId", msg.getReceiverId())
                                .put("text", msg.getText())
                                .put("status", msg.getStatus())
                                .put("timestamp", msg.getCreatedAt())
                );
            }

            try (Writer writer = resp.getWriter()) {
                resp.setStatus(HttpServletResponse.SC_OK); // 200
                writer.write(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
        }
    }

    private static List<Message> getMessages(HttpServletRequest req) throws IOException {
        StringBuilder jsonString = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        }

        JSONObject json = new JSONObject(jsonString.toString());
        String userId = json.getString("userId");
        String contactId = json.getString("contactId");

        MessageDAO messageDAO = new MessageDAO(DataBaseConnection.getConnection());
        return messageDAO.getContactMessages(userId, contactId);
    }
}

/*
req json =>
{
    "userId" : 1,
    "contactId" : 2
}
msglist =>
[
    {
        senderId : 1,
        receiverId : 2,
        text : "msg",
        timestamp : 2342345234
    },
    {
        senderId : 2,
        receiverId : 1,
        text : "msg",
        timestamp : 2342345234
    }
]
 */
