package com.smartdev.ufoss.component;

import java.util.regex.Pattern;

public class Validator {
    public static Boolean emailValidate(String email) {
        String regexEmail = "^[\\w]+@[^@\\s]+\\.[^@\\s]+$";
        if ((Pattern.matches(regexEmail, email))) {
            return true;
        }

        return false;
    }

    public static boolean checkNullField(String i) {
        if (i == null || "".equals(i) || "null".equalsIgnoreCase(i)) {
            return true;
        }

        return false;
    }

    public static boolean validateFields(String ...strings) {
        for (String i : strings) {
            if (i == null || "".equals(i) || "null".equalsIgnoreCase(i)) {
                return false;
            }
        }
        return true;
    }
}
