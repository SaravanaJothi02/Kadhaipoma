package com.base.usermanagement.register.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class OTPSender {
    private static final String FROM_EMAIL = "mail.chatapp1@gmail.com";
    private static final String PASSWORD = "vnab seea mpnt xgcj";
    private static final Session session;

    static {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    public static boolean sendOTP(String toEmail, String OTP){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("OTP from Kadhaippoma");
            message.setText("Your OTP is : "+OTP+"\nThis Will be Expire in 5 minutes");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println("sendOTP exception : "+e.getMessage());
        }
        return false;
    }
}
