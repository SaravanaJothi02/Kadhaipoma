package com.base.usermanagement.register;

import com.base.dao.UserDAO;
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
import java.sql.Connection;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("register");
        StringBuilder data = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        }
        JSONObject json = new JSONObject(data.toString());
        try (Connection connection = DataBaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            String mail = json.getString("mail");
            if (userDAO.isUserExist(mail)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                resp.getWriter().write(
                        "{\"error\": \"User email already exists.\"}"
                );
                return;
            }
            String userName = json.getString("name");
            String password = PasswordEncryptionUtil.hashPassword(json.getString("pass"));

            boolean isRegistered = userDAO.registerUser(userName, password, mail);
            if(isRegistered){
                resp.setStatus(HttpServletResponse.SC_OK); // 200
                resp.getWriter().write(
                        "{\"message\": \"User registered successfully!\"}"
                );
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                resp.getWriter().write(
                        "{\"error\": \"User registration failed.\"}"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
        }
    }
}


/*



 */
