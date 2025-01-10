package contact;

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

@WebServlet("/get-contact-list")
public class GetContactListServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String userId = (String)req.getSession(true).getAttribute("userId");
        String userId = req.getParameter("userId");
        System.out.println(userId);
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
    }
}
