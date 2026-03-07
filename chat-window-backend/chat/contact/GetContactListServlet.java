package com.base.chat.contact;

import com.base.dao.FriendsDAO;
import com.base.db.DataBaseConnection;
import com.base.model.Message;
import com.base.model.User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/get-contact-list")
public class GetContactListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
/*
//        String userId = (String)req.getSession(true).getAttribute("userId");
//        System.out.println(userId);
        List<String[]> contactList = DataBase.getContactList(userId);
//        contactList.forEach(contact -> System.out.println(Arrays.toString(contact)));
        resp.setContentType("application/json");
        JSONObject[] contacts = new JSONObject[contactList.size()];
        for (int i = 0; i < contactList.size(); i++) {
            JSONObject friend = new JSONObject();
            friend.put("id", contactList.get(i)[0]);
            friend.put("name", contactList.get(i)[1]);
            contacts[i] = friend;
        }
        try(Writer writer = resp.getWriter()){
            writer.write(Arrays.toString(contacts));
        }
 */
        try {
            FriendsDAO friendsDAO = new FriendsDAO(DataBaseConnection.getConnection());
            HashMap<User, Message> contactList = friendsDAO.getContactList(userId);
            List<JSONObject> response = new ArrayList<>();
            for (Map.Entry<User, Message> entry : contactList.entrySet()) {
                JSONObject userRep = new JSONObject();
                userRep.put("userId", entry.getKey().getId());
                userRep.put("userName", entry.getKey().getUserName());
                JSONObject msgRep = new JSONObject();
                if(entry.getValue() != null){
                    msgRep.put("text", entry.getValue().getText());
                    msgRep.put("status", entry.getValue().getStatus());
                    msgRep.put("timestamp", entry.getValue().getCreatedAt());
                }
                response.add(new JSONObject().put("user", userRep).put("message", msgRep));
            }
            try (Writer writer = resp.getWriter()) {
                writer.write(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
        }
    }
}

/*

Gson --> obj --> {}
friends list ->> last message
USER, MESSAGE, FRIENDS
[
    {
        user : {
                    user_id : 1,
                    user_name : "naga"
                },
        message : {
                    text : "hi",
                    status : "read/sent/delivered/deleted",
                    timestamp : 812376487132
                  }
    },
    {
        user : {
                    user_id : 2,
                    user_name : "c6h6"
                },
        message : {
                    text : "hello",
                    status : "read/sent/delivered/deleted",
                    timestamp : 812376487132
                  }
    }
]
 */
