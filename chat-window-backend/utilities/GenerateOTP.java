package com.base.utilities;

import java.util.Random;

public class GenerateOTP {
    private static Random random;
    
    private GenerateOTP(){}
    
    public static String getOTP(){
        if(random == null) random = new Random();
        return String.valueOf(random.nextInt(999999));
    }
    
}
