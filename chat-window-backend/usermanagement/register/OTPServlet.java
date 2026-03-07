package com.base.usermanagement.register;

import com.base.db.RedisConfig;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import com.base.usermanagement.register.service.OTPSender;
import com.base.utilities.GenerateOTP;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

@WebServlet("/generateOtp")
public class OTPServlet extends HttpServlet {
    private final int OPT_EXPIRE = 300;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder data = new StringBuilder();
        String line;
        try(BufferedReader reader = req.getReader()){
            while((line =reader.readLine()) != null){
                data.append(line);
            }
        }
        JSONObject json = new JSONObject(data.toString());
        String OTP = GenerateOTP.getOTP();

        String toEmail = json.getString("mail"); //
        boolean isSend = OTPSender.sendOTP(toEmail, OTP);

        JSONObject res = new JSONObject();
        if(isSend){
            resp.setStatus(HttpServletResponse.SC_OK); // 200
            res.put("message", "OTP send successfully...");
            try(Jedis jedis = RedisConfig.getJedisPool().getResource()){
                jedis.setex("otp:"+toEmail, OPT_EXPIRE, OTP); // otp:krishmoorcodeyt1 : 283743
            }
            System.out.println("generated otp successfully..");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            res.put("error" , "Invalid Email");
        }

        try(Writer writer = resp.getWriter()){
            writer.write(res.toString());
        }
    }
}
