package search;

import db.DataBase;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

@WebServlet("/search")
public class SearchUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        List<String[]> userList = DataBase.getUserList(key);

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
        }
    }
}
