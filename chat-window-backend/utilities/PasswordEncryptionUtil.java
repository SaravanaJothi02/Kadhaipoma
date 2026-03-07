package com.base.utilities;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptionUtil {

    public static String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String plainPassword, String hashPassword){
        return BCrypt.checkpw(plainPassword, hashPassword);
    }
}
