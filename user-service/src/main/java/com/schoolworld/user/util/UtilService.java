package com.schoolworld.user.util;

import java.util.regex.Pattern;

public class UtilService {
    public static boolean emailValidation(String email) {
        return Pattern.matches(Constants.EMAIL_PATTERN, email);
    }

}
