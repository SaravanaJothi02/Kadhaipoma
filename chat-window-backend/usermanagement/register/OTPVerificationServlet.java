package com.base.usermanagement.register;

import com.base.db.RedisConfig;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/verifyOtp")
public class OTPVerificationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder data = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        }

        JSONObject json = new JSONObject(data.toString());
        String OTP = json.getString("OTP");
        String mail = json.getString("mail");
        try (Jedis jedis = RedisConfig.getJedisPool().getResource()) {
            String storedOtp = jedis.get("otp:" + mail);
            if (storedOtp == null) {
                resp.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
                resp.getWriter().write(
                        "{\"error\":\"OTP expired\"}"
                );
            } else if (!OTP.equals(storedOtp)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(
                        "{\"error\":\"OTP is Incorrect\"}"
                );
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(
                        "{\"message\":\"OTP Valid\"}"
                );
            }
        } catch (Exception e) {
            resp.getWriter().write("Error : " + e.getMessage());
        }
    }
}
