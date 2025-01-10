package login;

import db.DataBase;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

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

        JSONObject json = new JSONObject(data.toString());
        String userName = json.getString("name");
        String password = json.getString("pass");

        JSONObject respJson = new JSONObject();
        String userId = DataBase.isValidUser(userName, password);
        if(!userId.equals("-1")){
            respJson.put("status", 200);
            respJson.put("userId", userId);
            respJson.put("message", "login successfully...");
        } else {
            respJson.put("status", 401);
            respJson.put("error", "Invalid Credential");
            respJson.put("message", "username or password incorrect");
        }
        try(Writer write = resp.getWriter()){
            resp.setContentType("application/json");
            write.write(respJson.toString());
        }

    }
}
