package com.base.chat.search;

import com.base.dao.UserDAO;
import com.base.db.DataBaseConnection;
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
import java.util.List;

@WebServlet("/search")
public class SearchUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");

        try {
            UserDAO userDAO = new UserDAO(DataBaseConnection.getConnection());
            List<User> users = userDAO.searchUsers(key);
            List<JSONObject> response = new ArrayList<>();
            for (User user : users) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", user.getId());
                userJson.put("name", user.getUserName());
                response.add(userJson);
            }
            try (Writer writer = resp.getWriter()) {
                writer.write(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
        }

        /*List<String[]> userList = DataBase.getUserList(key);

        resp.setContentType("application/json");
        JSONObject[] json = new JSONObject[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            JSONObject user = new JSONObject();
            user.put("id", userList.get(i)[0]);
            user.put("name", userList.get(i)[1]);
            json[i] = user;
        }
        try(Writer writer = resp.getWriter()){
            writer.write(Arrays.toString(json));
        }*/
    }
}

/*
[
    {
        userId : 1,
        userName : "abc"
    },
    {
        userId : 2,
        userName : "xyz"
    }
]
 */