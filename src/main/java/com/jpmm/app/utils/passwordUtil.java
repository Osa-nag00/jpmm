package com.jpmm.app.utils;

import java.util.Random;

public class passwordUtil {

    public static String generatePassword(int length) {
        String generatedPassword = "";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        generatedPassword = sb.toString();
        return generatedPassword;
    }

}
