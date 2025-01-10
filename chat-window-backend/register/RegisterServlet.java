package register;

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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("register");
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
        if(DataBase.isUserExist(userName)){
            respJson.put("status", 409);
            respJson.put("error", "user already exist");
            respJson.put("message", "A user with this username already exists. Please choose a different username.");
        } else {
            String userId = DataBase.addUser(userName, password);
            respJson.put("status", 200);
            respJson.put("userId", userId);
            respJson.put("message", "user created successfully...");
        }
        try(Writer write = resp.getWriter()){
            resp.setContentType("application/json");
            write.write(respJson.toString());
        }
    }
}
