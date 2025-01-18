package com.base.usermanagement.login;

import com.base.dao.UserDAO;
import com.base.db.DataBase;
import com.base.db.DataBaseConnection;
import com.base.utilities.PasswordEncryptionUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login");
        StringBuilder data = new StringBuilder();
        String line;
        try(BufferedReader reader = req.getReader()){
            while((line = reader.readLine()) != null){
                data.append(line);
            }
        }
        try (Connection connection = DataBaseConnection.getConnection()){
            UserDAO userDAO = new UserDAO(connection);
            JSONObject json = new JSONObject(data.toString());
            String userMail = json.getString("mail");
            String password = json.getString("pass");

            Optional<Integer> userId = userDAO.authenticateUser(userMail, password);
            if(userId.isPresent()){
                resp.setStatus(HttpServletResponse.SC_OK); // 200
                resp.getWriter().write("{\"message\": \"Login successful\", \"userId\": " + userId.get() + "}");
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                resp.getWriter().write("{\"error\": \"Invalid user email or password\"}");
            }
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            resp.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }
}
